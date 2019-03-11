package tn.insat.pfe.filemanagementservice.endpoints.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.dtos.BulkSaveOperationDto;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.WebScrapingRequestDto;
import tn.insat.pfe.filemanagementservice.endpoints.exceptions.ResourceNotFoundException;
import tn.insat.pfe.filemanagementservice.services.IFileService;

import java.io.IOException;

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

    @PostMapping
    public BulkSaveOperationDto saveMultipartFiles(@RequestParam(value = "multipartFiles") MultipartFile[] multipartFiles) throws IOException {
        return this.fileService.saveMultipartFiles(multipartFiles);
    }

    @PostMapping("/func/submitWebScrapingRequest")
    public BulkSaveOperationDto submitWebScrapingRequest(@RequestBody @Validated WebScrapingRequestDto webScrapingRequestDto) throws IOException {
        return this.fileService.submitWebScrapingRequest(webScrapingRequestDto);
    }
}
