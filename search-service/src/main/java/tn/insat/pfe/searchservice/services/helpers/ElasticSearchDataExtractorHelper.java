package tn.insat.pfe.searchservice.services.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.range.ParsedDateRange;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.FileGetDto;
import tn.insat.pfe.searchservice.dtos.LdaTopicsDescriptionGetDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticSearchDataExtractorHelper {

    private ElasticSearchDataExtractorHelper() {

    }

    public static FileGetDto getResponseToFileGetDto(GetResponse getResponse) {
        FileGetDto fileGetDto = new ObjectMapper().convertValue(getResponse.getSourceAsMap(), FileGetDto.class);
        fileGetDto.setText("text highlighting has not been requested");
        return fileGetDto;
    }

    public static List<FileGetDto> searchHitsToGetFileDtoList( SearchHit[] searchHits,String highlightField) {
        List<FileGetDto> fileGetDtosList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            float  score = hit.getScore();
            sourceAsMap.put("score", score);
            FileGetDto fileGetDto = new ObjectMapper().convertValue(sourceAsMap, FileGetDto.class);
            fileGetDto.setText("text highlighting has not been requested");
            if(highlightField != null) {
                List<String> highlightList = new ArrayList<>();
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get(highlightField);
                Text[] fragments = highlight.fragments();
                StringBuilder highlightedTextBuilder = new StringBuilder();
                for (int i = 0; i < fragments.length; i++) {
                    highlightedTextBuilder.append(fragments[i]);
                    if (i != fragments.length -1 ) {
                        highlightedTextBuilder.append("\n");
                    }
                    highlightList.add(fragments[i].toString());
                }
                fileGetDto.setText(highlightedTextBuilder.toString());
                fileGetDto.setText(null);
                fileGetDto.setHighlightList(highlightList);
            }
            fileGetDtosList.add(fileGetDto);
        }
        return fileGetDtosList;
    }

    public static Map<String, String> searchResponseToSuggestionsList(SearchResponse searchResponse) {
        Map<String, String> suggestionsMap = new HashMap <>();
        Suggest suggest = searchResponse.getSuggest();
        if(suggest != null) {
            PhraseSuggestion phraseSuggestion = suggest.getSuggestion("suggested_text");
            for (PhraseSuggestion.Entry entry : phraseSuggestion.getEntries()) {
                if(entry.getOptions().size()>0) {
                    suggestionsMap.put(entry.getOptions().get(0).getText().string(), entry.getOptions().get(0).getHighlighted().string());
                }
//            for (PhraseSuggestion.Entry.Option option : entry) {
//                suggestionsMap.put(option.getText().string(), option.getHighlighted().string());
//            }
            }
        }
        return suggestionsMap;
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
        List<FileGetDto> fileGetDtosList = searchHitsToGetFileDtoList(hits.getHits(), null);
        long totalHits = hits.getTotalHits();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        return new PageImpl<>(fileGetDtosList, PageRequest.of(page,size),totalHits);
    }

    public static List<Map<String, String>> searchResponseToSearchAsYouTypeListOfMap(SearchResponse searchResponse, String field) {
        List<Map<String, String>> result = new ArrayList<>();

        for (SearchHit hit: searchResponse.getHits().getHits()) {
            Map<String, HighlightField> highlightFieldsMap =  hit.getHighlightFields();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Map<String, String> map = new HashMap<>();
            map.put("highlight",highlightFieldsMap.get(field).fragments()[0].toString());
            map.put("value", (String)sourceAsMap.get(field));
            result.add(map);
        }
        return result;
    }

    public static List<Map<String, Object>> searchResponseTohHistogramByRange(SearchResponse searchResponse) {
        List<Map<String, Object>> result = new ArrayList<>();
        ParsedDateRange parsedDateRange = searchResponse.getAggregations().get("date_range");
        ParsedDateHistogram parsedDateHistogram = parsedDateRange.getBuckets().get(0).getAggregations().get("date_histogram");
        for(Histogram.Bucket bucket: parsedDateHistogram.getBuckets()) {
            Map<String, Object> map = new HashMap<>();
            map.put("x", bucket.getKeyAsString());
            map.put("y", bucket.getDocCount());
            result.add(map);
        }
        return result;
    }

    public static Page<Map<String, String>>  searchResponseToRawHistoryPageListOfMap(SearchResponse searchResponse, Pageable pageable) {
        List<Map<String, String>> result = new ArrayList<>();

        for (SearchHit hit: searchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Map<String, String> map = new HashMap<>();
            map.put("history", (String)sourceAsMap.get("history"));
            map.put("date", (String)sourceAsMap.get("date"));
            result.add(map);
        }
        long totalHits = searchResponse.getHits().getTotalHits();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        return new PageImpl<>(result, PageRequest.of(page,size),totalHits);
    }

}
