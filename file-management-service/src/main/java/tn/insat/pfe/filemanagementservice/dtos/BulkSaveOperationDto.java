package tn.insat.pfe.filemanagementservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkSaveOperationDto {

    @JsonProperty("bulkSaveOperationTimestamp")
    private String timestamp;
    @JsonProperty("bulkSaveOperationUuid")
    private String uuid;

    public BulkSaveOperationDto() {
    }

    public BulkSaveOperationDto(String timestamp, String uuid) {
        this.timestamp = timestamp;
        this.uuid = uuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
