package tn.insat.pfe.searchservice.clients;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Map;

public interface IElasticSearchClient {

    CountResponse count(String index) throws IOException;

    SearchResponse histogramByRange(String index, String field, String from, String to, DateHistogramInterval dateHistogramInterval) throws IOException;

    SearchResponse findAnd(String index, String field, String query) throws IOException;

    SearchResponse findAll(String index, String sortBy, Pageable pageable) throws IOException; //kind of, cause max records is 10k
    SearchResponse search(String index, String field, String query, String suggestionField, Pageable pageable) throws IOException;
    SearchResponse findByMustNot(String index, String by, String value, String must, String not,Pageable pageable) throws IOException;
    GetResponse findById(String index, String type, String id) throws IOException;
    boolean upsert(String index, String type, Map map) throws IOException;

    // actually this is index and not upsert
    boolean upsertAsync(String index, String type, Map map) throws IOException;

    boolean deleteByRangeFrom(String index, String attribute, int from) throws IOException;
    boolean deleteBy(String index, String deleteBy, String value) throws IOException;
    boolean deleteById(String index,String type, String value) throws IOException;

    boolean deleteAll(String index, String type) throws IOException;

    boolean indexExists(String index) throws IOException;

    boolean createIndex(String index, Settings.Builder settings) throws IOException;

    boolean putMapping(String index, String type, XContentBuilder builder) throws IOException;
}
