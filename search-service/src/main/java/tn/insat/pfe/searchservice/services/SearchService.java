package tn.insat.pfe.searchservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tn.insat.pfe.searchservice.clients.IElasticSearchClient;
import tn.insat.pfe.searchservice.dtos.*;
import tn.insat.pfe.searchservice.entities.redis.SearchDtoCache;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.mq.payloads.FileDeletePayload;
import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;
import tn.insat.pfe.searchservice.mq.payloads.LdaTopicsDescriptionPayload;
import tn.insat.pfe.searchservice.mq.payloads.NotificationPayload;
import tn.insat.pfe.searchservice.mq.producers.IRabbitProducer;
import tn.insat.pfe.searchservice.mq.producers.NotificationProducer;
import tn.insat.pfe.searchservice.services.fallbacks.SearchServiceCacheFallback;
import tn.insat.pfe.searchservice.services.fallbacks.utils.RedisUtils;
import tn.insat.pfe.searchservice.services.helpers.ElasticSearchDataExtractorHelper;
import tn.insat.pfe.searchservice.utils.JsonUtils;

import java.net.ConnectException;
import java.util.List;
import java.util.Map;

@Service
public class SearchService  extends SearchServiceCacheFallback implements ISearchService {
    @Value("${elasticsearch.index.file}")
    private String fileIndex;
    @Value("${elasticsearch.index.file.type}")
    private String fileIndexType;
    @Value("${elasticsearch.index.file.index-field}")
    private String fileIndexField;
    @Value("${elasticsearch.index.lda-topics}")
    private String ldaTopicsIndex;
    @Value("${elasticsearch.index.lda-topics.type}")
    private String ldaTopicsIndexType;
    private final IElasticSearchClient elasticSearchClient;
    private final IRabbitProducer notificationProducer;

    @Autowired
    public SearchService(IElasticSearchClient elasticSearchClient, @Qualifier("NotificationProducer") NotificationProducer notificationProducer) {
        super();
        this.elasticSearchClient = elasticSearchClient;
        this.notificationProducer = notificationProducer;
    }

    @HystrixCommand(fallbackMethod = "cachedFind")
    @Override
    public SearchDto find(String query, Pageable pageable) throws JsonProcessingException {

        List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList = this.getLdaTopics();

        SearchResponse searchResponse = this.elasticSearchClient.search(this.fileIndex, this.fileIndexField, query, pageable);
        SearchHits hits = searchResponse.getHits();
        List<String> suggestionsList = ElasticSearchDataExtractorHelper.searchResponseToSuggestionsList(searchResponse);
        List<FileGetDto> fileGetDtosList = ElasticSearchDataExtractorHelper.searchHitsToGetFileDtoList(hits.getHits());
        long totalHits = hits.getTotalHits();
        float maxScore = hits.getMaxScore();

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        Page<FileGetDto> fileGetDtosPage = new PageImpl<>(fileGetDtosList, PageRequest.of(page,size),totalHits);
        SearchDto searchDto =  new SearchDto(fileGetDtosPage, suggestionsList, maxScore, ldaTopicsDescriptionGetDtosList);
        // got to use another entity cause i can't deserialize abstract type page
        SearchDtoCache searchDtoCache = new SearchDtoCache(fileGetDtosList, page, size, totalHits, suggestionsList, maxScore, ldaTopicsDescriptionGetDtosList);


        try {
            Jedis jedis = new Jedis(this.redisHostname);
            String key = RedisUtils.generateKey(new String[]{this.keyPrefix,"find",query}, pageable);
            jedis.set(key, JsonUtils.objectToJsonString(searchDtoCache));
            jedis.expire(key, 60*60*24);
            jedis.close();
        }catch(Exception ex){
//            ex.printStackTrace();
            System.out.println("redis thrown an exception: SearchService.find");
        }

        return searchDto;
    }

//    @HystrixCommand(fallbackMethod = "cachedFindBy")
    @Override
    public Page<FileGetDto> findBy(String by, String value, Pageable pageable) throws JsonProcessingException {
        SearchResponse searchResponse = this.elasticSearchClient.findBy(this.fileIndex, by, value, pageable);
        Page<FileGetDto> pageFileGetDto =  ElasticSearchDataExtractorHelper.searchResponseToFileGetDtoPage(searchResponse, pageable);
//        this.jedis.set(RedisUtils.generateKey(new String[]{this.keyPrefix,"findBy",by,value}, pageable), JsonUtils.objectToJsonString(pageFileGetDto));
        return pageFileGetDto;
    }

//    @HystrixCommand(fallbackMethod = "cachedFindById")
    @Override
    public FileGetDto findById(String id) throws JsonProcessingException {
        GetResponse getResponse = this.elasticSearchClient.findById(this.fileIndex, this.fileIndexType, id);
        FileGetDto fileGetDto = new ObjectMapper().convertValue(getResponse.getSourceAsMap(), FileGetDto.class);
//        this.jedis.set(RedisUtils.generateKey(new String[]{this.keyPrefix,"findById",id}), JsonUtils.objectToJsonString(fileGetDto));
        return fileGetDto;
    }

//    @HystrixCommand(fallbackMethod = "cachedFindAllSortBy")
    @Override
    public Page<FileGetDto> findAllSortBy(String sortBy, Pageable pageable) throws JsonProcessingException {
        //find all & sort by
        SearchResponse searchResponse = this.elasticSearchClient.findAll(this.fileIndex,  sortBy);
        Page<FileGetDto> pageFileGetDto =  ElasticSearchDataExtractorHelper.searchResponseToFileGetDtoPage(searchResponse, pageable);
//        this.jedis.set(RedisUtils.generateKey(new String[]{this.keyPrefix,"findAllSortBy",sortBy}, pageable), JsonUtils.objectToJsonString(pageFileGetDto));
        return pageFileGetDto;
    }

//    @HystrixCommand(fallbackMethod = "cachedGetLdaTopics")
    @Override
    public List<LdaTopicsDescriptionGetDto> getLdaTopics() throws JsonProcessingException {
        SearchResponse topicsSearchResponse = this.elasticSearchClient.findAll(this.ldaTopicsIndex, null);
        List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList = ElasticSearchDataExtractorHelper
                .searchResponseToLdaTopicsDescriptionGetDtosList(topicsSearchResponse);
//        this.jedis.set(RedisUtils.generateKey(new String[]{this.keyPrefix,"getLdaTopics"}), JsonUtils.objectToJsonString(ldaTopicsDescriptionGetDtosList));
        return ldaTopicsDescriptionGetDtosList;
    }

//    @Override
//    public boolean save(FileSaveDto fileSaveDTo) {
//        return this.elasticSearchClient.save(fileSaveDTo);
//    }
//
//    @Override
//    public boolean deleteByBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp) {
//        return this.elasticSearchClient.deleteByBulkSaveOperationTimestamp(bulkSaveOperationTimestamp);
//    }
//
//    @Override
//    public boolean deleteByBulkSaveOperationUuid(String bulkSaveOperationUuid) {
//        return this.elasticSearchClient.deleteByBulkSaveOperationUuid(bulkSaveOperationUuid);
//    }

    @Override
    public boolean upsertFileIndex(FileIndexPayload fileIndexPayload) throws JsonProcessingException {
        Map fileIndexPayloadMap = fileIndexPayload.toMap();
        boolean result = this.elasticSearchClient.upsert(this.fileIndex, this.fileIndexType, fileIndexPayloadMap);
        //i know it's messy cause elastic may fail, but i got no time
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        NotificationPayload notificationPayload = new NotificationPayload(Constants.FILE_INDEXED, fileIndexPayload.getId(), fileIndexPayload.getFileName());
        String jsonPayload = ow.writeValueAsString(notificationPayload);
        this.notificationProducer.produce(jsonPayload);
        return result;
    }

    @Override
    public boolean upsertLdaTopicsDescription(List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList) {
        for (LdaTopicsDescriptionPayload topic: ldaTopicsDescriptionPayloadList) {
            Map topicMap = topic.toMap();
            this.elasticSearchClient.upsert(this.ldaTopicsIndex, this.ldaTopicsIndexType, topicMap);
        }
        this.elasticSearchClient.deleteByRangeFrom(this.ldaTopicsIndex, "id", ldaTopicsDescriptionPayloadList.size());
        return true; // ?
    }

    @Override
    public boolean delete(FileDeletePayload fileDeletePayload) {
        if(fileDeletePayload.getDeleteBy().equals("id"))
            return this.elasticSearchClient.deleteById(this.fileIndex, this.fileIndexType, fileDeletePayload.getValue());
        else
            return this.elasticSearchClient.deleteBy(this.fileIndex, fileDeletePayload.getDeleteBy(), fileDeletePayload.getValue());
    }

//    @Override
//    public boolean update(String id, FileUpdateDto fileUpdateDto) {
//        return this.elasticSearchClient.update(id, fileUpdateDto);
//    }
}
