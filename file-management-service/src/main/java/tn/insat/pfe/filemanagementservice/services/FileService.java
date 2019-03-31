package tn.insat.pfe.filemanagementservice.services;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.clients.IFileHdfsClient;
import tn.insat.pfe.filemanagementservice.dtos.BulkSaveOperationDto;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.IngestionRequestDto;
import tn.insat.pfe.filemanagementservice.dtos.mappers.FileMapper;
import tn.insat.pfe.filemanagementservice.entities.File;
import tn.insat.pfe.filemanagementservice.mq.Constants;
import tn.insat.pfe.filemanagementservice.mq.payloads.FileDeletePayload;
import tn.insat.pfe.filemanagementservice.mq.payloads.FileFoundPayload;
import tn.insat.pfe.filemanagementservice.mq.payloads.IngestionRequestPayload;
import tn.insat.pfe.filemanagementservice.mq.payloads.NotificationPayload;
import tn.insat.pfe.filemanagementservice.mq.producers.IRabbitProducer;
import tn.insat.pfe.filemanagementservice.repositories.IFileRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileService implements IFileService{
    private final IFileRepository fileRepository;
    private final FileMapper fileMapper;
    private final IFileHdfsClient fileHdfsClient;
    private final IRabbitProducer webScrapingProducer;
    private final IRabbitProducer ftpExplorerProducer;
    private final IRabbitProducer notificationProducer;
    private final IRabbitProducer fileDeleteProducer;
    @Value("${hdfs.save-directory}")
    private String saveDirectory;

    @Autowired
    public FileService(IFileRepository fileRepository, FileMapper fileMapper, IFileHdfsClient fileHdfsClient,
                       @Qualifier("WebScrapingProducer") IRabbitProducer webScrapingProducer,
                       @Qualifier("FtpExplorerProducer") IRabbitProducer ftpExplorerProducer,
                       @Qualifier("FileDeleteProducer") IRabbitProducer fileDeleteProducer,
                       @Qualifier("NotificationProducer") IRabbitProducer notificationProducer) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.fileHdfsClient = fileHdfsClient;
        this.webScrapingProducer = webScrapingProducer;
        this.ftpExplorerProducer = ftpExplorerProducer;
        this.fileDeleteProducer = fileDeleteProducer;
        this.notificationProducer = notificationProducer;
    }

    @Override
    public Optional<FileGetDto> findById(Long id) {
        return this.fileRepository.findById(id).map(this.fileMapper::fileToFileGetDto);
    }

    @Override
    public Page<FileGetDto> findAll(Pageable pageable) {
        Page<File> page = this.fileRepository.findAll(pageable);
        List<FileGetDto> fileGetDtoList = page
                .getContent()
                .stream()
                .map(this.fileMapper::fileToFileGetDto)
                .collect(Collectors.toList());
        return new PageImpl<>(fileGetDtoList, pageable, page.getTotalElements());
    }

    @Override
    public BulkSaveOperationDto saveMultipartFiles(MultipartFile[] multipartFiles) throws IOException {
        Long bulkSaveOperationTimestamp = System.currentTimeMillis();
        String bulkSaveOperationUuid = UUID.randomUUID().toString();
        for (MultipartFile multipartFile: multipartFiles) {
            this.saveFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),multipartFile.getContentType()
                    ,bulkSaveOperationTimestamp, bulkSaveOperationUuid);
        }
        return new BulkSaveOperationDto(bulkSaveOperationTimestamp,bulkSaveOperationUuid);
    }


    public boolean saveFile(InputStream inputStream, String fileName,String contentType, Long bulkSaveOperationTimestamp, String bulkSaveOperationUuid) throws IOException {
        String directoryUrl = this.saveDirectory + "/" + bulkSaveOperationTimestamp + "/" + bulkSaveOperationUuid;
        // save to hdfs
        this.fileHdfsClient.addFile(directoryUrl, fileName, inputStream);
        //save to db
        String fileUrl = directoryUrl + "/" + fileName;
        this.fileRepository.save(new File(fileUrl, fileName, contentType,bulkSaveOperationTimestamp, bulkSaveOperationUuid));
        return true;
    }

    @Override
    public BulkSaveOperationDto submitIngestionRequest(IngestionRequestDto ingestionRequestDto) throws IOException {
        System.out.println("Submitting web scraping request");
        Long bulkSaveOperationTimestamp = System.currentTimeMillis();
        String bulkSaveOperationUuid = UUID.randomUUID().toString();
        IngestionRequestPayload webscrapingRequestPayload = new IngestionRequestPayload(
                ingestionRequestDto,
                bulkSaveOperationTimestamp,
                bulkSaveOperationUuid
        );
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonPayload = ow.writeValueAsString(webscrapingRequestPayload);
        if(webscrapingRequestPayload.getUrl().startsWith("ftp://")) {
            this.ftpExplorerProducer.produce(jsonPayload);
        } else {
            this.webScrapingProducer.produce(jsonPayload);
        }
        return new BulkSaveOperationDto(bulkSaveOperationTimestamp,bulkSaveOperationUuid);
    }

    @Override
    public boolean downloadAndSaveFile(FileFoundPayload fileFoundPayload) throws IOException {
        // download
        URL url = new URL(fileFoundPayload.getUrl());
        String uuid = UUID.randomUUID().toString();
        java.io.File temporaryfile = java.io.File.createTempFile(String.format("indexini-pfe-java-app-%s",uuid), "");
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryfile);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        // save to HDFS & DB
        this.saveFile(new FileInputStream(temporaryfile), fileFoundPayload.getName(), Files.probeContentType(temporaryfile.toPath()),
                fileFoundPayload.getBulkSaveOperationTimestamp(),fileFoundPayload.getBulkSaveOperationUuid());
        // delete downloaded file from fs
        temporaryfile.delete();
        NotificationPayload notificationPayload = new NotificationPayload(Constants.FILE_DOWNLOADED, fileFoundPayload.getUrl(),fileFoundPayload.getName());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonPayload = ow.writeValueAsString(notificationPayload);
        this.notificationProducer.produce(jsonPayload);
        return true;
    }

    @Override
    public InputStream readFile(String url) throws IOException {
        return this.fileHdfsClient.readFile(url);
    }

    @Override
    public void deleteByBulkSaveOperationTimestamp(String timestamp) throws IOException {
        String url = this.saveDirectory + "/" + timestamp + "/";
        this.fileHdfsClient.delete(url);
        this.fileRepository.removeByBulkSaveOperationTimestamp(timestamp);
        FileDeletePayload fileDeletePayload = new FileDeletePayload("bulkSaveOperationTimestamp",timestamp);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonPayload = ow.writeValueAsString(fileDeletePayload);
        this.fileDeleteProducer.produce(jsonPayload);
    }

    @Override
    public void deleteByUrl(String url) throws IOException {
        this.fileHdfsClient.delete(url);
        this.fileRepository.removeByUrl(url);
        FileDeletePayload fileDeletePayload = new FileDeletePayload("id",url);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonPayload = ow.writeValueAsString(fileDeletePayload);
        this.fileDeleteProducer.produce(jsonPayload);
    }


}
