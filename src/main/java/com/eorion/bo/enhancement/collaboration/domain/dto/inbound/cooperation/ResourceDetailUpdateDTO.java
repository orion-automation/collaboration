package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation;

import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
public class ResourceDetailUpdateDTO {
    private String name;
    private String xml;

    private Map<String, Object> configJson;

    private List<String> nodes;

    private boolean forceSave;

    @NotNull(message = "currentVersionTimestamp not be empty !")
    private Long currentVersionTimestamp;

    public String getConfigJson() {
        if (this.configJson != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(this.configJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static ResourceDetail toEntity(ResourceDetailUpdateDTO updateDTO) {
        ResourceDetail detail = new ResourceDetail();
        detail.setName(updateDTO.getName());
        detail.setXml(updateDTO.getXml());
        if (StringUtils.isNotEmpty(updateDTO.getConfigJson()))
            detail.setConfigJson(updateDTO.getConfigJson());
        return detail;
    }
}
