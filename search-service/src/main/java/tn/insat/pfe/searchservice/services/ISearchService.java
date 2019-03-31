package tn.insat.pfe.searchservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.*;
import tn.insat.pfe.searchservice.mq.payloads.FileDeletePayload;
import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;
import tn.insat.pfe.searchservice.mq.payloads.LdaTopicsDescriptionPayload;

import java.util.List;

public interface ISearchService {
    SearchDto find(String query, Pageable pageable) throws JsonProcessingException;
    Page<FileGetDto> findBy(String by, String  value, Pageable pageable) throws JsonProcessingException;
    FileGetDto findById(String id) throws JsonProcessingException;
    Page<FileGetDto> findAllSortBy(String sortBy, Pageable pageable) throws JsonProcessingException;
    List<LdaTopicsDescriptionGetDto> getLdaTopics() throws JsonProcessingException;
//    boolean save(FileSaveDto fileSaveDTo);
//    boolean deleteByBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp);
//    boolean deleteByBulkSaveOperationUuid(String bulkSaveOperationUuid);
//    boolean update(String id, FileUpdateDto fileUpdateDto);
    boolean upsertFileIndex(FileIndexPayload fileIndexPayload) throws JsonProcessingException;
    boolean upsertLdaTopicsDescription(List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList);
    boolean delete(FileDeletePayload fileDeletePayload);
}
