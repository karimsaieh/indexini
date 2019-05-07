package tn.insat.pfe.searchservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.insat.pfe.searchservice.clients.IElasticSearchClient;
import tn.insat.pfe.searchservice.dtos.*;
import tn.insat.pfe.searchservice.entities.redis.SearchDtoCache;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.mq.payloads.*;
import tn.insat.pfe.searchservice.mq.producers.FileDbUpdateProducer;
import tn.insat.pfe.searchservice.mq.producers.IRabbitProducer;
import tn.insat.pfe.searchservice.mq.producers.NotificationProducer;
import tn.insat.pfe.searchservice.repositories.redis.ISearchDtoCacheRepository;
import tn.insat.pfe.searchservice.services.fallbacks.SearchServiceCacheFallback;
import tn.insat.pfe.searchservice.services.fallbacks.utils.RedisUtils;
import tn.insat.pfe.searchservice.services.helpers.ElasticSearchDataExtractorHelper;
import tn.insat.pfe.searchservice.utils.JsonUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService  extends SearchServiceCacheFallback implements ISearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    @Value("${pfe_elasticsearch_index_file}")
    private String fileIndex;
    @Value("${pfe_elasticsearch_index_file_type}")
    private String fileIndexType;
    @Value("${pfe_elasticsearch_index_file_index-field}")
    private String fileIndexField;
    @Value("${pfe_elasticsearch_index_file_index_suggestion-field}")
    private String suggestionField;
    @Value("${pfe_elasticsearch_index_lda-topics}")
    private String ldaTopicsIndex;
    @Value("${pfe_elasticsearch_index_lda-topics_type}")
    private String ldaTopicsIndexType;
    @Value("${pfe_elasticsearch_index_history}")
    private String historyIndex;
    @Value("${pfe_elasticsearch_index_history_type}")
    private String historyIndextype;
    @Value("${pfe_elasticsearch_index_history-field}")
    private String historyIndexField;
    @Value("${pfe_elasticsearch_index_raw_history}")
    private String rawHistoryIndex;
    @Value("${pfe_elasticsearch_index_raw_history_type}")
    private String rawHistoryIndextype;
    private final IElasticSearchClient elasticSearchClient;
    private final IRabbitProducer notificationProducer;
    private final IRabbitProducer fileDbUpdateProducer;
    private final ISearchDtoCacheRepository searchDtoCacheRepository;

    @Autowired
    public SearchService(IElasticSearchClient elasticSearchClient,
                         @Qualifier("NotificationProducer") NotificationProducer notificationProducer,
                         @Qualifier("FileDbUpdateProducer") FileDbUpdateProducer fileDbUpdateProducer,
                         ISearchDtoCacheRepository searchDtoCacheRepository) {
        this.elasticSearchClient = elasticSearchClient;
        this.notificationProducer = notificationProducer;
        this.fileDbUpdateProducer = fileDbUpdateProducer;
        this.searchDtoCacheRepository = searchDtoCacheRepository;
    }


    @HystrixCommand(fallbackMethod = "cachedFind")
    @Override
    public SearchDto find(String query, Pageable pageable) throws IOException {

        this.saveHistory(query);
        this.saveRawHistory(query);

        List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList = this.getLdaTopics();

        SearchResponse searchResponse = this.elasticSearchClient.search(this.fileIndex, this.fileIndexField, query, this.suggestionField , pageable);
        SearchHits hits = searchResponse.getHits();
        Map<String, String> suggestionMap = ElasticSearchDataExtractorHelper.searchResponseToSuggestionsList(searchResponse);
        List<FileGetDto> fileGetDtosList = ElasticSearchDataExtractorHelper.searchHitsToGetFileDtoList(hits.getHits(),this.fileIndexField);
        long totalHits = hits.getTotalHits();
        float maxScore = hits.getMaxScore();

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        Page<FileGetDto> fileGetDtosPage = new PageImpl<>(fileGetDtosList, PageRequest.of(page,size),totalHits);
        SearchDto searchDto =  new SearchDto(fileGetDtosPage, suggestionMap, maxScore, ldaTopicsDescriptionGetDtosList);
        // got to use another entity cause i can't deserialize abstract type page
        SearchDtoCache searchDtoCache = new SearchDtoCache(
                RedisUtils.generateKey(new String[]{this.keyPrefix,"find",query}, pageable),
                fileGetDtosList, page, size, totalHits, suggestionMap, maxScore, ldaTopicsDescriptionGetDtosList);
        try {
            this.searchDtoCacheRepository.save(searchDtoCache);
        }catch(Exception ex){
            //  need to contain redis error,  so we can stay here and not go to the fallback
            logger.error("redis thrown an exception: SearchService.find,", ex);
        }
        return searchDto;
    }

    @Override
    public List<Map<String, String>> searchAsYouType(String query) throws IOException {
        SearchResponse searchResponse = this.elasticSearchClient.findAnd(this.historyIndex,this.historyIndexField, query);
        return ElasticSearchDataExtractorHelper.searchResponseToSearchAsYouTypeListOfMap(searchResponse, this.historyIndexField);
    }

    private void saveHistory(String query) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", query);
        map.put(this.historyIndexField, query);
        map.put("date", new Date()); // not needed anymore ?
        this.elasticSearchClient.upsertAsync(this.historyIndex, this.historyIndextype, map);
    }

    private void saveRawHistory(String query) throws  IOException {
        Map<String, Object> map = new HashMap<>();
        map.put(this.historyIndexField, query);
        map.put("date", new Date());
        this.elasticSearchClient.upsertAsync(this.rawHistoryIndex, this.rawHistoryIndextype, map);
    }

    @Override
    public long countSearch() throws IOException {
        return this.elasticSearchClient.count(this.rawHistoryIndex).getCount();
    }


    @Override
    public Page<FileGetDto> findByMustNot(String by, String value, String must, String not, Pageable pageable) throws IOException {
        SearchResponse searchResponse = this.elasticSearchClient.findByMustNot(this.fileIndex, by, value, must, not, pageable);
        return ElasticSearchDataExtractorHelper.searchResponseToFileGetDtoPage(searchResponse, pageable);
    }


    @Override
    public List<Map<String, Object>> histogramByRange(String range) throws IOException {
        DateHistogramInterval dateHistogramInterval;
        String from;
        String to ="now";
        switch (range) {
            case "lastHour":
                dateHistogramInterval = DateHistogramInterval.MINUTE;
                from = "now-60m/m";
                break;
            case "lastDay":
                dateHistogramInterval = DateHistogramInterval.HOUR;
                from = "now-"+(60*24)+"m/m";
                break;
            case "lastWeek":
                dateHistogramInterval = DateHistogramInterval.DAY;
                from = "now-"+(60*24*7)+"m/m";
                break;
            case "lastMonth":
                dateHistogramInterval = DateHistogramInterval.DAY;
                from = "now-"+(60*24*7*31)+"m/m";
                break;
            case "lastYear":
                dateHistogramInterval = DateHistogramInterval.MONTH;
                from = "now-"+(60*24*7*30*12)+"m/m";
                break;
            default:
                dateHistogramInterval = DateHistogramInterval.MINUTE;
                from = "now-60m/m";
        }
        SearchResponse searchResponse = this.elasticSearchClient
                .histogramByRange(this.rawHistoryIndex, "date", from, to, dateHistogramInterval);
        return ElasticSearchDataExtractorHelper.searchResponseTohHistogramByRange(searchResponse);
    }

    @Override
    public Page<Map<String, String>> findAllRawHistory(Pageable pageable) throws IOException {
        SearchResponse searchResponse = this.elasticSearchClient.findAll(this.rawHistoryIndex, "date", pageable);
        return ElasticSearchDataExtractorHelper.searchResponseToRawHistoryPageListOfMap(searchResponse, pageable);
    }

    @Override
    public FileGetDto findById(String id) throws IOException {
        GetResponse getResponse = this.elasticSearchClient.findById(this.fileIndex, this.fileIndexType, id);
        return ElasticSearchDataExtractorHelper.getResponseToFileGetDto(getResponse);
    }

    @Override
    public Page<FileGetDto> findAllSortBy(String sortBy, Pageable pageable) throws IOException {
        //find all & sort by
        SearchResponse searchResponse = this.elasticSearchClient.findAll(this.fileIndex,  sortBy, pageable);
        return ElasticSearchDataExtractorHelper.searchResponseToFileGetDtoPage(searchResponse, pageable);
    }

    @Override
    public List<LdaTopicsDescriptionGetDto> getLdaTopics() throws IOException {
        SearchResponse topicsSearchResponse = this.elasticSearchClient.findAll(this.ldaTopicsIndex, null, null);
        return ElasticSearchDataExtractorHelper.searchResponseToLdaTopicsDescriptionGetDtosList(topicsSearchResponse);
    }

    @Override
    public boolean upsertFileIndex(FileIndexPayload fileIndexPayload) throws IOException {
        Map fileIndexPayloadMap = fileIndexPayload.toMap();
        boolean result = this.elasticSearchClient.upsert(this.fileIndex, this.fileIndexType, fileIndexPayloadMap);
        //i know it's messy cause elastic may fail, but i got no time

        NotificationPayload notificationPayload = new NotificationPayload(Constants.FILE_INDEXED, fileIndexPayload.getId(), fileIndexPayload.getFileName());
        this.notificationProducer.produce(JsonUtils.objectToJsonString(notificationPayload));
        FileDbUpdatePayload fileDbUpdatePayload = new FileDbUpdatePayload(fileIndexPayload.getId(),true);
        this.fileDbUpdateProducer.produce(JsonUtils.objectToJsonString(fileDbUpdatePayload));
        return result;
    }

    @Override
    public boolean upsertLdaTopicsDescription(List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList) throws IOException {
        this.searchDtoCacheRepository.deleteAll();//files got reindexed ? => delete all cache
        try{
            this.elasticSearchClient.deleteAll(this.ldaTopicsIndex, this.ldaTopicsIndexType);
        } catch(ElasticsearchStatusException ex) {
            logger.error(ex.toString());
            logger.error("error in upserting topics: no such index");
        }
        for (LdaTopicsDescriptionPayload topic: ldaTopicsDescriptionPayloadList) {
            Map topicMap = topic.toMap();
            this.elasticSearchClient.upsert(this.ldaTopicsIndex, this.ldaTopicsIndexType, topicMap);
        }
        return true; // ?
    }

    @Override
    public boolean delete(FileDeletePayload fileDeletePayload) throws IOException {
        this.searchDtoCacheRepository.deleteAll();
        if(fileDeletePayload.getDeleteBy().equals("id"))
            return this.elasticSearchClient.deleteById(this.fileIndex, this.fileIndexType, fileDeletePayload.getValue());
        else
            return this.elasticSearchClient.deleteBy(this.fileIndex, fileDeletePayload.getDeleteBy(), fileDeletePayload.getValue());
    }


    @Override
    public boolean initEsMapping() throws IOException {
        Settings.Builder fileIndexSettings =  Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
                .put("index.analysis.analyzer.trigram.type", "custom")
                .put("index.analysis.analyzer.trigram.tokenizer", "standard")
                .put("index.analysis.analyzer.trigram.filter", "shingle")
                .put("index.analysis.filter.shingle.type", "shingle")
                .put("index.analysis.filter.shingle.min_shingle_size", 2)
                .put("index.analysis.filter.shingle.max_shingle_size", 3);

        Settings.Builder historyIndexSettings =  Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)

                .put("index.analysis.analyzer.autocomplete.tokenizer", "autocomplete")
                .put("index.analysis.analyzer.autocomplete.filter", "lowercase")
                .put("index.analysis.analyzer.autocomplete_search.tokenizer", "lowercase")
                .put("index.analysis.tokenizer.autocomplete.type", "edge_ngram")
                .put("index.analysis.tokenizer.autocomplete.min_gram", 1)
                .put("index.analysis.tokenizer.autocomplete.max_gram", 30)
                .put("index.analysis.tokenizer.autocomplete.token_chars", "letter")

                ;

        XContentBuilder fileIndexBuilder = XContentFactory.jsonBuilder();
        fileIndexBuilder.startObject();
        {
            fileIndexBuilder.startObject("properties");
            {
                fileIndexBuilder.startObject("id");
                {
                    fileIndexBuilder.field("type", "keyword");
                    fileIndexBuilder.field("index", "true");
                }
                fileIndexBuilder.endObject();
                fileIndexBuilder.startObject("thumbnail");
                {
                    fileIndexBuilder.field("type", "text");
                    fileIndexBuilder.field("index", "false");
                }
                fileIndexBuilder.endObject();
                fileIndexBuilder.startObject("text");
                {
                    fileIndexBuilder.field("type", "text");
                    fileIndexBuilder.field("analyzer", "french");
                    fileIndexBuilder.startObject("fields");
                    {
                        fileIndexBuilder.startObject("suggestion");
                        {
                            fileIndexBuilder.field("type", "text");
                            fileIndexBuilder.field("analyzer", "trigram");
                        }
                        fileIndexBuilder.endObject();
                    }
                    fileIndexBuilder.endObject();

                }
                fileIndexBuilder.endObject();
            }
            fileIndexBuilder.endObject();
        }
        fileIndexBuilder.endObject();

        XContentBuilder historyIndexBuilder = XContentFactory.jsonBuilder();
        historyIndexBuilder.startObject();
        {
            historyIndexBuilder.startObject("properties");
            {
                historyIndexBuilder.startObject("history");
                {
                    historyIndexBuilder.field("type", "text");
                    historyIndexBuilder.field("analyzer", "autocomplete");
                    historyIndexBuilder.field("search_analyzer", "autocomplete_search");
                }
                historyIndexBuilder.endObject();
            }
            historyIndexBuilder.endObject();
        }
        historyIndexBuilder.endObject();

        if (!this.elasticSearchClient.indexExists(this.fileIndex)) {
            this.elasticSearchClient.createIndex(this.fileIndex, fileIndexSettings);
        }
        if (!this.elasticSearchClient.indexExists(this.historyIndex)) {
            this.elasticSearchClient.createIndex(this.historyIndex, historyIndexSettings);
        }
        boolean ok =  this.elasticSearchClient.putMapping(this.fileIndex, this.fileIndexType, fileIndexBuilder);
        ok = ok && this.elasticSearchClient.putMapping(this.historyIndex, this.historyIndextype, historyIndexBuilder );
        return ok;
    }

}
