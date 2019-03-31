package tn.insat.pfe.filemanagementservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import tn.insat.pfe.filemanagementservice.entities.File;

import java.util.List;
import java.util.Optional;

public interface IFileRepository extends PagingAndSortingRepository<File, Long> {
//    Optional<File> findByName(String name);
    //query dsl
    Long removeByUrl(String url);
    List<Long> removeByBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp);
}
