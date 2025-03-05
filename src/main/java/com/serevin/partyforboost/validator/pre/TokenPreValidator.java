package com.serevin.partyforboost.validator.pre;

import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.model.TokenHandlerType;

public interface TokenPreValidator {

    void preValidate(User user, TokenRequest tokenRequest);

    TokenHandlerType getTokenHandlerType();

}
