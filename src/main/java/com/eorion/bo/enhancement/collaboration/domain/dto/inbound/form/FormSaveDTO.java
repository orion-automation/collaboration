package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class FormSaveDTO {
    private String name;
    private String definitionKey;
    private String type;
    private String tenant;
    private String createdBy;

    private List<Map<String, Object>> formData;

    public String getFormData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (!this.formData.isEmpty()) {
                return objectMapper.writeValueAsString(this.formData);
            } else {
                return "";
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
