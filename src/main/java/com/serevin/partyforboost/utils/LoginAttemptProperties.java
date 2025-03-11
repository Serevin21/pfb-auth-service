package com.serevin.partyforboost.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoginAttemptProperties {

    public static final int MAX_FAILED_ENTERING_ATTEMPTS = 3;
    public static final long LOCK_TIME_IN_MINUTES = 5L;

}
