package com.serevin.patyforboost.validator.pre.impl;

import com.serevin.patyforboost.dto.token.TokenRequest;
import com.serevin.patyforboost.entity.User;
import com.serevin.patyforboost.model.TokenHandlerType;
import com.serevin.patyforboost.validator.pre.AbstractTokenPreValidator;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenPreValidator extends AbstractTokenPreValidator {

    @Override
    protected void preValidateImpl(User user, TokenRequest tokenRequest) {

    }

    @Override
    public TokenHandlerType getTokenHandlerType() {
        return TokenHandlerType.GOOGLE;
    }
}
