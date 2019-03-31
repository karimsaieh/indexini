package tn.insat.pfe.searchservice.clients;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.config.IElasticSearchProvider;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;


@Component
public class ElasticSearchClient implements IElasticSearchClient {
    private RestHighLevelClient restHighLevelClient;
    private final IElasticSearchProvider elasticSearchProvider;
    @Autowired
    public ElasticSearchClient(IElasticSearchProvider elasticSearchProvider) {
        this.elasticSearchProvider = elasticSearchProvider;
    }
    @PostConstruct
    public void init() {
        this.restHighLevelClient = this.elasticSearchProvider.getRestHighLevelClient();
    }

    @Override
    public SearchResponse findAll(String index, String sortBy) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).from(0).size(10000);
        if (sortBy !=null) {
            searchSourceBuilder.sort(new FieldSortBuilder(sortBy).order(SortOrder.DESC));
        }
        searchRequest.source(searchSourceBuilder);
        try {
            return this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.findAll: Elastic search not working properly");
        }
    }


    @Override
    public SearchResponse search(String index, String field,  String query, Pageable pageable) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(field, query)
                .fuzziness(Fuzziness.AUTO)
                .maxExpansions(100)
                .fuzzyTranspositions(true);
        SuggestionBuilder termSuggestionBuilder =
                SuggestBuilders.termSuggestion(field).text(query);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggest_text", termSuggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        searchSourceBuilder.query(matchQueryBuilder).from(page * size).size(size);
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).from(0).size(10000);
        searchRequest.source(searchSourceBuilder);
        try {
            return this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.search: Elastic search not working properly");
        }
    }

    @Override
    public SearchResponse findBy(String index, String by, String value, Pageable pageable) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder(by, value);
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        searchSourceBuilder.query(termQueryBuilder).from(page * size).size(size);
        searchRequest.source(searchSourceBuilder);
        try {
            return this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.findBy: Elastic search not working properly");
        }
    }

    @Override
    public GetResponse findById(String index, String type, String id) {
        GetRequest getRequest = new GetRequest(index, type, id);
        try {
            return this.restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.findById: Elastic search not working properly");
        }
    }

//    @Override
//    public boolean save(FileSaveDto fileSaveDto) {
//        IndexRequest request = new IndexRequest(this.elasticSearchProvider.getIndex())
//                .id(String.format("%s-%s-%s", fileSaveDto.getBulkSaveOperationTimestamp(), fileSaveDto.getBulkSaveOperationUuid(),fileSaveDto.getFileName()))
//                .type(this.elasticSearchProvider.getType())
//                .source(fileSaveDto.toMap())
//                .opType("create");
//        try {
//            IndexResponse indexResponse = this.restHighLevelClient.index(request, RequestOptions.DEFAULT);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("ElasticSearchClient.save: Elastic search not working properly");
//        }
//    }
//
//    @Override
//    public boolean deleteByBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp) {
//        DeleteByQueryRequest request = new DeleteByQueryRequest(this.elasticSearchProvider.getIndex());
//        request.setQuery(new TermQueryBuilder("bulkSaveOperationTimestamp", bulkSaveOperationTimestamp));
//
//        try {
//            BulkByScrollResponse bulkResponse =
//                    this.restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("ElasticSearchClient.deleteByBulkSaveOperationTimestamp: Elastic search not working properly");
//        }
//    }

//    @Override
//    public boolean deleteByBulkSaveOperationUuid(String bulkSaveOperationUuid) {
//        return true;
//        //TODO: Should be generic
//        //        DeleteByQueryRequest request = new DeleteByQueryRequest(this.fileIndex);
////        request.setQuery(new TermQueryBuilder("bulkSaveOperationUuid", bulkSaveOperationUuid));
////
////        try {
////            BulkByScrollResponse bulkResponse =
////                    this.restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
////            return true;
////        } catch (IOException e) {
////            e.printStackTrace();
////            throw new RuntimeException("ElasticSearchClient.deleteByBulkSaveOperationTimestamp: Elastic search not working properly");
////        }
//    }

    @Override
    public boolean upsert(String index, String type, Map map) {
        UpdateRequest request = new UpdateRequest(index, type, (String) map.get("id"))
                .doc(map);
        request.upsert(map);
        try {
            UpdateResponse updateResponse = this.restHighLevelClient.update(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.upsertFileIndex: Elastic search not working properly");
        }
    }

    @Override
    public boolean deleteByRangeFrom(String index, String attribute, int from) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(QueryBuilders.rangeQuery("id").from(from));
        try {
            BulkByScrollResponse bulkResponse =
                    this.restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.deleteByIdRange: Elastic search not working properly");
        }
    }

    @Override
    public boolean deleteBy(String index, String deleteBy, String value) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(new TermQueryBuilder(deleteBy, value));
        try {
            BulkByScrollResponse bulkResponse =
                    this.restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.delete: Elastic search not working properly");
        }
    }

    @Override
    public boolean deleteById(String index, String type, String value) {
        DeleteRequest request = new DeleteRequest(index).type(type).id(value);
        try {
            DeleteResponse deleteResponse =  this.restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.delete: Elastic search not working properly");
        }
    }

//    @Override
//    public boolean upsertFileIndex(FileIndexPayload fileIndexPayload) {
//        Map fileIndexPayloadMap = fileIndexPayload.toMap();
//        UpdateRequest request = new UpdateRequest(this.fileIndex, this.fileIndexType, fileIndexPayload.getId())
//                .doc(fileIndexPayloadMap);
//        request.upsert(fileIndexPayloadMap);
//        try {
//            UpdateResponse updateResponse = this.restHighLevelClient.update(request, RequestOptions.DEFAULT);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("ElasticSearchClient.upsertFileIndex: Elastic search not working properly");
//        }
//    }
//
//    @Override
//    public boolean upsertLdaTopicsDescription(List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList) {
//        for (LdaTopicsDescriptionPayload topic: ldaTopicsDescriptionPayloadList) {
//            Map topicMap = topic.toMap();
//            UpdateRequest request = new UpdateRequest(this.ldaTopicsIndex, this.ldaTopicsIndexType, topic.getId())
//                    .doc(topicMap);
//            request.upsert(topicMap);
//        }
//    }

//    @Override
//    public boolean update(String id, FileUpdateDto fileUpdateDto) {
//        UpdateRequest request = new UpdateRequest(
//                this.elasticSearchProvider.getIndex(),
//                this.elasticSearchProvider.getType(),
//                id
//        ).doc(
//                fileUpdateDto.toMap()
//        );
//        try {
//            UpdateResponse updateResponse = this.restHighLevelClient.update(request, RequestOptions.DEFAULT);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("ElasticSearchClient.update: Elastic search not working properly");
//        }
//    }
}
