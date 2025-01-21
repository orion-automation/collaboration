package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import com.eorion.bo.enhancement.collaboration.domain.entity.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Map;

@Data
public class ProjectDetailDTO {
    private Integer id;

    private String name;

    private String coeCode;

    private String tags;

    private String owner;

    private String tenant;

    private Long createdTs;

    private Long updatedTs;

    private String createdBy;

    private String updatedBy;

    private Map<String, Object> configJson;

    public static ProjectDetailDTO fromEntity(Project project){
        ProjectDetailDTO detail = new ProjectDetailDTO();
        if (project != null) {
            detail.setId(project.getId());
            detail.setName(project.getName());
            detail.setCoeCode(project.getCoeCode());
            detail.setTags(project.getTags());
            detail.setOwner(project.getOwner());
            detail.setTenant(project.getTenant());
            detail.setCreatedTs(project.getCreatedTs());
            detail.setUpdatedTs(project.getUpdatedTs());
            detail.setCreatedBy(project.getCreatedBy());
            detail.setUpdatedBy(project.getUpdatedBy());
            if (StringUtils.hasLength(project.getConfigJson())){
                ObjectMapper mapper = new ObjectMapper();
                try {
                    var map = mapper.readValue(project.getConfigJson(), Map.class);
                    detail.setConfigJson(map);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return detail;
    }

}
