package com.serevin.patyforboost.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorCodes {

    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String INVALID_REFRESH_TOKEN_ERROR = "invalidRefreshToken";
    public static final String EXPIRED_REFRESH_TOKEN_ERROR = "expiredRefreshTokenError";
    public static final String TRY_AGAIN_AT = "try_again_at";
    public static final String EXCEEDED_ATTEMPTS = "exceededAttempts";
    public static final String USER_ALREADY_EXISTS_ERROR = "userAlreadyExists";

}
