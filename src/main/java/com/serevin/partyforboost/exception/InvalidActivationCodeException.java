package com.serevin.partyforboost.exception;

import java.util.HashMap;

import static com.serevin.partyforboost.utils.ErrorCodes.INVALID_ACTIVATION_CODE_ERROR;

public class InvalidActivationCodeException extends ApiException {

    public static final int ERROR_CODE = 400;

    public InvalidActivationCodeException(String message) {
        super(message, message, INVALID_ACTIVATION_CODE_ERROR, ERROR_CODE, new HashMap<>());
    }

    @Override
    public String getCode() {
        return INVALID_ACTIVATION_CODE_ERROR;
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
