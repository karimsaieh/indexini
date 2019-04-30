package tn.insat.pfe.searchservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.AcknowledgedResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHits;
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

//    @HystrixCommand(fallbackMethod = "cachedFind")
    @Override
    public SearchDto find(String query, Pageable pageable) throws IOException {

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
    public Page<FileGetDto> findByMustNot(String by, String value, String must, String not, Pageable pageable) throws IOException {
        SearchResponse searchResponse = this.elasticSearchClient.findByMustNot(this.fileIndex, by, value, must, not, pageable);
        return ElasticSearchDataExtractorHelper.searchResponseToFileGetDtoPage(searchResponse, pageable);
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
        if (!this.elasticSearchClient.indexExists(this.fileIndex)) {
            this.elasticSearchClient.createIndex(this.fileIndex);
        }
        return this.elasticSearchClient.putMapping(this.fileIndex, this.fileIndexType);
    }

}
