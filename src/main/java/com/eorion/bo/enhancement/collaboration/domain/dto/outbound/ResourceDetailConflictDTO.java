package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDetailConflictDTO {
    private Long updatedTs;

    private String updatedBy;
}
