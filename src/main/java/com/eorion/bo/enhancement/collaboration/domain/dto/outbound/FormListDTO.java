package com.eorion.bo.enhancement.collaboration.domain.dto.outbound;

import lombok.Data;

@Data
public class FormListDTO {

    private String id;

    private String name;

    private String definitionKey;

    private String tenant;

    private String createdBy;

    private String updatedBy;

    private Long createdAt;

    private Long updatedAt;

}
