package com.serevin.partyforboost.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;

import static com.serevin.partyforboost.utils.ErrorCodes.EXCEEDED_ATTEMPTS_ERROR;


public class ExceededAttemptsException extends ApiException {

    private final LocalDateTime retryAfter;

    public ExceededAttemptsException(String message, LocalDateTime retryAfter) {
        super(
                message,
                message,
                EXCEEDED_ATTEMPTS_ERROR,
                HttpStatus.TOO_MANY_REQUESTS.value(),
                new HashMap<>()
        );
        this.retryAfter = retryAfter;
    }

    @Override
    public String getCode() {
        return EXCEEDED_ATTEMPTS_ERROR;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLogMessage() {
        return logMessage + retryAfter;
    }

    @Override
    public int getErrorCode() {
        return HttpStatus.TOO_MANY_REQUESTS.value();
    }

    public LocalDateTime getRetryAfter() {
        return retryAfter;  // Optionally return the retryAfter value
    }
}
