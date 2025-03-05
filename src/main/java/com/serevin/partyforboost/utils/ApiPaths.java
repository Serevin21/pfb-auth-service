package com.serevin.partyforboost.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPaths {
    private static final String API_BASE = "/api/v1";

    public static final String SIGN_UP = API_BASE + "/sign-up";
    public static final String TOKEN = API_BASE + "/token";
    public static final String REFRESH_TOKEN = API_BASE + "/refresh-token";
    public static final String TOKEN_SIGN_OUT = API_BASE + "/sign-out";
    public static final String EMAIL_ACTIVATION_SEND_EMAIL = API_BASE + "/activation/sendEmail";
    public static final String EMAIL_ACTIVATION_ACTIVATE = API_BASE + "/activation/activate";
    public static final String RESET_PASSWORD_SEND_EMAIL = API_BASE + "/sendEmail";
    public static final String RESET_PASSWORD_VALIDATE_CODE = API_BASE + "/validateCode";
    public static final String RESET_PASSWORD_CHANGE = API_BASE + "/change";
}
