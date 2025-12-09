package com.eorion.bo.enhancement.collaboration.exception;

import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ResourceDetailConflictDTO;
import lombok.Getter;

@Getter
public class ResourceConflictException extends Exception {

    private final ResourceDetailConflictDTO conflict;

    public ResourceConflictException(ResourceDetailConflictDTO conflict) {
        this.conflict = conflict;
    }

}
