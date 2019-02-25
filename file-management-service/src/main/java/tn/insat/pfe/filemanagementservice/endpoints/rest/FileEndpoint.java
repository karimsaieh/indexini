package tn.insat.pfe.filemanagementservice.endpoints.rest;


//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.clients.IFileHdfsClient;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.FileSaveDto;
import tn.insat.pfe.filemanagementservice.endpoints.exceptions.ResourceNotFoundException;
import tn.insat.pfe.filemanagementservice.services.IFileService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/v1/files")
//@CrossOrigin
public class FileEndpoint {
    private final IFileService fileService;
    @Autowired
    public FileEndpoint(IFileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{id:^[0-9]+$}")
    public FileGetDto findById(@PathVariable Long id) {
         return this.fileService.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    //TODO: change methods names
    @PostMapping
    public boolean saveMultipartFiles(@RequestParam(value = "multipartFiles") MultipartFile[] multipartFiles) throws IOException {
        return this.fileService.saveMultipartFiles(multipartFiles);
    }

    //TODO: mapping conflict ?
//    @PostMapping
//    public boolean submitWebScrappingRequest(String pageUrl, boolean entireWebsite, String[] fileTypes) throws IOException {
//        return this.fileService.submitWebScrappingRequest(pageUrl, entireWebsite, fileTypes);
//    }
    @GetMapping("/downloadfiletest")
    public boolean download() throws IOException {
        return this.fileService.downloadAndSaveFile("https://artifacts.elastic.co/downloads/elasticsearch-hadoop/elasticsearch-hadoop-6.6.0.zip");
    }
}
