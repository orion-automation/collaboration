package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageDTO {
    @Size(max = 255)
    private String message;
}
