package com.serevin.partyforboost.exchanger.impl;

import com.serevin.partyforboost.dto.google.userinfo.UserInfo;
import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.exchanger.AbstractTokenExchanger;
import com.serevin.partyforboost.model.TokenHandlerType;
import com.serevin.partyforboost.service.GoogleUserInfoService;
import com.serevin.partyforboost.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenExchanger extends AbstractTokenExchanger {

    private final GoogleUserInfoService googleUserInfoService;

    public GoogleTokenExchanger(GoogleUserInfoService googleUserInfoService,
                                UserService userService) {
        super(userService);
        this.googleUserInfoService = googleUserInfoService;
    }

    @Override
    protected String getUserEmail(TokenRequest request) {
        UserInfo userInfo = googleUserInfoService.getUserInfoByAccessToken(request.token());
        return userInfo.email();
    }

    @Override
    public TokenHandlerType getTokenHandlerType() {
        return TokenHandlerType.GOOGLE;
    }
}
