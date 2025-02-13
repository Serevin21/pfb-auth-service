package com.serevin.patyforboost.exchanger.impl;

import com.serevin.patyforboost.dto.token.TokenRequest;
import com.serevin.patyforboost.exchanger.AbstractTokenExchanger;
import com.serevin.patyforboost.model.TokenHandlerType;
import com.serevin.patyforboost.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class CredentialsTokenExchanger extends AbstractTokenExchanger {

    public CredentialsTokenExchanger(UserService userService) {
        super(userService);
    }

    @Override
    protected String getUserEmail(TokenRequest request) {
        return request.email();
    }

    @Override
    public TokenHandlerType getTokenHandlerType() {
        return TokenHandlerType.CREDENTIALS;
    }
}
