package tn.insat.pfe.filemanagementservice.repositories;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import tn.insat.pfe.filemanagementservice.entities.File;
import tn.insat.pfe.filemanagementservice.entities.QFile;

import java.util.List;


public interface IFileRepository extends MongoRepository<File, Long>,    QuerydslPredicateExecutor<File>
        , QuerydslBinderCustomizer<QFile> {

    @Override
    default void customize(QuerydslBindings bindings, QFile file) {
        bindings.bind(file.name).first((path, value) -> path.containsIgnoreCase(value));
    }

    Long removeByLocation(String location);
    List<Long> removeByBulkSaveOperationTimestamp(Long bulkSaveOperationTimestamp);
    File findByLocation(String location);

    Long countByIsIndexed(boolean isIndexed);

}
