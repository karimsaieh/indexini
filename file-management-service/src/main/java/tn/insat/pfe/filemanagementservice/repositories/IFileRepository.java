package tn.insat.pfe.filemanagementservice.repositories;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import tn.insat.pfe.filemanagementservice.entities.File;

import java.util.List;
import java.util.Optional;

//public interface IFileRepository extends PagingAndSortingRepository<File, Long> {
////    Optional<File> findByName(String name);
//    //query dsl
//    Long removeByLocation(String location);
//    List<Long> removeByBulkSaveOperationTimestamp(Long bulkSaveOperationTimestamp);
//    File findByLocation(String location);
//}


public interface IFileRepository extends MongoRepository<File, Long> {
    //    Optional<File> findByName(String name);
    //query dsl
    Long removeByLocation(String location);
    List<Long> removeByBulkSaveOperationTimestamp(Long bulkSaveOperationTimestamp);
    File findByLocation(String location);
}
