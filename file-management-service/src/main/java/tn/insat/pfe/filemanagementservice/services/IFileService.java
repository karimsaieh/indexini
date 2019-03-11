package tn.insat.pfe.filemanagementservice.services;

import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.dtos.BulkSaveOperationDto;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.WebScrapingRequestDto;
import tn.insat.pfe.filemanagementservice.mq.payloads.FilePayload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface IFileService {

    Optional<FileGetDto> findById(Long id);
    BulkSaveOperationDto saveMultipartFiles(MultipartFile[] multipartFiles) throws IOException;
    boolean saveFile(InputStream inputStream, String fileName, String contentType, String bulkSaveOperationTimestamp, String bulkSaveOperationUuid) throws IOException;
    BulkSaveOperationDto submitWebScrapingRequest(WebScrapingRequestDto webScrapingRequestDto) throws IOException;
    boolean downloadAndSaveFile(FilePayload filePayload) throws IOException;
    //    Page<FileGetDto> findAll(Predicate predicate, Pageable pageable);
    //    FileGetDto update(Long fileId, fileSaveDto fileSaveDto);
    //    void deleteById(Long id);
    //    void deleteByIdIn(List<Long> filesIds);

}
