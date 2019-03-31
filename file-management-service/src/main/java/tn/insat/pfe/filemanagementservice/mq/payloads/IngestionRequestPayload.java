package tn.insat.pfe.filemanagementservice.mq.payloads;

import tn.insat.pfe.filemanagementservice.dtos.IngestionRequestDto;

public class IngestionRequestPayload {
    private String url;
    private int depth;
    private String[] fileTypes;
    private Long bulkSaveOperationTimestamp;
    private String bulkSaveOperationUuid;

public IngestionRequestPayload(IngestionRequestDto ingestionRequestDto, Long bulkSaveOperationTimestamp, String bulkSaveOperationUuid) {
        this.url = ingestionRequestDto.getUrl();
        this.depth = ingestionRequestDto.getDepth();
        this.fileTypes = ingestionRequestDto.getFileTypes();
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
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

    public Long getBulkSaveOperationTimestamp() {
        return bulkSaveOperationTimestamp;
    }

    public void setBulkSaveOperationTimestamp(Long bulkSaveOperationTimestamp) {
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
    }

    public String getBulkSaveOperationUuid() {
        return bulkSaveOperationUuid;
    }

    public void setBulkSaveOperationUuid(String bulkSaveOperationUuid) {
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
    }
}
