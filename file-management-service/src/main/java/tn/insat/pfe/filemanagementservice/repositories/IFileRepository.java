package tn.insat.pfe.filemanagementservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.insat.pfe.filemanagementservice.entities.File;
import java.util.List;


public interface IFileRepository extends MongoRepository<File, Long> {

    Long removeByLocation(String location);
    List<Long> removeByBulkSaveOperationTimestamp(Long bulkSaveOperationTimestamp);
    File findByLocation(String location);

}
