package com.eorion.bo.enhancement.collaboration.domain.dto.mapper;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.MemberSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.MemberDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberStructureMapper {
    Member saveDtoToEntity(MemberSaveDTO dto);
    MemberDetailDTO toMemberDetailDTO(Member member);
}
