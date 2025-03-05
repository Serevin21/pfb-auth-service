package com.serevin.partyforboost.exchanger;

import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.model.TokenHandlerType;

public interface TokenExchanger {

    User exchange(TokenRequest request);

    TokenHandlerType getTokenHandlerType();

}
