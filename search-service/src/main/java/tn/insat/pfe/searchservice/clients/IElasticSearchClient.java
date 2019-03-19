package tn.insat.pfe.searchservice.clients;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.FileSaveDto;
import tn.insat.pfe.searchservice.dtos.FileUpdateDto;
import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;
import tn.insat.pfe.searchservice.mq.payloads.LdaTopicsDescriptionPayload;

import java.util.List;
import java.util.Map;

public interface IElasticSearchClient {
    SearchResponse findAll(String index); //kind of, cause max records is 10k
    SearchResponse search(String index, String field, String query, Pageable pageable);
//    boolean save(FileSaveDto fileSaveDto);
//    boolean deleteByBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp);
//    boolean deleteByBulkSaveOperationUuid(String bulkSaveOperationUuid);
//    boolean update(String id, FileUpdateDto fileUpdateDto);
//    boolean upsertFileIndex(FileIndexPayload fileIndexPayload);
//    boolean upsertLdaTopicsDescription(List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList);
    boolean upsert(String index, String type, Map map);
    boolean deleteByRangeFrom(String index, String attribute, int from);
}
