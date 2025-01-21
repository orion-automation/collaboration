package com.eorion.bo.enhancement.collaboration.domain.dto.mapper;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CoopPublicResourceDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CoopResourceDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceDetailStructureMapper {
    ResourceDetail saveDtoToEntity(ResourceDetailSaveDTO dto);
    CoopResourceDetailDTO entityToDto(ResourceDetail entity);
    CoopPublicResourceDetailDTO entityToPublicDto(ResourceDetail entity);
}
