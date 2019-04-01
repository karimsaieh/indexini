package tn.insat.pfe.filemanagementservice.services;

import org.apache.commons.io.FilenameUtils;
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
import tn.insat.pfe.filemanagementservice.mq.payloads.*;
import tn.insat.pfe.filemanagementservice.mq.producers.IRabbitProducer;
import tn.insat.pfe.filemanagementservice.repositories.IFileRepository;
import tn.insat.pfe.filemanagementservice.services.Utils.FileServiceUtils;

import javax.transaction.Transactional;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.*;
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
        List<String> metadata=  new ArrayList<>(Arrays.asList("ui-upload"));
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
                    ,bulkSaveOperationTimestamp, bulkSaveOperationUuid, metadata);
        }
        return new BulkSaveOperationDto(bulkSaveOperationTimestamp,bulkSaveOperationUuid);
    }

    @Override
    public void updateFile(FileDbUpdatePayload fileDbUpdatePayload) {
        File file = this.fileRepository.findByLocation(fileDbUpdatePayload.getLocation());
        file.setIndexed(fileDbUpdatePayload.isIndexed()); //always true btw
//        this.fileRepository.save(file);
    }


    public boolean saveFile(InputStream inputStream, String fileName,String contentType, Long bulkSaveOperationTimestamp,
                            String bulkSaveOperationUuid, List<String> metadata) throws IOException {
        String directoryUrl = this.saveDirectory + "/" + bulkSaveOperationTimestamp + "/" + bulkSaveOperationUuid;
        // save to hdfs
        this.fileHdfsClient.addFile(directoryUrl, fileName, inputStream);
        //save to db
        String fileLocation = directoryUrl + "/" + fileName;
        this.fileRepository.save(new File(fileLocation, fileName, contentType,bulkSaveOperationTimestamp,
                bulkSaveOperationUuid, metadata));
        return true;
    }

    @Override
    public BulkSaveOperationDto submitIngestionRequest(IngestionRequestDto ingestionRequestDto) throws IOException {
        System.out.println("Submitting web scraping request");
        Long bulkSaveOperationTimestamp = System.currentTimeMillis();
        String bulkSaveOperationUuid = UUID.randomUUID().toString();
        List<String> metadata = new ArrayList<>(Arrays.asList("from url", ingestionRequestDto.getUrl()));
        IngestionRequestPayload ingestionRequestPayload = new IngestionRequestPayload(
                ingestionRequestDto,
                bulkSaveOperationTimestamp,
                bulkSaveOperationUuid,
                metadata
        );
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonPayload = ow.writeValueAsString(ingestionRequestPayload);
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
        System.out.println("krr");
        URL url = new URL(fileFoundPayload.getUrl());
        String uuid = UUID.randomUUID().toString();
        System.out.println("krrsdmlfk");
        java.io.File temporaryfile = java.io.File.createTempFile(String.format("indexini-pfe-java-app-%s",uuid), "");
        System.out.println("krrcwxwxcwxsdmlfk");
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        System.out.println("krrsdmxclfk");
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryfile);
        System.out.println("krrpoipp");
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        // save to HDFS & DB
        System.out.println("ttk");
        FileInputStream fileInputStream = new FileInputStream(temporaryfile);
        this.saveFile(fileInputStream, fileFoundPayload.getName(), Files.probeContentType(temporaryfile.toPath()),
                fileFoundPayload.getBulkSaveOperationTimestamp(),fileFoundPayload.getBulkSaveOperationUuid(),
                fileFoundPayload.getMetadata());
        // delete downloaded file from fs
        System.out.println("ssk");
        fileInputStream.close();
        fileOutputStream.close();
        temporaryfile.delete();
        System.out.println("kff");

        NotificationPayload notificationPayload = new NotificationPayload(Constants.FILE_DOWNLOADED, fileFoundPayload.getUrl(),fileFoundPayload.getName());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonPayload = ow.writeValueAsString(notificationPayload);
        this.notificationProducer.produce(jsonPayload);
        System.out.println("kdd");

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
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonPayload = ow.writeValueAsString(fileDeletePayload);
        this.fileDeleteProducer.produce(jsonPayload);
    }

    @Override
    public void deleteByLocation(String location) throws IOException {
        this.fileHdfsClient.delete(location);
        this.fileRepository.removeByLocation(location);
        FileDeletePayload fileDeletePayload = new FileDeletePayload("id",location);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonPayload = ow.writeValueAsString(fileDeletePayload);
        this.fileDeleteProducer.produce(jsonPayload);
    }


}
