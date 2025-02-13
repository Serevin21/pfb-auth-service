package com.serevin.patyforboost.validator.pre;

import com.serevin.patyforboost.dto.token.TokenRequest;
import com.serevin.patyforboost.entity.User;
import jakarta.validation.ValidationException;

public abstract class AbstractTokenPreValidator implements TokenPreValidator {

    @Override
    public void preValidate(User user, TokenRequest tokenRequest) {

        if (!user.isEnabled()) {
            throw new ValidationException("User is disabled");
        }

        preValidateImpl(user, tokenRequest);
    }

    protected abstract void preValidateImpl(User user, TokenRequest tokenRequest);
}
