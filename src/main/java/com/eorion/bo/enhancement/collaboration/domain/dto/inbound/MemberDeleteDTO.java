package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberDeleteDTO {
    @Min(0)
    private Integer projectId;
    @Size(max = 50)
    private String userId;
}
