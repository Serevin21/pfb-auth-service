package com.serevin.partyforboost.exception;

import static com.serevin.partyforboost.utils.ErrorCodes.INVALID_CREDENTIALS_ERROR;

public class BadCredentialsException extends ApiException {

    public BadCredentialsException(String message) {
        super(message, message, INVALID_CREDENTIALS_ERROR, 401, null);
    }

    @Override
    public String getCode() {
        return code;
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
        return errorCode;
    }
}
