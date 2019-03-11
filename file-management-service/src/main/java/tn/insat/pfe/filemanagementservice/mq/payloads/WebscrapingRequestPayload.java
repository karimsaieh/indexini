package tn.insat.pfe.filemanagementservice.mq.payloads;

import tn.insat.pfe.filemanagementservice.dtos.WebScrapingRequestDto;

public class WebscrapingRequestPayload {
    private String pageUrl;
    private int depth;
    private String[] fileTypes;
    private String bulkSaveOperationTimestamp;
    private String bulkSaveOperationUuid;

    public WebscrapingRequestPayload(WebScrapingRequestDto webScrapingRequestDto, String bulkSaveOperationTimestamp, String bulkSaveOperationUuid) {
        this.pageUrl = webScrapingRequestDto.getPageUrl();
        this.depth = webScrapingRequestDto.getDepth();
        this.fileTypes = webScrapingRequestDto.getFileTypes();
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
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

    public String getBulkSaveOperationTimestamp() {
        return bulkSaveOperationTimestamp;
    }

    public void setBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp) {
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
    }

    public String getBulkSaveOperationUuid() {
        return bulkSaveOperationUuid;
    }

    public void setBulkSaveOperationUuid(String bulkSaveOperationUuid) {
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
    }
}
