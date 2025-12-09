package com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConversationSaveDTO {
    @Size(max = 255)
    private String message;
    @NotBlank @Size(max = 64)
    private String activityId;

}
