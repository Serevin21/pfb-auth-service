package com.serevin.partyforboost.exception;

import java.util.HashMap;

import static com.serevin.partyforboost.utils.ErrorCodes.INVALID_PASSWORD_OR_EMAIL_ERROR;

public class InvalidPasswordOrEmailException extends ApiException{
    public static final int ERROR_CODE = 401;

    public InvalidPasswordOrEmailException(String message) {
        super(message, message, INVALID_PASSWORD_OR_EMAIL_ERROR, ERROR_CODE, new HashMap<>());
    }

    @Override
    public String getCode() {
        return INVALID_PASSWORD_OR_EMAIL_ERROR;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLogMessage() {
        return logMessage;
    }

    @Override
    public int getErrorCode() {
        return ERROR_CODE;
    }
}
