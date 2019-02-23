package tn.insat.pfe.searchservice.clients;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.config.IElasticSearchProvider;
import tn.insat.pfe.searchservice.dtos.FileSaveDto;
import tn.insat.pfe.searchservice.dtos.FileUpdateDto;

import javax.annotation.PostConstruct;
import java.io.IOException;


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
    public SearchResponse find(String query, Pageable pageable) {
        SearchRequest searchRequest = new SearchRequest(this.elasticSearchProvider.getIndex());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("text", query)
                .fuzziness(Fuzziness.AUTO)
                .maxExpansions(100)
                .fuzzyTranspositions(true);

        SuggestionBuilder termSuggestionBuilder =
                SuggestBuilders.termSuggestion("text").text(query);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggest_text", termSuggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        searchSourceBuilder.query(matchQueryBuilder).from(page * size).size(size);
        searchRequest.source(searchSourceBuilder);
        try {
            return this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.find: Elastic search not working properly");
        }
    }

    @Override
    public boolean save(FileSaveDto fileSaveDto) {
        IndexRequest request = new IndexRequest(this.elasticSearchProvider.getIndex())
                .id(String.format("%s-%s-%s", fileSaveDto.getBulkSaveOperationTimestamp(), fileSaveDto.getBulkSaveOperationUuid(),fileSaveDto.getFileName()))
                .type(this.elasticSearchProvider.getType())
                .source(fileSaveDto.toMap()
                )
                .opType("create");
        try {
            IndexResponse indexResponse = this.restHighLevelClient.index(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.save: Elastic search not working properly");
        }
    }
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

    @Override
    public boolean deleteByBulkSaveOperationUuid(String bulkSaveOperationUuid) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(this.elasticSearchProvider.getIndex());
        request.setQuery(new TermQueryBuilder("bulkSaveOperationUuid", bulkSaveOperationUuid));

        try {
            BulkByScrollResponse bulkResponse =
                    this.restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.deleteByBulkSaveOperationTimestamp: Elastic search not working properly");
        }
    }

    @Override
    public boolean update(String id, FileUpdateDto fileUpdateDto) {
        UpdateRequest request = new UpdateRequest(
                this.elasticSearchProvider.getIndex(),
                this.elasticSearchProvider.getType(),
                id
        ).doc(
                fileUpdateDto.toMap()
        );
        try {
            UpdateResponse updateResponse = this.restHighLevelClient.update(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ElasticSearchClient.update: Elastic search not working properly");
        }
    }
}
