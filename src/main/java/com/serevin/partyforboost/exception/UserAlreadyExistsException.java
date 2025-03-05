package com.serevin.partyforboost.exception;

import java.util.HashMap;

import static com.serevin.partyforboost.utils.ErrorCodes.USER_ALREADY_EXISTS_ERROR;

public class UserAlreadyExistsException extends ApiException {

    private static final int ERROR_CODE = 409;

    public UserAlreadyExistsException(String message) {
        super(message, message, USER_ALREADY_EXISTS_ERROR, ERROR_CODE, new HashMap<>());
    }

    @Override
    public String getCode() {
        return USER_ALREADY_EXISTS_ERROR;
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
