package tn.insat.pfe.filemanagementservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.dtos.BulkSaveOperationDto;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.IngestionRequestDto;
import tn.insat.pfe.filemanagementservice.mq.payloads.FileFoundPayload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface IFileService {

    Optional<FileGetDto> findById(Long id);
    Page<FileGetDto> findAll(Pageable pageable);
    BulkSaveOperationDto saveMultipartFiles(MultipartFile[] multipartFiles) throws IOException;
//    boolean saveFile(InputStream inputStream, String fileName, String contentType, String bulkSaveOperationTimestamp, String bulkSaveOperationUuid) throws IOException;
    BulkSaveOperationDto submitIngestionRequest(IngestionRequestDto ingestionRequestDto) throws IOException;
    boolean downloadAndSaveFile(FileFoundPayload fileFoundPayload) throws IOException;
    InputStream readFile(String url) throws IOException;
    void deleteByBulkSaveOperationTimestamp(String timestamp) throws IOException;
    void deleteByUrl(String url) throws IOException;

    //    Page<FileGetDto> findAll(Predicate predicate, Pageable pageable);
    //    FileGetDto update(Long fileId, fileSaveDto fileSaveDto);
    //    void deleteById(Long id);
    //    void deleteByIdIn(List<Long> filesIds);

}
