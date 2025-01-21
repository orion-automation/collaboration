package com.eorion.bo.enhancement.collaboration.domain.dto.mapper;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ResourceSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ResourceUpdatedDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Resource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceStructureMapper {
    Resource saveDtoToEntity(ResourceSaveDTO dto);
    Resource updateDtoToEntity(ResourceUpdatedDTO dto);
}
