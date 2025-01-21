package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import com.eorion.bo.enhancement.collaboration.domain.entity.Resource;
import com.eorion.bo.enhancement.collaboration.domain.enums.CoopResourceStatus;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Map;

@Data
public class CooperationResourceDetailDTO {

    private Integer id;

    private Integer projectId;

    private String name;

    private ResourceType type;

    private Integer parentNode;

    private String externalResourceId;

    private Long createdTs;

    private Long updatedTs;

    private String createdBy;

    private String updatedBy;

    private CoopResourceStatus status;

    private String tags;

    private Map<String, Object> configJson;

    public static CooperationResourceDetailDTO fromEntity(Resource resource){
        CooperationResourceDetailDTO detail = new CooperationResourceDetailDTO();
        if (resource != null) {
            detail.setId(resource.getId());
            detail.setProjectId(resource.getProjectId());
            detail.setName(resource.getName());
            detail.setType(resource.getType());
            detail.setParentNode(resource.getParentNode());
            detail.setExternalResourceId(resource.getExternalResourceId());
            detail.setCreatedTs(resource.getCreatedTs());
            detail.setUpdatedTs(resource.getUpdatedTs());
            detail.setCreatedBy(resource.getCreatedBy());
            detail.setUpdatedBy(resource.getUpdatedBy());
            detail.setTags(resource.getTags());
            if (StringUtils.hasLength(resource.getConfigJson())){
                ObjectMapper mapper = new ObjectMapper();
                try {
                    var map = mapper.readValue(resource.getConfigJson(), Map.class);
                    detail.setConfigJson(map);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return detail;
    }

}
