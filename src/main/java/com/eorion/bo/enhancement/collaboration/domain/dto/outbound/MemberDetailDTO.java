package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;


import com.eorion.bo.enhancement.collaboration.domain.enums.RoleStatus;
import lombok.Data;

@Data
public class MemberDetailDTO {
    private Integer id;

    private Integer projectId;

    private String userId;

    private String name;

    private RoleStatus role;

    private Long createdTs;

    private Long updatedTs;

    private String createdBy;

    private String updatedBy;

}
