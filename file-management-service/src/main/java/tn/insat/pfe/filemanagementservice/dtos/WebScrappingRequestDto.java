package tn.insat.pfe.filemanagementservice.dtos;

public class WebScrappingRequestDto {
    private String pageUrl;
    private boolean entireWebsite;
    private String[] fileTypes;

    public WebScrappingRequestDto(String pageUrl, boolean entireWebsite, String[] fileTypes) {
        this.pageUrl = pageUrl;
        this.entireWebsite = entireWebsite;
        this.fileTypes = fileTypes;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public boolean isEntireWebsite() {
        return entireWebsite;
    }

    public void setEntireWebsite(boolean entireWebsite) {
        this.entireWebsite = entireWebsite;
    }

    public String[] getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(String[] fileTypes) {
        this.fileTypes = fileTypes;
    }
}
