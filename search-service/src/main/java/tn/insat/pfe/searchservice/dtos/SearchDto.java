package tn.insat.pfe.searchservice.dtos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public class SearchDto {
    private Page<FileGetDto> fileGetDtosPage;
    private Map<String, String> suggestionsList;
    private float maxScore;
    private List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList;

    public SearchDto() {
    }

    public SearchDto(Page<FileGetDto> fileGetDtosPage, Map<String, String> suggestionsList, float maxScore, List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList) {
        this.fileGetDtosPage = fileGetDtosPage;
        this.suggestionsList = suggestionsList;
        this.maxScore = maxScore;
        this.ldaTopicsDescriptionGetDtosList = ldaTopicsDescriptionGetDtosList;
    }

    public Page<FileGetDto> getFileGetDtosPage() {
        return fileGetDtosPage;
    }

    public void setFileGetDtosPage(Page<FileGetDto> fileGetDtosPage) {
        this.fileGetDtosPage = fileGetDtosPage;
    }

    public Map<String, String> getSuggestionsList() {
        return suggestionsList;
    }

    public void setSuggestionsList(Map<String, String> suggestionsList) {
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

    public void setLdaTopicsDescriptionGetDtosList(List<LdaTopicsDescriptionGetDto> ldaTopicsDescriptionGetDtosList) {
        this.ldaTopicsDescriptionGetDtosList = ldaTopicsDescriptionGetDtosList;
    }
}
