package com.serevin.partyforboost.exception;

import java.util.HashMap;

import static com.serevin.partyforboost.utils.ErrorCodes.ENTITY_NOT_FOUND_ERROR;

public class EntityNotFoundException extends ApiException {

    private static final int ERROR_CODE = 404;

    public EntityNotFoundException(String message) {
        super(message, message, ENTITY_NOT_FOUND_ERROR, ERROR_CODE, new HashMap<>());
    }

    @Override
    public String getCode() {
        return ENTITY_NOT_FOUND_ERROR;
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
        return ERROR_CODE;
    }
}
