package tn.insat.pfe.filemanagementservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.clients.IFileHdfsClient;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.FileSaveDto;
import tn.insat.pfe.filemanagementservice.dtos.mappers.FileMapper;
import tn.insat.pfe.filemanagementservice.entities.File;
import tn.insat.pfe.filemanagementservice.repositories.IFileRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class FileService implements IFileService{
    private final IFileRepository fileRepository;
    private final FileMapper fileMapper;
    private final IFileHdfsClient fileHdfsClient;


    @Autowired
    public FileService(IFileRepository fileRepository, FileMapper fileMapper, IFileHdfsClient fileHdfsClient) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.fileHdfsClient = fileHdfsClient;
    }

    @Override
    public Optional<FileGetDto> findById(Long id) {
        return this.fileRepository.findById(id).map(this.fileMapper::fileToFileGetDto);
    }

    @Override
    public FileGetDto save(MultipartFile multipartFile, FileSaveDto fileSaveDto) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        this.fileHdfsClient.addFile(multipartFile.getInputStream(), fileName);
        File file = this.fileMapper.fileSaveDtoToFile(fileSaveDto);
        file.setName(multipartFile.getOriginalFilename());
        //*** Temporary Solution
        this.fileRepository.findByName(fileName).ifPresent(existingFile ->{
            file.setId(existingFile.getId());
            file.setIndexed(false);
        });
        //***
        return this.fileMapper.fileToFileGetDto(this.fileRepository.save(file));
    }
}
