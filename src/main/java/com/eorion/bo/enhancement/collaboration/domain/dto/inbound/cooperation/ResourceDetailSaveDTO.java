package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResourceDetailSaveDTO {
    @Size(max = 255)
    private String name;
    private String xml;

    private Map<String, Object> configJson;

    private List<@Size(max = 64) String> nodes;

    public String getConfigJson() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.configJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
