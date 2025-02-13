package com.serevin.patyforboost.exception;

public class EntityNotFoundException extends ApiException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "entityNotFound";
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLogMessage() {
        return message;
    }

    @Override
    public int getErrorCode() {
        return 404;
    }
}
