package tn.insat.pfe.filemanagementservice.endpoints.rest;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.dtos.BulkSaveOperationDto;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.IngestionRequestDto;
import tn.insat.pfe.filemanagementservice.endpoints.exceptions.ResourceNotFoundException;
import tn.insat.pfe.filemanagementservice.entities.File;
import tn.insat.pfe.filemanagementservice.services.IFileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/v1/files")
@CrossOrigin(origins = "http://localhost:9527")
public class FileEndpoint {
    private final IFileService fileService;

    @Autowired
    public FileEndpoint(IFileService fileService) {
        this.fileService = fileService;
    }

    // size=X&page=Y&sort=bulkSaveOperationTimestamp,desc
    @GetMapping
    public Page<FileGetDto> findAll(@QuerydslPredicate(root = File.class) Predicate predicate, Pageable pageable) {
        return this.fileService.findAll(predicate, pageable);
    }

    // get file from DB
    @GetMapping("/{id:^[0-9]+$}")
    public FileGetDto findById(@PathVariable Long id) {
        return this.fileService.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    //
    @GetMapping("/func/readFile")
    public void readFile(@RequestParam(name = "url") String url, HttpServletResponse response) throws IOException {
        InputStream is = this.fileService.readFile(url);
        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }

    @PostMapping
    public BulkSaveOperationDto saveMultipartFiles(@RequestParam(value = "multipartFiles") MultipartFile[] multipartFiles) throws IOException {
        return this.fileService.saveMultipartFiles(multipartFiles);
    }

    @PostMapping("/func/submitIngestionRequest")
    public BulkSaveOperationDto submitIngestionRequest(@RequestBody @Validated IngestionRequestDto ingestionRequestDto) throws IOException {
        return this.fileService.submitIngestionRequest(ingestionRequestDto);
    }

    @DeleteMapping(params = "url")
    public boolean deleteFileByUrl(@RequestParam(name = "url", required = false) String url) throws IOException {
        this.fileService.deleteByLocation(url);
        return true; // "nothing is true everything is permitted", ezio auditore da firenze
    }

    @DeleteMapping("/deletes")
    public boolean deleteMultipleFilesByUrl(@RequestBody String[] urls) throws IOException {
        this.fileService.deleteMultipleFilesByLocation(urls);
        return true; // "nothing is true everything is permitted", ezio auditore da firenze
    }

    @DeleteMapping(params = "bulkSaveOperationTimestamp")
    public boolean deleteFileBybulkSaveOperationTimestamp(
            @RequestParam(name = "bulkSaveOperationTimestamp", required = false) String bulkSaveOperationTimestamp) throws IOException {
        this.fileService.deleteByBulkSaveOperationTimestamp(bulkSaveOperationTimestamp);
        return true; // "nothing is true everything is permitted", ezio auditore da firenze
    }

    @GetMapping("/func/indexingStats")
    public Map<String, Long> indexingStats() {
        return this.fileService.indexingStats();
    }
}
