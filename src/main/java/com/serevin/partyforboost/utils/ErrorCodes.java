package com.serevin.partyforboost.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorCodes {

    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String INVALID_REFRESH_TOKEN_ERROR = "invalidRefreshToken";
    public static final String EXPIRED_REFRESH_TOKEN_ERROR = "expiredRefreshTokenError";
    public static final String EXCEEDED_ATTEMPTS_ERROR = "exceededAttempts";
    public static final String USER_ALREADY_EXISTS_ERROR = "userAlreadyExists";
    public static final String INVALID_ACTIVATION_CODE_ERROR = "invalidActivationCode";
    public static final String ENTITY_NOT_FOUND_ERROR = "entityNotFound";
    public static final String INVALID_RESET_PASSWORD_CODE_ERROR = "invalidResetPasswordCodeException";
    public static final String INVALID_PASSWORD_OR_EMAIL_ERROR = "invalidPasswordOrEmailException";
}
