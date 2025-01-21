package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import lombok.Data;

@Data
public class ResourceDetailDTO {

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

    private String tags;


    private String detailLatestUpdatedBy;

    private Long detailLatestUpdatedTs;

    public void setType(String type) {
        this.type = ResourceType.from(type);
    }
}
