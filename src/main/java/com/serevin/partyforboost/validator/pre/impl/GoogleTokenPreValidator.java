package com.serevin.partyforboost.validator.pre.impl;

import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.model.TokenHandlerType;
import com.serevin.partyforboost.validator.pre.AbstractTokenPreValidator;
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
