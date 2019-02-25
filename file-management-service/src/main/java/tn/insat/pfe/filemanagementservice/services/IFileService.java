package tn.insat.pfe.filemanagementservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.FileSaveDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public interface IFileService {

    Optional<FileGetDto> findById(Long id);
//    Page<FileGetDto> findAll(Predicate predicate, Pageable pageable);
    boolean saveMultipartFiles(MultipartFile[] multipartFiles) throws IOException;
    //TODO: save individual file
    boolean saveFile(InputStream inputStream, String fileName,String bulkSaveOperationTimestamp, String bulkSaveOperationUuid) throws IOException;
    boolean submitWebScrappingRequest(String pageUrl,boolean entireWebsite,String[] fileTypes);
    boolean downloadAndSaveFile(String fileUrl) throws IOException;
//    FileGetDto update(Long fileId, fileSaveDto fileSaveDto);
//    void deleteById(Long id);
//    void deleteByIdIn(List<Long> filesIds);

}
