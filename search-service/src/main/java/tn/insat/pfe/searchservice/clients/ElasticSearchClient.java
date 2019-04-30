package tn.insat.pfe.searchservice.clients;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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

import static org.elasticsearch.index.query.QueryBuilders.termQuery;


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
    public SearchResponse findAll(String index, String sortBy, Pageable pageable) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int page = pageable == null ? 0 : pageable.getPageNumber();
        int size = pageable == null ? 10000 : pageable.getPageSize();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).from(page*size).size(size);
        if (sortBy !=null) {
            searchSourceBuilder.sort(new FieldSortBuilder(sortBy).order(SortOrder.DESC));
        }
        searchRequest.source(searchSourceBuilder);
        return this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }


    @Override
    public SearchResponse search(String index, String field,  String query, String suggestionField, Pageable pageable) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(field, query);
//                .fuzziness(Fuzziness.AUTO)
//                .maxExpansions(100)
//                .fuzzyTranspositions(true);

        SuggestionBuilder phraseSuggestionBuilder =
                SuggestBuilders.phraseSuggestion(suggestionField).text(query).highlight("<em>","</em>");
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggested_text", phraseSuggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);

        HighlightBuilder highlightBuilder = new HighlightBuilder().preTags("<em>").postTags("</em>").fragmentSize(130);
        HighlightBuilder.Field highlightField =
                new HighlightBuilder.Field(field);
        highlightField.highlighterType("unified");
        highlightBuilder.field(highlightField);

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        searchSourceBuilder.query(matchQueryBuilder).from(page * size).size(size);
        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);
        return this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse findByMustNot(String index, String by, String value, String must, String not, Pageable pageable) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder(by, value);
        QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must (termQuery(by, value))
                .mustNot(termQuery(must, not));
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        searchSourceBuilder.query(qb).from(page * size).size(size);
        searchRequest.source(searchSourceBuilder);
        return this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public GetResponse findById(String index, String type, String id) throws IOException {
        GetRequest getRequest = new GetRequest(index, type, id);
        return this.restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
    }

    @Override
    public boolean upsert(String index, String type, Map map) throws IOException {
        IndexRequest request = new IndexRequest(index, type, (String) map.get("id"))
                .source(map);
        this.restHighLevelClient.index(request, RequestOptions.DEFAULT);
        return true; // nothing is true, everything is permitted
    }

    @Override
    public boolean deleteByRangeFrom(String index, String attribute, int from) throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(QueryBuilders.rangeQuery("id").from(from));
        this.restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        return true; // nothing is true, everything is permitted
    }

    @Override
    public boolean deleteBy(String index, String deleteBy, String value) throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(new TermQueryBuilder(deleteBy, value));
        this.restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        return true;
    }

    @Override
    public boolean deleteById(String index, String type, String value) throws IOException {
        DeleteRequest request = new DeleteRequest(index).type(type).id(value);
        this.restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        return true;
    }

    @Override
    public boolean deleteAll(String index, String type) throws IOException {
        DeleteByQueryRequest request =
                new DeleteByQueryRequest(index);
        request.setQuery(new MatchAllQueryBuilder());
        this.restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        return true;
    }


    @Override
    public boolean indexExists(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        return this.restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    @Override
    public boolean createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
                .put("index.analysis.analyzer.trigram.type", "custom")
                .put("index.analysis.analyzer.trigram.tokenizer", "standard")
                .put("index.analysis.analyzer.trigram.filter", "shingle")
                .put("index.analysis.filter.shingle.type", "shingle")
                .put("index.analysis.filter.shingle.min_shingle_size", 2)
                .put("index.analysis.filter.shingle.max_shingle_size", 3)
        );
        CreateIndexResponse createIndexResponse = this.restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public boolean putMapping(String index, String type) throws IOException {
        // i know this is messed up
        PutMappingRequest request = new PutMappingRequest(index).type(type);
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("id");
                {
                    builder.field("type", "keyword");
                    builder.field("index", "true");
                }
                builder.endObject();
                builder.startObject("thumbnail");
                {
                    builder.field("type", "text");
                    builder.field("index", "false");
                }
                builder.endObject();
                builder.startObject("text");
                {
                    builder.field("type", "text");
                    builder.field("analyzer", "french");
                    builder.startObject("fields");
                    {
                        builder.startObject("suggestion");
                        {
                            builder.field("type", "text");
                            builder.field("analyzer", "trigram");
                        }
                        builder.endObject();
                    }
                    builder.endObject();

                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        request.source(builder);
        AcknowledgedResponse putMappingResponse = this.restHighLevelClient.indices().putMapping(request, RequestOptions.DEFAULT);
        return  putMappingResponse.isAcknowledged();
    }
}
