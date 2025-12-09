package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FormUpdateDTO {
    @NotBlank
    @Size(max = 64)
    private String name;
    @Size(max = 255)
    private String definitionKey;
    @Size(max = 16)
    private String type;
    @Size(max = 20)
    private String updatedBy;
    private List<Map<String, Object>> formData;

    public String getFormData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (this.formData != null && !this.formData.isEmpty()) {
                return objectMapper.writeValueAsString(this.formData);
            } else {
                return "";
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
