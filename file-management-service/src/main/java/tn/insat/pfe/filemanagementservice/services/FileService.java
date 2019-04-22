package tn.insat.pfe.filemanagementservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import tn.insat.pfe.filemanagementservice.mq.payloads.*;
import tn.insat.pfe.filemanagementservice.mq.producers.IRabbitProducer;
import tn.insat.pfe.filemanagementservice.repositories.IFileRepository;
import tn.insat.pfe.filemanagementservice.services.utils.FileServiceUtils;
import tn.insat.pfe.filemanagementservice.utils.JsonUtils;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@Transactional
public class FileService implements IFileService{
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private final IFileRepository fileRepository;
    private final FileMapper fileMapper;
    private final IFileHdfsClient fileHdfsClient;
    private final IRabbitProducer webScrapingProducer;
    private final IRabbitProducer ftpExplorerProducer;
    private final IRabbitProducer notificationProducer;
    private final IRabbitProducer fileDeleteProducer;

    @Value("${pfe_hdfs_save-directory}")
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
    @HystrixCommand(fallbackMethod = "fallback")
    public Page<FileGetDto> findAll(Predicate predicate, Pageable pageable) {
        Page<File> page = null;
        if(predicate == null) {
            page = this.fileRepository.findAll(pageable);
        } else {
            page = this.fileRepository.findAll(predicate, pageable);
        }
        List<FileGetDto> fileGetDtoList = page
                .getContent()
                .stream()
                .map(this.fileMapper::fileToFileGetDto)
                .collect(Collectors.toList());
        return new PageImpl<>(fileGetDtoList, pageable, page.getTotalElements());
    }

    public Page<FileGetDto> fallback(Predicate predicate, Pageable pageable) {
        return new PageImpl<>(new ArrayList<FileGetDto>(), pageable, 0);
    }

    @Override
    public BulkSaveOperationDto saveMultipartFiles(MultipartFile[] multipartFiles) throws IOException {
        String source = "ui-upload";
        Long bulkSaveOperationTimestamp = System.currentTimeMillis();
        String bulkSaveOperationUuid = UUID.randomUUID().toString();
        //
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile multipartFile: multipartFiles ) {
            fileNames.add(multipartFile.getOriginalFilename());
        }
        fileNames = FileServiceUtils.renameFiles(fileNames);
        //
        for (int i = 0; i < multipartFiles.length ; i++) {
            this.saveFile(multipartFiles[i].getInputStream(), fileNames.get(i),multipartFiles[i].getContentType()
                    ,bulkSaveOperationTimestamp, bulkSaveOperationUuid, source);
        }
        return new BulkSaveOperationDto(bulkSaveOperationTimestamp,bulkSaveOperationUuid);
    }

    @Override
    public void updateFile(FileDbUpdatePayload fileDbUpdatePayload) {
        File file = this.fileRepository.findByLocation(fileDbUpdatePayload.getLocation());
        file.setIndexed(fileDbUpdatePayload.isIndexed()); //always true btw
        this.fileRepository.save(file);
    }

    public boolean saveFile(InputStream inputStream, String fileName,String contentType, Long bulkSaveOperationTimestamp,
                            String bulkSaveOperationUuid, String source) throws IOException {
        String directoryUrl = this.saveDirectory + "/" + bulkSaveOperationTimestamp + "/" + bulkSaveOperationUuid;
        // save to hdfs
        this.fileHdfsClient.addFile(directoryUrl, fileName, inputStream);
        //save to db
        String fileLocation = directoryUrl + "/" + fileName;
        this.fileRepository.save(new File(fileLocation, fileName, contentType,bulkSaveOperationTimestamp,
                bulkSaveOperationUuid, source));
        return true;
    }

    @Override
    public BulkSaveOperationDto submitIngestionRequest(IngestionRequestDto ingestionRequestDto) throws IOException {
        logger.error("Submitting ingestion request");
        Long bulkSaveOperationTimestamp = System.currentTimeMillis();
        String bulkSaveOperationUuid = UUID.randomUUID().toString();
        String source = ingestionRequestDto.getUrl();
        IngestionRequestPayload ingestionRequestPayload = new IngestionRequestPayload(
                ingestionRequestDto,
                bulkSaveOperationTimestamp,
                bulkSaveOperationUuid,
                source
        );
        String jsonPayload = JsonUtils.objectToJsonString(ingestionRequestPayload);
        if(ingestionRequestPayload.getUrl().startsWith("ftp://")) {
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
        logger.info("opening channel");
        java.io.File temporaryFile =null;
        try(ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream())) {
            logger.info("channel opened");
             temporaryFile =  java.io.File.createTempFile(String.format("indexini-pfe-java-app-%s",uuid), "");
            try(FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)) {
                // downloading file
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                // save to HDFS & DB
                try(FileInputStream fileInputStream = new FileInputStream(temporaryFile)) {
                    this.saveFile(fileInputStream, fileFoundPayload.getName(), Files.probeContentType(temporaryFile.toPath()),
                            fileFoundPayload.getBulkSaveOperationTimestamp(),fileFoundPayload.getBulkSaveOperationUuid(),
                            fileFoundPayload.getSource());
                }
            }
        }
        Files.delete(temporaryFile.toPath());
        String logMsg = "temp file " + temporaryFile.getName() + "deleted";
        logger.info(logMsg);

        NotificationPayload notificationPayload = new NotificationPayload(Constants.FILE_DOWNLOADED, fileFoundPayload.getUrl(),fileFoundPayload.getName());
        String jsonPayload = JsonUtils.objectToJsonString(notificationPayload);
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
        this.fileRepository.removeByBulkSaveOperationTimestamp(Long.parseLong(timestamp));
        FileDeletePayload fileDeletePayload = new FileDeletePayload("bulkSaveOperationTimestamp",timestamp);
        String jsonPayload = JsonUtils.objectToJsonString(fileDeletePayload);
        this.fileDeleteProducer.produce(jsonPayload);
    }

    @Override
    public void deleteByLocation(String location) throws IOException {
        this.fileHdfsClient.delete(location);
        this.fileRepository.removeByLocation(location);
        FileDeletePayload fileDeletePayload = new FileDeletePayload("id",location);
        String jsonPayload = JsonUtils.objectToJsonString(fileDeletePayload);
        this.fileDeleteProducer.produce(jsonPayload);
    }

    @Override
    public void deleteMultipleFilesByLocation(String[] locations) throws IOException {
        // this will generate multiple ElasticSearch Request
        // would rather leverage the Elasticsearch bulk api but i got no time
        for(String location: locations) {
            this.deleteByLocation(location);
        }
    }


    @Override
    public Map<String , Long> indexingStats(){
        Map<String, Long> stats  = new HashMap<>();
        stats.put("notIndexed", this.fileRepository.countByIsIndexed(false));
        stats.put("files", this.fileRepository.count());
        return stats;
    }

}
