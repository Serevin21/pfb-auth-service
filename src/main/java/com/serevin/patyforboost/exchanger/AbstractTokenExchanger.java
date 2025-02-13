package com.serevin.patyforboost.exchanger;

import com.serevin.patyforboost.dto.token.TokenRequest;
import com.serevin.patyforboost.entity.User;
import com.serevin.patyforboost.service.UserService;

public abstract class AbstractTokenExchanger implements TokenExchanger {

    private final UserService userService;

    public AbstractTokenExchanger(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User exchange(TokenRequest request) {
        String userEmail = getUserEmail(request);
        return userService.getByEmail(userEmail);
    }

    protected abstract String getUserEmail(TokenRequest request);
}
