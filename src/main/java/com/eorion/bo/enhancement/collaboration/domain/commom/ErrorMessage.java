package com.eorion.bo.enhancement.collaboration.domain.commom;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorMessage {
    private final int code;
    private final long timestamp = System.currentTimeMillis();
    private final String error;
    private final String message;

    public ErrorMessage(String message) {
        this(-1, null, message);
    }

    public ErrorMessage(int code, String message) {
        this(code, null, message);
    }
}
