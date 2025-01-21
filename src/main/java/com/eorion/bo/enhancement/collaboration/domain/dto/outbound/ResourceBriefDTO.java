package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import com.eorion.bo.enhancement.collaboration.domain.enums.ProjectType;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import lombok.Data;

@Data
public class ResourceBriefDTO {

    private Integer id;

    private Integer projectId;

    private String name;

    private ResourceType resourceType;

    private ProjectType projectType;
}
