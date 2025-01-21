package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import com.eorion.bo.enhancement.collaboration.domain.enums.RoleStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberSaveDTO {
    @NotNull(message = "projectId不能为空")
    private Integer projectId;
    @NotBlank(message = "userId不能为空")
    private String userId;
    private String name;
    private RoleStatus role;
}
