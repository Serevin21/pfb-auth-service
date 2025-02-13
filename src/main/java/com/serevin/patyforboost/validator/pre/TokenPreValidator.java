package com.serevin.patyforboost.validator.pre;

import com.serevin.patyforboost.dto.token.TokenRequest;
import com.serevin.patyforboost.entity.User;
import com.serevin.patyforboost.model.TokenHandlerType;

public interface TokenPreValidator {

    void preValidate(User user, TokenRequest tokenRequest);

    TokenHandlerType getTokenHandlerType();

}
