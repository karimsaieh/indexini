package tn.insat.pfe.searchservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.*;
import tn.insat.pfe.searchservice.mq.payloads.FileDeletePayload;
import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;
import tn.insat.pfe.searchservice.mq.payloads.LdaTopicsDescriptionPayload;

import java.io.IOException;
import java.util.List;

public interface ISearchService {
    SearchDto find(String query, Pageable pageable) throws IOException;
    Page<FileGetDto> findByMustNot(String by, String  value, String must, String not, Pageable pageable) throws IOException;
    FileGetDto findById(String id) throws IOException;
    Page<FileGetDto> findAllSortBy(String sortBy, Pageable pageable) throws IOException;
    List<LdaTopicsDescriptionGetDto> getLdaTopics() throws IOException;
    boolean upsertFileIndex(FileIndexPayload fileIndexPayload) throws IOException;
    boolean upsertLdaTopicsDescription(List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList) throws IOException;
    boolean delete(FileDeletePayload fileDeletePayload) throws IOException;


    boolean initEsMapping() throws IOException;
}
