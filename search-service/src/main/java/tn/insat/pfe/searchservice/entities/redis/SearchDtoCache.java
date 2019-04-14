package tn.insat.pfe.searchservice.entities.redis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisHash;
import tn.insat.pfe.searchservice.dtos.FileGetDto;
import tn.insat.pfe.searchservice.dtos.LdaTopicsDescriptionGetDto;
import tn.insat.pfe.searchservice.dtos.SearchDto;

import java.util.List;

//got to do this cause i can't deerialize a "Page"
@RedisHash(value = "SearchDtoCache", timeToLive = 60*60*24)
public class SearchDtoCache {
    private String id;
    private List<FileGetDto> fileGetDtosList;
    private int page;
    private int size;
    private long totalHits;
    private List<String> suggestionsList;
    private float maxScore;
    private List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList;

    public SearchDtoCache() {
    }

    public SearchDtoCache(String id, List<FileGetDto> fileGetDtosList, int page, int size, long totalHits, List<String> suggestionsList, float maxScore, List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList) {
        this.id = id;
        this.fileGetDtosList = fileGetDtosList;
        this.page = page;
        this.size = size;
        this.totalHits = totalHits;
        this.suggestionsList = suggestionsList;
        this.maxScore = maxScore;
        this.ldaTopicsDescriptionGetDtosList = ldaTopicsDescriptionGetDtosList;
    }

    @JsonIgnore
    public SearchDto getSearchDto() {
        Page<FileGetDto> fileGetDtosPage = new PageImpl<>(this.fileGetDtosList, PageRequest.of(this.page,this.size),totalHits);
        return  new SearchDto(fileGetDtosPage, suggestionsList, maxScore, ldaTopicsDescriptionGetDtosList);
    }

    public List<FileGetDto> getFileGetDtosList() {
        return fileGetDtosList;
    }

    public void setFileGetDtosList(List<FileGetDto> fileGetDtosList) {
        this.fileGetDtosList = fileGetDtosList;
    }

    public long getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public List<String> getSuggestionsList() {
        return suggestionsList;
    }

    public void setSuggestionsList(List<String> suggestionsList) {
        this.suggestionsList = suggestionsList;
    }

    public float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    public List<LdaTopicsDescriptionGetDto> getLdaTopicsDescriptionGetDtosList() {
        return ldaTopicsDescriptionGetDtosList;
    }

    public void setLdaTopicsDescriptionGetDtoList(List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList) {
        this.ldaTopicsDescriptionGetDtosList = ldaTopicsDescriptionGetDtosList;
    }
}
