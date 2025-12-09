package com.eorion.bo.enhancement.collaboration.domain.dto.inbound;

import com.eorion.bo.enhancement.collaboration.domain.enums.ProjectType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class ProjectUpdateDTO {
    @Size(max = 256)
    private String name;
    @Size(max = 128)
    private String tags;

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
