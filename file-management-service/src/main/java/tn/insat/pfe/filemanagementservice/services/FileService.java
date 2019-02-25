package tn.insat.pfe.filemanagementservice.services;

import org.apache.commons.io.FilenameUtils;
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
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

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
    public boolean saveMultipartFiles(MultipartFile[] multipartFiles) throws IOException {
        // common to multiple files, don't move them
        String bulkSaveOperationTimestamp = Long.toString(System.currentTimeMillis());
        String bulkSaveOperationUuid = UUID.randomUUID().toString();
        for (MultipartFile multipartFile: multipartFiles) {
            this.saveFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), bulkSaveOperationTimestamp, bulkSaveOperationUuid);
        }
        File file = new File(Long.toString(System.currentTimeMillis()), UUID.randomUUID().toString());
        //TODO: delegate a method to save the file, this method will be used to save files here & in webscrapper consumer
        //TODO deal with (1) (files that have the same name recieved from webscrapper)
//        File file = this.fileMapper.fileSaveDtoToFile(fileSaveDto);
//        file.setName(multipartFile.getOriginalFilename());
//        //*** Temporary Solution
//        this.fileRepository.findByName(fileName).ifPresent(existingFile ->{
//            file.setId(existingFile.getId());
//            file.setIndexed(false);
//        });
//        //***
//        return this.fileMapper.fileToFileGetDto(this.fileRepository.save(file));
        return true;
    }

    @Override
    public boolean saveFile(InputStream inputStream, String fileName, String bulkSaveOperationTimestamp, String bulkSaveOperationUuid) throws IOException {
        this.fileHdfsClient.addFile(inputStream, fileName, bulkSaveOperationTimestamp, bulkSaveOperationUuid);
        return true;
    }

    @Override
    public boolean submitWebScrappingRequest(String pageUrl, boolean entireWebsite, String[] fileTypes) {
        System.out.println("Submitting web scrapping request");
        System.out.println(pageUrl + " - " +entireWebsite+" - ");
        for (String fileType : fileTypes) {
            System.out.print(fileType+ " - ");
        }
        return false;
    }

    //TODO: must be called in rabbitmq consumer
    @Override
    public boolean downloadAndSaveFile(String fileUrl) throws IOException {
//        String bulkSaveOperationTimestamp = from mq;
//        String bulkSaveOperationUuid = from mq;
//        URL url = new URL(fileUrl);
//        String uuid = UUID.randomUUID().toString();
//        java.io.File temporaryfile = java.io.File.createTempFile(String.format("indexini-pfe-java-app-%s",uuid), "");
//        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
//        FileOutputStream fileOutputStream = new FileOutputStream(temporaryfile);
//        fileOutputStream.getChannel()
//                .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
//        this.saveFile(new FileInputStream(temporaryfile), FilenameUtils.getName(url.getPath()),bulkSaveOperationTimestamp,bulkSaveOperationUuid);
//        temporaryfile.delete();
        return true;
    }


}
