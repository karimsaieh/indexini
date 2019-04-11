package tn.insat.pfe.filemanagementservice.dtos;

import tn.insat.pfe.filemanagementservice.validators.ValidFileTypes;

import javax.validation.constraints.Max;

public class IngestionRequestDto {
    private String url;
    @Max(3)
    private int depth;
    @ValidFileTypes
    private String[] fileTypes;

    public IngestionRequestDto() {
    }

    public IngestionRequestDto(String url, @Max(3) int depth, String[] fileTypes) {
        this.url = url;
        this.depth = depth;
        this.fileTypes = fileTypes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String[] getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(String[] fileTypes) {
        this.fileTypes = fileTypes;
    }
}
