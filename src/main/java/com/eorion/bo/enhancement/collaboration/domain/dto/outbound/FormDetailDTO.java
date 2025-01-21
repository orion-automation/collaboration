package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import com.eorion.bo.enhancement.collaboration.domain.entity.CollaborationForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class FormDetailDTO {

    private String name;

    private String type;

    private String definitionKey;

    private String tenant;

    private String createdBy;

    private String updatedBy;
    private Long createdTs;

    private Long updatedTs;

    private Object formData;

    public static FormDetailDTO getData(CollaborationForm collaborationForm) {
        FormDetailDTO formDetailDTO = new FormDetailDTO();
        formDetailDTO.setName(collaborationForm.getName());
        formDetailDTO.setType(collaborationForm.getType());
        formDetailDTO.setTenant(collaborationForm.getTenant());
        formDetailDTO.setDefinitionKey(collaborationForm.getDefinitionKey());
        formDetailDTO.setCreatedBy(collaborationForm.getCreatedBy());
        formDetailDTO.setCreatedTs(collaborationForm.getCreatedTs());
        formDetailDTO.setUpdatedTs(collaborationForm.getUpdatedTs());
        formDetailDTO.setUpdatedBy(collaborationForm.getUpdatedBy());
        formDetailDTO.setDefinitionKey(collaborationForm.getDefinitionKey());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (!collaborationForm.getFormData().isEmpty()) {
                Object formData = objectMapper.readValue(collaborationForm.getFormData(), Object.class);
                formDetailDTO.setFormData(formData);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return formDetailDTO;
    }

}
