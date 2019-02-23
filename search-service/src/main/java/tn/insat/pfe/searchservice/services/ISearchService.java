package tn.insat.pfe.searchservice.services;

import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.FileSaveDto;
import tn.insat.pfe.searchservice.dtos.FileUpdateDto;
import tn.insat.pfe.searchservice.dtos.SearchDto;

public interface ISearchService {
    SearchDto find(String query, Pageable pageable);
    boolean save(FileSaveDto fileSaveDTo);
//    boolean deleteByTimestamp(String timestamp);
    boolean deleteByBulkOperationUuid(String bulkOperationUuid);
    boolean update(String id, FileUpdateDto fileUpdateDto);
}
