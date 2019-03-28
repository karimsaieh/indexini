package tn.insat.pfe.filemanagementservice.endpoints.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.insat.pfe.filemanagementservice.dtos.BulkSaveOperationDto;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.IngestionRequestDto;
import tn.insat.pfe.filemanagementservice.endpoints.exceptions.ResourceNotFoundException;
import tn.insat.pfe.filemanagementservice.services.IFileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/v1/files")
//@CrossOrigin
public class FileEndpoint {
    private final IFileService fileService;
    @Autowired
    public FileEndpoint(IFileService fileService) {
        this.fileService = fileService;
    }

    // get file from DB
    @GetMapping("/{id:^[0-9]+$}")
    public FileGetDto findById(@PathVariable Long id) {
         return this.fileService.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    //
    @GetMapping("/func/downloadFile")
    public void donwloadFile(@RequestParam(name = "url") String url, HttpServletResponse response) {
        try {
            InputStream is = this.fileService.readFile(url);
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("FileEndpoint.donwloadFile IOError writing file to output stream");
        }

    }

    @PostMapping
    public BulkSaveOperationDto saveMultipartFiles(@RequestParam(value = "multipartFiles") MultipartFile[] multipartFiles) throws IOException {
        return this.fileService.saveMultipartFiles(multipartFiles);
    }

    @PostMapping("/func/submitIngestionRequest")
    public BulkSaveOperationDto submitWebScrapingRequest(@RequestBody @Validated IngestionRequestDto ingestionRequestDto) throws IOException {
        return this.fileService.submitIngestionRequest(ingestionRequestDto);
    }
}
