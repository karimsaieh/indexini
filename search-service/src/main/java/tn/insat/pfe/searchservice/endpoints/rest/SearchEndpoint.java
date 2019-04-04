package tn.insat.pfe.searchservice.endpoints.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tn.insat.pfe.searchservice.dtos.*;
import tn.insat.pfe.searchservice.services.ISearchService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/search")
//@CrossOrigin
public class SearchEndpoint {
    private final ISearchService searchService;
    @Autowired
    public SearchEndpoint(ISearchService searchService) {
        this.searchService = searchService;
    }

    //use case query
    @GetMapping(params = "query")
    public SearchDto find(@RequestParam String query, @RequestParam int page,  @RequestParam int size) throws JsonProcessingException {
        return this.searchService.find(query, PageRequest.of(page, size));
    }
    // use cases: find by prediction
    @GetMapping(params = "by")
    public Page<FileGetDto> findBy(@RequestParam String by, @RequestParam String value, Pageable pageable) throws JsonProcessingException {
        return this.searchService.findBy(by, value, pageable);
    }

    //use case: get file info
    @GetMapping(params = "id")
    public FileGetDto findById(@RequestParam String id) throws JsonProcessingException {
        return this.searchService.findById(id);
    }

    //use case: get files sorted by a topic, sortBy=ldaTopics.4
    @GetMapping(params = "sortBy")
    public Page<FileGetDto> findAllSortBy(@RequestParam String sortBy, Pageable pageable) throws JsonProcessingException {
        return this.searchService.findAllSortBy(sortBy, pageable);
    }

    @GetMapping("/ldaTopics")
    public List<LdaTopicsDescriptionGetDto> getLdaTopics() throws JsonProcessingException {
        return this.searchService.getLdaTopics();
    }

//    @PostMapping
//    public boolean save(@RequestBody @Valid FileSaveDto fileSaveDto) {
//        return this.searchService.save(fileSaveDto);
//    }
//    @DeleteMapping("/{bulkSaveOperationUuid}")
//    public boolean deleteByBulkSaveOperationUuid(@PathVariable String bulkSaveOperationUuid) {
//        return this.searchService.deleteByBulkSaveOperationUuid(bulkSaveOperationUuid);
//    }
//    @PutMapping("/{id}")
//    public boolean update(@PathVariable String id, @RequestBody @Valid FileUpdateDto fileUpdateDto) {
//        return this.searchService.update(id, fileUpdateDto);
//    }
}
