package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation;

import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceAssociationType;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AssociationSaveDTO {
    @Size(max = 255)
    private String name;
    @Size(max = 255)
    private String url;
    private ResourceAssociationType type;
}
