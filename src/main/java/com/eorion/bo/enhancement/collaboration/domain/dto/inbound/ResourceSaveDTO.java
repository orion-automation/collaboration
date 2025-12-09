package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class ResourceSaveDTO {
    @Min(0)
    private int projectId;
    @NotBlank(message = "资源名称不能为空")
    @Size(max = 50)
    private String name;
    @NotNull(message = "type不能为null")
    private ResourceType type;
    @Min(0)
    private Integer parentNode;
    @Size(max = 255)
    private String externalResourceId;
    @Size(max = 255)
    private String tags;

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
