package com.serevin.partyforboost.exchanger;

import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.service.UserService;

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
