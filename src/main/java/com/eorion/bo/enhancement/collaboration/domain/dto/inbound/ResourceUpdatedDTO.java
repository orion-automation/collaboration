package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Map;

@Data
public class ResourceUpdatedDTO {
    private String name;

    private String parentNode;

    private String externalResourceId;

    private Integer projectId;

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
