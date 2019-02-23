package tn.insat.pfe.searchservice.dtos;

import org.springframework.data.domain.Page;

import java.util.List;

public class SearchDto {
    private Page<FileGetDto> fileGetDtosPage;
    private List<String> suggestionsList;
    private float maxScore;

    public SearchDto(Page<FileGetDto> fileGetDtosPage, List<String> suggestionsList, float maxScore) {
        this.fileGetDtosPage = fileGetDtosPage;
        this.suggestionsList = suggestionsList;
        this.maxScore = maxScore;
    }

    public Page<FileGetDto> getFileGetDtosPage() {
        return fileGetDtosPage;
    }

    public void setFileGetDtosPage(Page<FileGetDto> fileGetDtosPage) {
        this.fileGetDtosPage = fileGetDtosPage;
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
}
