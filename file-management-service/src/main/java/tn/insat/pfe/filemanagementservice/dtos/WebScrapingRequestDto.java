package tn.insat.pfe.filemanagementservice.dtos;

import tn.insat.pfe.filemanagementservice.validators.ValidFileTypes;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

public class WebScrapingRequestDto {
    private String pageUrl;
    @Max(3)
    private int depth;
    @ValidFileTypes
    private String[] fileTypes;

    public WebScrapingRequestDto(String pageUrl, int depth, String[] fileTypes) {
        this.pageUrl = pageUrl;
        this.depth = depth;
        this.fileTypes = fileTypes;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
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
