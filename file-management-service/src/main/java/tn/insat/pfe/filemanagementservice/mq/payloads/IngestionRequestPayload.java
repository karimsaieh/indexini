package tn.insat.pfe.filemanagementservice.mq.payloads;

import tn.insat.pfe.filemanagementservice.dtos.IngestionRequestDto;

import java.util.List;

public class IngestionRequestPayload  extends  IngestionRequestDto{

    private Long bulkSaveOperationTimestamp;
    private String bulkSaveOperationUuid;
    private String source;

    public IngestionRequestPayload(IngestionRequestDto ingestionRequestDto, Long bulkSaveOperationTimestamp, String bulkSaveOperationUuid, String source) {
        super(ingestionRequestDto.getUrl(),ingestionRequestDto.getDepth(),ingestionRequestDto.getFileTypes());
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
