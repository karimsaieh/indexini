package tn.insat.pfe.searchservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.insat.pfe.searchservice.clients.IElasticSearchClient;
import tn.insat.pfe.searchservice.dtos.*;
import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;
import tn.insat.pfe.searchservice.mq.payloads.LdaTopicsDescriptionPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService implements ISearchService {
    @Value("${elasticsearch.index.file}")
    private String fileIndex;
    @Value("${elasticsearch.index.file.type}")
    private String fileIndexType;
    @Value("${elasticsearch.index.file.index-field}")
    private String fileIndexField;
    @Value("${elasticsearch.index.lda-topics}")
    private String ldaTopicsIndex;
    @Value("${elasticsearch.index.lda-topics.type}")
    private String ldaTopicsIndexType;
    private IElasticSearchClient elasticSearchClient;
    @Autowired
    public SearchService(IElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }
    @Override
    public SearchDto find(String query, Pageable pageable) {

        List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList = new ArrayList<>();
        SearchResponse topicsSearchResponse = this.elasticSearchClient.findAll(this.ldaTopicsIndex);

        SearchResponse searchResponse = this.elasticSearchClient.search(this.fileIndex, this.fileIndexField, query, pageable);
        List<FileGetDto> fileGetDtosList = new ArrayList<>();
        List<String> suggestionsList = new ArrayList<>();
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            float  score = hit.getScore();
            sourceAsMap.put("score", score);
            fileGetDtosList.add(new ObjectMapper().convertValue(sourceAsMap, FileGetDto.class));
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

        for (SearchHit hit: topicsSearchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            ldaTopicsDescriptionGetDtosList.add(new ObjectMapper().convertValue(sourceAsMap, LdaTopicsDescriptionGetDto.class));
        }

        Page<FileGetDto> fileGetDtosPage = new PageImpl<>(fileGetDtosList, PageRequest.of(page,size),totalHits);
        return new SearchDto(fileGetDtosPage, suggestionsList, maxScore, ldaTopicsDescriptionGetDtosList);
    }

//    @Override
//    public boolean save(FileSaveDto fileSaveDTo) {
//        return this.elasticSearchClient.save(fileSaveDTo);
//    }
//
//    @Override
//    public boolean deleteByBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp) {
//        return this.elasticSearchClient.deleteByBulkSaveOperationTimestamp(bulkSaveOperationTimestamp);
//    }
//
//    @Override
//    public boolean deleteByBulkSaveOperationUuid(String bulkSaveOperationUuid) {
//        return this.elasticSearchClient.deleteByBulkSaveOperationUuid(bulkSaveOperationUuid);
//    }

    @Override
    public boolean upsertFileIndex(FileIndexPayload fileIndexPayload) {
        Map fileIndexPayloadMap = fileIndexPayload.toMap();
        return this.elasticSearchClient.upsert(this.fileIndex, this.fileIndexType, fileIndexPayloadMap);
    }

    @Override
    public boolean upsertLdaTopicsDescription(List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList) {
        for (LdaTopicsDescriptionPayload topic: ldaTopicsDescriptionPayloadList) {
            Map topicMap = topic.toMap();
            this.elasticSearchClient.upsert(this.ldaTopicsIndex, this.ldaTopicsIndexType, topicMap);
        }
        this.elasticSearchClient.deleteByRangeFrom(this.ldaTopicsIndex, "id", ldaTopicsDescriptionPayloadList.size());
        return true; // ?
    }

//    @Override
//    public boolean update(String id, FileUpdateDto fileUpdateDto) {
//        return this.elasticSearchClient.update(id, fileUpdateDto);
//    }
}
