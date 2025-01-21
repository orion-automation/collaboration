package com.eorion.bo.enhancement.collaboration.domain.dto.mapper;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProjectSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProjectUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ProjectListDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectStructureMapper {
    Project saveDtoToEntity(ProjectSaveDTO projectSaveDTO);
    Project updateDtoToEntity(ProjectUpdateDTO projectUpdateDTO);
    ProjectListDTO toDto(Project project);
}
