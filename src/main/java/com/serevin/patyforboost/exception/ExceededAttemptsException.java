package com.serevin.patyforboost.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.serevin.patyforboost.utils.ErrorCodes.EXCEEDED_ATTEMPTS;
import static com.serevin.patyforboost.utils.ErrorCodes.TRY_AGAIN_AT;

public class ExceededAttemptsException extends ApiException {

    public ExceededAttemptsException(String message, LocalDateTime localDateTime) {
        super(message, "%s. Try again at: %s".formatted(message, localDateTime),
                Map.of(TRY_AGAIN_AT, localDateTime.format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    @Override
    public String getCode() {
        return EXCEEDED_ATTEMPTS;
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
        return 409;
    }
}
