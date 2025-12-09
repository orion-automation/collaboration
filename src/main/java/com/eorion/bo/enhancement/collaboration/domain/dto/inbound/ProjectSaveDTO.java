package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import com.eorion.bo.enhancement.collaboration.domain.enums.ProjectType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class ProjectSaveDTO {
    @NotBlank(message = "项目名称不能为空！")
    @Size(max = 256)
    private String name;
    @NotBlank(message = "coeCode 不能为空！")
    @Size(max = 128)
    private String coeCode;
    @Size(max = 128)
    private String tags;
    @NotBlank(message = "tenant 不能为空！")
    @Size(max = 64)
    private String tenant;

    @NotNull(message = "type 不能为空！")
    private ProjectType type;

    private Map<String, Object> configJson;

    public String getConfigJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (configJson != null && !configJson.isEmpty())
                return mapper.writeValueAsString(configJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

}
