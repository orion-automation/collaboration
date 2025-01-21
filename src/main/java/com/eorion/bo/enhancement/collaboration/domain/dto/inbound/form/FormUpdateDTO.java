package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FormUpdateDTO {
    private String name;
    private String definitionKey;
    private String type;
    private String updatedBy;

    private List<Map<String, Object>> formData;

    public String getFormData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this.formData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
