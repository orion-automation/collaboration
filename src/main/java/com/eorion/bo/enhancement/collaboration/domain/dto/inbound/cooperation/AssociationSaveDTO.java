package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation;

import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceAssociationType;
import lombok.Data;

@Data
public class AssociationSaveDTO {
    private String name;
    private String url;
    private ResourceAssociationType type;
}
