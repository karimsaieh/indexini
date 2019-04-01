package tn.insat.pfe.filemanagementservice.mq.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileFoundPayload {
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("bulkSaveOperationTimestamp")
    private Long bulkSaveOperationTimestamp;
    @JsonProperty("bulkSaveOperationUuid")
    private String bulkSaveOperationUuid;
    @JsonProperty("metadata")
    private List<String> metadata;

    public FileFoundPayload() {
    }

    public FileFoundPayload(String name, String url, Long bulkSaveOperationTimestamp, String bulkSaveOperationUuid, List<String> metadata) {
        this.name = name;
        this.url = url;
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
        this.metadata = metadata;
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

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "FileFoundPayload{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", bulkSaveOperationTimestamp=" + bulkSaveOperationTimestamp +
                ", bulkSaveOperationUuid='" + bulkSaveOperationUuid + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
