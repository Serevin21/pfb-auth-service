package com.serevin.partyforboost.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "The password must be between 8 and 30 characters, contain at least 1 number and 1 capital letter";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
