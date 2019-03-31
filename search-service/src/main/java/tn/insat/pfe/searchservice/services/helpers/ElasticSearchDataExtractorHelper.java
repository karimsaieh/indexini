package tn.insat.pfe.searchservice.services.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.FileGetDto;
import tn.insat.pfe.searchservice.dtos.LdaTopicsDescriptionGetDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElasticSearchDataExtractorHelper {
    public static List<FileGetDto> searchHitsToGetFileDtoList( SearchHit[] searchHits) {
        List<FileGetDto> fileGetDtosList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            float  score = hit.getScore();
            sourceAsMap.put("score", score);
            fileGetDtosList.add(new ObjectMapper().convertValue(sourceAsMap, FileGetDto.class));
        }
        return fileGetDtosList;
    }

    public static List<String> searchResponseToSuggestionsList(SearchResponse searchResponse) {
        List<String> suggestionsList = new ArrayList<>();
        Suggest suggest = searchResponse.getSuggest();
        TermSuggestion termSuggestion = suggest.getSuggestion("suggest_text");
        for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
            for (TermSuggestion.Entry.Option option : entry) {
                suggestionsList.add(option.getText().string());
            }
        }
        return suggestionsList;
    }

    public static List<LdaTopicsDescriptionGetDto> searchResponseToLdaTopicsDescriptionGetDtosList(SearchResponse searchResponse) {
        List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList = new ArrayList<>();
        for (SearchHit hit: searchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            ldaTopicsDescriptionGetDtosList.add(new ObjectMapper().convertValue(sourceAsMap, LdaTopicsDescriptionGetDto.class));
        }
        return ldaTopicsDescriptionGetDtosList;
    }

    public static Page<FileGetDto> searchResponseToFileGetDtoPage(SearchResponse searchResponse, Pageable pageable) {
        SearchHits hits = searchResponse.getHits();
        List<FileGetDto> fileGetDtosList = searchHitsToGetFileDtoList(hits.getHits());
        long totalHits = hits.getTotalHits();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        return new PageImpl<>(fileGetDtosList, PageRequest.of(page,size),totalHits);
    }

}
