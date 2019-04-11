package tn.insat.pfe.searchservice.clients;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Map;

public interface IElasticSearchClient {
    SearchResponse findAll(String index, String sortBy, Pageable pageable) throws IOException; //kind of, cause max records is 10k
    SearchResponse search(String index, String field, String query, Pageable pageable) throws IOException;
    SearchResponse findBy(String index, String by, String value, Pageable pageable) throws IOException;
    GetResponse findById(String index, String type, String id) throws IOException;
    boolean upsert(String index, String type, Map map) throws IOException;
    boolean deleteByRangeFrom(String index, String attribute, int from) throws IOException;
    boolean deleteBy(String index, String deleteBy, String value) throws IOException;
    boolean deleteById(String index,String type, String value) throws IOException;
}
