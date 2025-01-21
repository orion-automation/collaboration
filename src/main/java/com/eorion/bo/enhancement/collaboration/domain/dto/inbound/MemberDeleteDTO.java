package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import lombok.Data;

@Data
public class MemberDeleteDTO {
    private Integer projectId;
    private String userId;
}
