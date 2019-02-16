package tn.insat.pfe.filemanagementservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.FileSaveDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IFileService {

    Optional<FileGetDto> findById(Long id);
//    Page<FileGetDto> findAll(Predicate predicate, Pageable pageable);
    FileGetDto save(MultipartFile file, FileSaveDto fileSaveDto) throws IOException;
//    FileGetDto update(Long fileId, fileSaveDto fileSaveDto);
//    void deleteById(Long id);
//    void deleteByIdIn(List<Long> filesIds);

}
