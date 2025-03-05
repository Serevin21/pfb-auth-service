package com.serevin.partyforboost.exchanger.impl;

import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.exchanger.AbstractTokenExchanger;
import com.serevin.partyforboost.model.TokenHandlerType;
import com.serevin.partyforboost.service.UserService;
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
