package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessDevTimeSaveDTO {
    @NotBlank(message = "资源详情ID不能为空！")
    private String id;

    @NotBlank(message = "类型不能为空！")
    private String type;
}
