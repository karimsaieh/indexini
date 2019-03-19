package tn.insat.pfe.searchservice.services;

import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.FileSaveDto;
import tn.insat.pfe.searchservice.dtos.FileUpdateDto;
import tn.insat.pfe.searchservice.dtos.SearchDto;
import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;
import tn.insat.pfe.searchservice.mq.payloads.LdaTopicsDescriptionPayload;

import java.util.List;

public interface ISearchService {
    SearchDto find(String query, Pageable pageable);
//    boolean save(FileSaveDto fileSaveDTo);
//    boolean deleteByBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp);
//    boolean deleteByBulkSaveOperationUuid(String bulkSaveOperationUuid);
//    boolean update(String id, FileUpdateDto fileUpdateDto);
    boolean upsertFileIndex(FileIndexPayload fileIndexPayload);
    boolean upsertLdaTopicsDescription(List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList);
}
