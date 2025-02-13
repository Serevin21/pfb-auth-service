package com.serevin.patyforboost.exchanger;

import com.serevin.patyforboost.dto.token.TokenRequest;
import com.serevin.patyforboost.entity.User;
import com.serevin.patyforboost.model.TokenHandlerType;

public interface TokenExchanger {

    User exchange(TokenRequest request);

    TokenHandlerType getTokenHandlerType();

}
