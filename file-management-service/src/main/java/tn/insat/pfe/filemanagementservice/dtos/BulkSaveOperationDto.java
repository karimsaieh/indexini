package tn.insat.pfe.filemanagementservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkSaveOperationDto {

    @JsonProperty("bulkSaveOperationTimestamp")
    private Long timestamp;
    @JsonProperty("bulkSaveOperationUuid")
    private String uuid;

    public BulkSaveOperationDto() {
    }

    public BulkSaveOperationDto(Long timestamp, String uuid) {
        this.timestamp = timestamp;
        this.uuid = uuid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
