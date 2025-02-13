package com.serevin.patyforboost.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPaths {
    private static final String API_BASE = "/api/v1";

    public static final String SIGN_UP = API_BASE + "/sign-up";
    public static final String TOKEN = API_BASE + "/token";
    public static final String REFRESH_TOKEN = API_BASE + "/refresh-token";
    public static final String TOKEN_SIGN_OUT = API_BASE + "/sign-out";
}
