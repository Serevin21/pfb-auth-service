package com.serevin.partyforboost.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResetPasswordsProperties {

    public static final int MAX_RESET_PASSWORD_ATTEMPTS  = 3;
    public static final int MAX_FAILED_CODE_ENTERING_ATTEMPTS = 3;
    public static final long RESET_PASSWORD_CODE_EXPIRATION_MINUTES = 5L;
    public static final long FAILED_CODE_ENTERING_LOCK_DURATION_MINUTES = 5L;

}
