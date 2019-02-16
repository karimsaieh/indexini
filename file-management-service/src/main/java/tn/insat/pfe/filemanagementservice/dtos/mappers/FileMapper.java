package tn.insat.pfe.filemanagementservice.dtos.mappers;

import org.mapstruct.Mapper;
import tn.insat.pfe.filemanagementservice.dtos.FileGetDto;
import tn.insat.pfe.filemanagementservice.dtos.FileSaveDto;
import tn.insat.pfe.filemanagementservice.dtos.FileUpdateDto;
import tn.insat.pfe.filemanagementservice.entities.File;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileGetDto fileToFileGetDto(File file);
    File fileSaveDtoToFile(FileSaveDto fileSaveDto);
    File fileUpdateDtoToFile(FileUpdateDto fileUpdateDto);

}
