package com.serevin.patyforboost.exchanger.impl;

import com.serevin.patyforboost.dto.google.userinfo.UserInfo;
import com.serevin.patyforboost.dto.token.TokenRequest;
import com.serevin.patyforboost.exchanger.AbstractTokenExchanger;
import com.serevin.patyforboost.model.TokenHandlerType;
import com.serevin.patyforboost.service.GoogleUserInfoService;
import com.serevin.patyforboost.service.UserService;
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
