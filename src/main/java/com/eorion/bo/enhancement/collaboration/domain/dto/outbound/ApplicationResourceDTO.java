package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import com.eorion.bo.enhancement.collaboration.domain.entity.ApplicationResource;
import com.eorion.bo.enhancement.collaboration.domain.enums.ApplicationResStatus;
import com.eorion.bo.enhancement.collaboration.domain.enums.ApplicationResourceType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Data
public class ApplicationResourceDTO {

    private static final ObjectMapper mapper = new ObjectMapper();

    private Integer id;

    private Integer applicationId;

    private String name;

    private ApplicationResourceType type;

    private String icon;

    private Integer parentNode;

    private String externalResourceId;

    private String linkedResourceId;

    private String editGroup;

    private ApplicationResStatus status;

    private String definitionKey;

    private String definitionId;

    private String configJson;

    private Long createdTs;

    private Long updatedTs;

    private String createdBy;

    private String updatedBy;

    public static ApplicationResourceDTO formApplicationResource(ApplicationResource applicationResource){
        ApplicationResourceDTO applicationResourceDTO = new ApplicationResourceDTO();

        applicationResourceDTO.setId(applicationResource.getId());
        applicationResourceDTO.setApplicationId(applicationResource.getApplicationId());
        applicationResourceDTO.setIcon(applicationResource.getIcon());
        applicationResourceDTO.setName(applicationResource.getName());
        applicationResourceDTO.setType(applicationResource.getType());
        applicationResourceDTO.setParentNode(applicationResource.getParentNode());
        applicationResourceDTO.setDefinitionId(applicationResource.getDefinitionId());
        applicationResourceDTO.setDefinitionKey(applicationResource.getDefinitionKey());
        applicationResourceDTO.setCreatedBy(applicationResource.getCreatedBy());
        applicationResourceDTO.setUpdatedBy(applicationResource.getUpdatedBy());
        applicationResourceDTO.setCreatedTs(applicationResource.getCreatedTs());
        applicationResourceDTO.setUpdatedTs(applicationResource.getUpdatedTs());
        applicationResourceDTO.setLinkedResourceId(applicationResource.getLinkedResourceId());
        applicationResourceDTO.setExternalResourceId(applicationResource.getExternalResourceId());
        applicationResourceDTO.setEditGroup(applicationResource.getEditGroup());
        applicationResourceDTO.setStatus(applicationResource.getStatus());
        applicationResourceDTO.setConfigJson(applicationResource.getConfigJson());
        return applicationResourceDTO;
    }

    public Object getConfigJson(){
        if (StringUtils.hasLength(this.configJson)) {
            try {
                return mapper.readValue(this.configJson, Map.class);
            } catch (JsonProcessingException e) {
                try {
                    return mapper.readValue(this.configJson, List.class);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return null;
    }
}
