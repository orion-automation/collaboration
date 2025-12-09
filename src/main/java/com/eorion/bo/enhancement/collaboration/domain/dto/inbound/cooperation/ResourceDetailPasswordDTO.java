package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResourceDetailPasswordDTO {
    @NotBlank
    private String password;
}
