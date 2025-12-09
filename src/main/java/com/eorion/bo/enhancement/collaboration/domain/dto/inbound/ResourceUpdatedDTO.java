package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class ResourceUpdatedDTO {
    @Size(max = 50)
    private String name;
    @Min(0)
    private Integer parentNode;
    @Size(max = 255)
    private String externalResourceId;
    @Min(0)
    private Integer projectId;
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
