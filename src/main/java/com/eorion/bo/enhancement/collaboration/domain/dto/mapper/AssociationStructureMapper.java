package com.eorion.bo.enhancement.collaboration.domain.dto.mapper;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.AssociationSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Association;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssociationStructureMapper {
    Association saveDtoToEntity(AssociationSaveDTO dto);
}
