package tn.insat.pfe.searchservice.clients;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface IElasticSearchClient {
    SearchResponse findAll(String index, String sortBy); //kind of, cause max records is 10k
    SearchResponse search(String index, String field, String query, Pageable pageable);
    SearchResponse findBy(String index, String by, String value, Pageable pageable);
    GetResponse findById(String index, String type, String id);
//    boolean save(FileSaveDto fileSaveDto);
//    boolean deleteByBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp);
//    boolean deleteByBulkSaveOperationUuid(String bulkSaveOperationUuid);
//    boolean update(String id, FileUpdateDto fileUpdateDto);
//    boolean upsertFileIndex(FileIndexPayload fileIndexPayload);
//    boolean upsertLdaTopicsDescription(List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList);
    boolean upsert(String index, String type, Map map);
    boolean deleteByRangeFrom(String index, String attribute, int from);
    boolean deleteBy(String index, String deleteBy, String value);
    boolean deleteById(String index,String type, String value);
}
