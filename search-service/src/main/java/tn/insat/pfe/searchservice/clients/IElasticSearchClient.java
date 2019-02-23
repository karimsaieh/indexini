package tn.insat.pfe.searchservice.clients;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.FileSaveDto;
import tn.insat.pfe.searchservice.dtos.FileUpdateDto;

public interface IElasticSearchClient {
    SearchResponse find(String query, Pageable pageable);
    boolean save(FileSaveDto fileSaveDto);
//    boolean deleteByTimestamp(String timestamp);
    boolean deleteByBulkOperationUuid(String bulkOperationUuid);
    boolean update(String id, FileUpdateDto fileUpdateDto);
}
