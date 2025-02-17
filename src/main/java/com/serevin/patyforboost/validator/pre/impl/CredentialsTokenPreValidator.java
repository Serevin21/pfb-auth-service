package com.serevin.patyforboost.validator.pre.impl;

import com.serevin.patyforboost.dto.token.TokenRequest;
import com.serevin.patyforboost.entity.User;
import com.serevin.patyforboost.exception.BadCredentialsException;
import com.serevin.patyforboost.model.TokenHandlerType;
import com.serevin.patyforboost.validator.pre.AbstractTokenPreValidator;
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
