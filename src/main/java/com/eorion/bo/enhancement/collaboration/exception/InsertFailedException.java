package com.eorion.bo.enhancement.collaboration.exception;

public class InsertFailedException extends Exception{
    public InsertFailedException() {
    }

    public InsertFailedException(String message) {
        super(message);
    }

    public InsertFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsertFailedException(Throwable cause) {
        super(cause);
    }

    public InsertFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
