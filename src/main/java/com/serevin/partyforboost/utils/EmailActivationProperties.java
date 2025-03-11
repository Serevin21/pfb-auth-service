package com.serevin.partyforboost.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailActivationProperties {

    public static final int MAX_ACTIVATION_EMAIL_ATTEMPTS = 3;
    public static final int MAX_FAILED_CODE_ENTERING_ATTEMPTS = 3;
    public static final long ACTIVATION_EMAIL_COOLDOWN_MINUTES = 5L;
    public static final long INVALID_CODE_COOLDOWN_MINUTES = 5L;

}
