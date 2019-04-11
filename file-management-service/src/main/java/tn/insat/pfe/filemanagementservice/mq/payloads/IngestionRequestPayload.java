package tn.insat.pfe.filemanagementservice.mq.payloads;

import tn.insat.pfe.filemanagementservice.dtos.IngestionRequestDto;

import java.util.List;

public class IngestionRequestPayload  extends  IngestionRequestDto{

    private Long bulkSaveOperationTimestamp;
    private String bulkSaveOperationUuid;
    private List<String> metadata;

    public IngestionRequestPayload(IngestionRequestDto ingestionRequestDto, Long bulkSaveOperationTimestamp, String bulkSaveOperationUuid, List<String> metadata) {
        super(ingestionRequestDto.getUrl(),ingestionRequestDto.getDepth(),ingestionRequestDto.getFileTypes());
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
        this.metadata = metadata;
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
}
