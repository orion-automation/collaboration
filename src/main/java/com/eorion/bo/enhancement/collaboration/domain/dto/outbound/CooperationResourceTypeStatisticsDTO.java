package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import lombok.Data;

@Data
public class CooperationResourceTypeStatisticsDTO {
    private ResourceType type;

    private Integer count;
}
