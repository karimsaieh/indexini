package tn.insat.pfe.filemanagementservice.services;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.clients.IFileHdfsClient;
import tn.insat.pfe.filemanagementservice.dtos.BulkSaveOperationDto;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.WebScrapingRequestDto;
import tn.insat.pfe.filemanagementservice.dtos.mappers.FileMapper;
import tn.insat.pfe.filemanagementservice.entities.File;
import tn.insat.pfe.filemanagementservice.mq.payloads.FilePayload;
import tn.insat.pfe.filemanagementservice.mq.payloads.WebscrapingRequestPayload;
import tn.insat.pfe.filemanagementservice.mq.producers.IRabbitProducer;
import tn.insat.pfe.filemanagementservice.repositories.IFileRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FileService implements IFileService{
    private final IFileRepository fileRepository;
    private final FileMapper fileMapper;
    private final IFileHdfsClient fileHdfsClient;
    private final IRabbitProducer webScrapingProducer;
    private final IRabbitProducer notificationProducer;

    @Autowired
    public FileService(IFileRepository fileRepository, FileMapper fileMapper, IFileHdfsClient fileHdfsClient,
                       @Qualifier("webScrapingProducer") IRabbitProducer webScrapingProducer,
                       @Qualifier("NotificationProducer") IRabbitProducer notificationProducer) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.fileHdfsClient = fileHdfsClient;
        this.webScrapingProducer = webScrapingProducer;
        this.notificationProducer = notificationProducer;
    }

    @Override
    public Optional<FileGetDto> findById(Long id) {
        return this.fileRepository.findById(id).map(this.fileMapper::fileToFileGetDto);
    }

    @Override
    public BulkSaveOperationDto saveMultipartFiles(MultipartFile[] multipartFiles) throws IOException {
        String bulkSaveOperationTimestamp = Long.toString(System.currentTimeMillis());
        String bulkSaveOperationUuid = UUID.randomUUID().toString();
        for (MultipartFile multipartFile: multipartFiles) {
            this.saveFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),multipartFile.getContentType()
                    ,bulkSaveOperationTimestamp, bulkSaveOperationUuid);
        }
        return new BulkSaveOperationDto(bulkSaveOperationTimestamp,bulkSaveOperationUuid);
    }

    @Override
    public boolean saveFile(InputStream inputStream, String fileName,String contentType, String bulkSaveOperationTimestamp, String bulkSaveOperationUuid) throws IOException {
        // save to hdfs
        //TODO: not so important but file path should be created here btw
        this.fileHdfsClient.addFile(inputStream, fileName, bulkSaveOperationTimestamp, bulkSaveOperationUuid);
        //save to db
        this.fileRepository.save(new File(fileName, contentType,bulkSaveOperationTimestamp, bulkSaveOperationUuid));
        return true;
    }

    @Override
    public BulkSaveOperationDto submitWebScrapingRequest(WebScrapingRequestDto webScrapingRequestDto) throws IOException {
        System.out.println("Submitting web scraping request");
        String bulkSaveOperationTimestamp = Long.toString(System.currentTimeMillis());
        String bulkSaveOperationUuid = UUID.randomUUID().toString();
        WebscrapingRequestPayload webscrapingRequestPayload = new WebscrapingRequestPayload(
                webScrapingRequestDto,
                bulkSaveOperationTimestamp,
                bulkSaveOperationUuid
        );
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonPayload = ow.writeValueAsString(webscrapingRequestPayload);
        this.webScrapingProducer.produce(jsonPayload);
        return new BulkSaveOperationDto(bulkSaveOperationTimestamp,bulkSaveOperationUuid);
    }

    @Override
    public boolean downloadAndSaveFile(FilePayload filePayload) throws IOException {
        // download
        URL url = new URL(filePayload.getUrl());
        String uuid = UUID.randomUUID().toString();
        java.io.File temporaryfile = java.io.File.createTempFile(String.format("indexini-pfe-java-app-%s",uuid), "");
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryfile);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        // save to HDFS & DB
        this.saveFile(new FileInputStream(temporaryfile), filePayload.getName(), Files.probeContentType(temporaryfile.toPath()),
                filePayload.getBulkSaveOperationTimestamp(),filePayload.getBulkSaveOperationUuid());
        // delete downloaded file from fs
        temporaryfile.delete();
        String routingKey =filePayload.getBulkSaveOperationTimestamp() + "-" + filePayload.getBulkSaveOperationUuid();
        this.notificationProducer.produce(routingKey, "a notnification recieved");
        return true;
    }

    @Override
    public InputStream readFile(String url) throws IOException {
        return this.fileHdfsClient.readFile(url);
    }

}
