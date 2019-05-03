package tn.insat.pfe.searchservice.endpoints.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import tn.insat.pfe.searchservice.dtos.*;
import tn.insat.pfe.searchservice.services.ISearchService;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/search")
@CrossOrigin(origins = "http://localhost:9527")
public class SearchEndpoint {
    private final ISearchService searchService;
    @Autowired
    public SearchEndpoint(ISearchService searchService) {
        this.searchService = searchService;
    }

    //use case query
    @GetMapping(params = "query")
    public SearchDto find(@RequestParam String query, @RequestParam int page,  @RequestParam int size) throws IOException {
        return this.searchService.find(query, PageRequest.of(page, size));
    }
    // use cases: find by prediction
    @GetMapping(params = "by")
    public Page<FileGetDto> findBy(@RequestParam String by, @RequestParam String value,
                                   @RequestParam String must,
                                   @RequestParam String not,
                                   Pageable pageable) throws IOException {
        return this.searchService.findByMustNot(by, value,must, not, pageable);
    }

    //use case: get file info
    @GetMapping(params = "id")
    public FileGetDto findById(@RequestParam String id) throws IOException {
        return this.searchService.findById(id);
    }

    //use case: get files sorted by a topic, sortBy=ldaTopics.4
    @GetMapping(params = "sortBy")
    public Page<FileGetDto> findAllSortBy(@RequestParam String sortBy, Pageable pageable) throws IOException {
        return this.searchService.findAllSortBy(sortBy, pageable);
    }

    //use case search as you type from history
    @GetMapping(params = "searchAsYouType")
    public List<Map<String, String>> searchAsYouType(String searchAsYouType) throws IOException {
        return this.searchService.searchAsYouType(searchAsYouType);
    }

    @GetMapping("/ldaTopics")
    public List<LdaTopicsDescriptionGetDto> getLdaTopics() throws IOException {
        return this.searchService.getLdaTopics();
    }

    @GetMapping("/historyAgg")
    public List<Map<String, Object>> histogramByRange(@RequestParam String range) throws IOException {
        return this.searchService.histogramByRange(range);
    }

    @GetMapping("/countSearch")
    public long countSearch() throws IOException {
        return this.searchService.countSearch();
    }

    @GetMapping("/history")
    public Page<Map<String, String>> findAllHistory(Pageable pageable) throws IOException {
        return this.searchService.findAllRawHistory(pageable);
    }

}
