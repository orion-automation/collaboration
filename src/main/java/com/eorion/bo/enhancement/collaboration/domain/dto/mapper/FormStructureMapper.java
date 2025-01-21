package com.eorion.bo.enhancement.collaboration.domain.dto.mapper;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form.FormSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form.FormUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.CollaborationForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FormStructureMapper {
    CollaborationForm saveDtoToEntity(FormSaveDTO dto);
    CollaborationForm updateDtoToEntity(FormUpdateDTO dto);
}
