package tn.insat.pfe.filemanagementservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.insat.pfe.filemanagementservice.entities.File;

import java.util.Optional;

public interface IFileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
    //query dsl
}
