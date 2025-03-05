package com.serevin.partyforboost.validator.pre.impl;

import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.exception.BadCredentialsException;
import com.serevin.partyforboost.model.TokenHandlerType;
import com.serevin.partyforboost.validator.pre.AbstractTokenPreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialsTokenPreValidator extends AbstractTokenPreValidator {

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void preValidateImpl(User user, TokenRequest tokenRequest) {
        if (!passwordEncoder.matches(tokenRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password provided");
        }
    }

    @Override
    public TokenHandlerType getTokenHandlerType() {
        return TokenHandlerType.CREDENTIALS;
    }
}
