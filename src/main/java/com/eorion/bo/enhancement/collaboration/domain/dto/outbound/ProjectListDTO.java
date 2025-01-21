package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import lombok.Data;

import java.util.List;

@Data
public class ProjectListDTO {
    List<MemberDetailDTO> members;
    private Integer id;
    private String name;
    private String coeCode;
    private String tags;
    private String owner;
    private String tenant;
    private Long createdTs;
    private Long updatedTs;
    private String createdBy;
    private String updatedBy;
}
