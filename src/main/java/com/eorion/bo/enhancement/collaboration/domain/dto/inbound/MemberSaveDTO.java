package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import com.eorion.bo.enhancement.collaboration.domain.enums.RoleStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberSaveDTO {
    @NotNull(message = "projectId不能为空")
    @Min(0)
    private Integer projectId;
    @NotBlank(message = "userId不能为空")
    @Size(max = 256)
    private String userId;
    @Size(max = 50)
    private String name;
    private RoleStatus role;
}
