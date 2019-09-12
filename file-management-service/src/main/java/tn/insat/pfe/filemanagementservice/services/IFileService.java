package tn.insat.pfe.filemanagementservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.dtos.BulkSaveOperationDto;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.IngestionRequestDto;
import tn.insat.pfe.filemanagementservice.mq.payloads.FileDbUpdatePayload;
import tn.insat.pfe.filemanagementservice.mq.payloads.FileFoundPayload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface IFileService {

    Optional<FileGetDto> findById(Long id);

    Page<FileGetDto> findAll(Predicate predicate, Pageable pageable);

    void saveMultipartFile(MultipartFile multipartFile, String bulkSaveOperationUuid, Long bulkSaveOperationTimestamp) throws IOException;

    BulkSaveOperationDto saveMultipartFiles(MultipartFile[] multipartFiles) throws IOException;
    void updateFile(FileDbUpdatePayload fileDbUpdatePayload);
    BulkSaveOperationDto submitIngestionRequest(IngestionRequestDto ingestionRequestDto) throws IOException;
    boolean downloadAndSaveFile(FileFoundPayload fileFoundPayload) throws IOException;
    InputStream readFile(String url) throws IOException;
    void deleteByBulkSaveOperationTimestamp(String timestamp) throws IOException;
    void deleteByLocation(String location) throws IOException;

    void deleteMultipleFilesByLocation(String[] locations) throws IOException;

    Map<String , Object> indexingStats();
}
