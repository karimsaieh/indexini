package tn.insat.pfe.filemanagementservice.repositories;

import tn.insat.pfe.filemanagementservice.dtos.FileTypesGetDto;

import java.util.List;

public interface IFileMongoRepository {

    List<FileTypesGetDto> countFileTypes();

}
