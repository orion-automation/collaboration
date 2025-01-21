package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class CoopPublicResourceDetailDTO {
    private String id;
    private Long resourceId;
    private String name;
    private String xml;
    private Integer version;

    private Map<String, Object> configJson;

    public void setConfigJson(String configJson) {
        if (StringUtils.isNotEmpty(configJson)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.configJson = objectMapper.readValue(configJson, new TypeReference<HashMap<String, Object>>() {
                });
            } catch (JsonProcessingException e) {
                log.error("convert error", e);
            }

        } else {
            this.configJson = null;
        }
    }
}
