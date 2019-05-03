package tn.insat.pfe.searchservice.clients;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchClient.class);

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
    public CountResponse count(String index) throws IOException {
        CountRequest countRequest = new CountRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        countRequest.source(searchSourceBuilder);
        return this.restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse histogramByRange(String index, String field, String from, String to, DateHistogramInterval dateHistogramInterval) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        DateHistogramAggregationBuilder histogramAggregation = AggregationBuilders.dateHistogram("date_histogram")
                .field(field).dateHistogramInterval(dateHistogramInterval);
        DateRangeAggregationBuilder rangeAggregation = AggregationBuilders.dateRange("date_range")
                .field(field).addRange(from, to).subAggregation(histogramAggregation);
        searchSourceBuilder.aggregation(rangeAggregation);
        searchSourceBuilder.fetchSource(false);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        return this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse findAnd(String index, String field, String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(field, query).operator(Operator.AND);
        HighlightBuilder highlightBuilder = new HighlightBuilder().preTags("<em>").postTags("</em>").fragmentSize(1000);
        HighlightBuilder.Field highlightField =
                new HighlightBuilder.Field(field);
        highlightField.highlighterType("unified");
        highlightBuilder.field(highlightField);
        searchSourceBuilder.query(matchQueryBuilder);
        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);
        return this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
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

    @Override // actually this is index and not upsert
    public boolean upsert(String index, String type, Map map) throws IOException {
        IndexRequest request = new IndexRequest(index, type, (String) map.get("id"))
                .source(map);
        this.restHighLevelClient.index(request, RequestOptions.DEFAULT);
        return true; // nothing is true, everything is permitted
    }

    @Override // actually this is index and not upsert
    public boolean upsertAsync(String index, String type, Map map) throws IOException {
        IndexRequest request = new IndexRequest(index, type, (String) map.get("id"))
                .source(map);
        ActionListener listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                logger.info("index async done");
            }
            @Override
            public void onFailure(Exception e) {
                String msg = "error in index async ==> \n" + e.toString();
                logger.error(msg);
            }
        };
        this.restHighLevelClient.indexAsync(request, RequestOptions.DEFAULT, listener);
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
    public boolean createIndex(String index, Settings.Builder settings) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.settings(settings);
        CreateIndexResponse createIndexResponse = this.restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public boolean putMapping(String index, String type, XContentBuilder builder) throws IOException {
        PutMappingRequest request = new PutMappingRequest(index).type(type);
        request.source(builder);
        AcknowledgedResponse putMappingResponse = this.restHighLevelClient.indices().putMapping(request, RequestOptions.DEFAULT);
        return  putMappingResponse.isAcknowledged();
    }
}
