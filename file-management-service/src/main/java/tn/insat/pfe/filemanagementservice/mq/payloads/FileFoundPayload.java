package tn.insat.pfe.filemanagementservice.mq.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileFoundPayload {
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("bulkSaveOperationTimestamp")
    private Long bulkSaveOperationTimestamp;
    @JsonProperty("bulkSaveOperationUuid")
    private String bulkSaveOperationUuid;

    public FileFoundPayload() {
    }

    public FileFoundPayload(String name, String url, Long bulkSaveOperationTimestamp, String bulkSaveOperationUuid) {
        this.name = name;
        this.url = url;
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public String toString() {
        return "FilePayload{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", bulkSaveOperationTimestamp='" + bulkSaveOperationTimestamp + '\'' +
                ", bulkSaveOperationUuid='" + bulkSaveOperationUuid + '\'' +
                '}';
    }
}
