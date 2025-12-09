package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class CoopResourceDetailDTO {

    private String id;

    private Integer resourceId;

    private String name;

    private String xml;

    private Integer version;

    private Long createdTs;

    private Long updatedTs;

    private String createdBy;

    private String updatedBy;

    private Map<String, Object> configJson;

    public static CoopResourceDetailDTO formDetail(ResourceDetail detail) {
        CoopResourceDetailDTO resourceDetailDTO = new CoopResourceDetailDTO();
        resourceDetailDTO.setId(detail.getId());
        resourceDetailDTO.setName(detail.getName());
        resourceDetailDTO.setResourceId(detail.getResourceId());
        resourceDetailDTO.setXml(detail.getXml());
        resourceDetailDTO.setConfigJson(detail.getConfigJson());
        resourceDetailDTO.setVersion(detail.getVersion());
        resourceDetailDTO.setCreatedTs(detail.getCreatedTs());
        resourceDetailDTO.setUpdatedTs(detail.getUpdatedTs());
        resourceDetailDTO.setCreatedBy(detail.getCreatedBy());
        resourceDetailDTO.setCreatedBy(detail.getCreatedBy());
        return resourceDetailDTO;
    }

    public Map<String, Object> getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (StringUtils.isNotEmpty(configJson)) {
                this.configJson = mapper.readValue(configJson, Map.class);
            } else {
                this.configJson = null;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
