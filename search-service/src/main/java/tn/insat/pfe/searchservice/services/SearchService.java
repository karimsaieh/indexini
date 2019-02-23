package tn.insat.pfe.searchservice.services;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.insat.pfe.searchservice.clients.IElasticSearchClient;
import tn.insat.pfe.searchservice.dtos.FileGetDto;
import tn.insat.pfe.searchservice.dtos.FileSaveDto;
import tn.insat.pfe.searchservice.dtos.FileUpdateDto;
import tn.insat.pfe.searchservice.dtos.SearchDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService implements ISearchService {
    private IElasticSearchClient elasticSearchClient;
    @Autowired
    public SearchService(IElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }
    @Override
    public SearchDto find(String query, Pageable pageable) {
        //defaults: text, suggest_text
        SearchResponse searchResponse = this.elasticSearchClient.find(query, pageable);
        List<FileGetDto> fileGetDtosList = new ArrayList<>();
        List<String> suggestionsList = new ArrayList<>();
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String id = hit.getId();
            String bulkSaveOperationUuid = (String) sourceAsMap.get("bulkSaveOperationUuid");
            String text = (String) sourceAsMap.get("text");
            String summary = (String) sourceAsMap.get("summary");
            String thumbnailUrl = (String) sourceAsMap.get("thumbnailUrl");
            String bulkSaveOperationTimestamp = (String) sourceAsMap.get("bulkSaveOperationTimestamp");
            String fileName = (String) sourceAsMap.get("fileName");
            float  score = hit.getScore();
            fileGetDtosList.add(new FileGetDto(id, bulkSaveOperationUuid, text, summary, thumbnailUrl, bulkSaveOperationTimestamp, fileName, score));
        }
        Suggest suggest = searchResponse.getSuggest();
        TermSuggestion termSuggestion = suggest.getSuggestion("suggest_text");
        for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
            for (TermSuggestion.Entry.Option option : entry) {
                suggestionsList.add(option.getText().string());
            }
        }
        float maxScore = hits.getMaxScore();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        Page<FileGetDto> fileGetDtosPage = new PageImpl<>(fileGetDtosList, PageRequest.of(page,size),totalHits);
        return new SearchDto(fileGetDtosPage, suggestionsList, maxScore);
    }

    @Override
    public boolean save(FileSaveDto fileSaveDTo) {
        return this.elasticSearchClient.save(fileSaveDTo);
    }
//
//    @Override
//    public boolean deleteByBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp) {
//        return this.elasticSearchClient.deleteByBulkSaveOperationTimestamp(bulkSaveOperationTimestamp);
//    }
//
    @Override
    public boolean deleteByBulkSaveOperationUuid(String bulkSaveOperationUuid) {
        return this.elasticSearchClient.deleteByBulkSaveOperationUuid(bulkSaveOperationUuid);
    }

    @Override
    public boolean update(String id, FileUpdateDto fileUpdateDto) {
        return this.elasticSearchClient.update(id, fileUpdateDto);
    }
}
