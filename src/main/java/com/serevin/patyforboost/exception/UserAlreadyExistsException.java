package com.serevin.patyforboost.exception;

import static com.serevin.patyforboost.utils.ErrorCodes.USER_ALREADY_EXISTS_ERROR;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException(String message) {
        super(message);
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
        return 409;
    }
}
