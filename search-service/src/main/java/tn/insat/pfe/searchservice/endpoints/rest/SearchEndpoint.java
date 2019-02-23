package tn.insat.pfe.searchservice.endpoints.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import tn.insat.pfe.searchservice.dtos.FileSaveDto;
import tn.insat.pfe.searchservice.dtos.FileUpdateDto;
import tn.insat.pfe.searchservice.dtos.SearchDto;
import tn.insat.pfe.searchservice.services.ISearchService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/search")
//@CrossOrigin
public class SearchEndpoint {
    private final ISearchService searchService;
    @Autowired
    public SearchEndpoint(ISearchService searchService) {
        this.searchService = searchService;
    }
    @GetMapping
    public SearchDto find(@RequestParam String query, @RequestParam int page,  @RequestParam int size) {
;        return this.searchService.find(query, PageRequest.of(page, size));
    }
    @PostMapping
    public boolean save(@RequestBody @Valid FileSaveDto fileSaveDto) {
        return this.searchService.save(fileSaveDto);
    }
    @DeleteMapping("/{bulkOperationUuid}")
    public boolean deleteByBulkOperationUuid(@PathVariable String bulkOperationUuid) {
        return this.searchService.deleteByBulkOperationUuid(bulkOperationUuid);
    }
    @PutMapping("/{id}")
    public boolean update(@PathVariable String id, @RequestBody @Valid FileUpdateDto fileUpdateDto) {
        return this.searchService.update(id, fileUpdateDto);
    }
}
