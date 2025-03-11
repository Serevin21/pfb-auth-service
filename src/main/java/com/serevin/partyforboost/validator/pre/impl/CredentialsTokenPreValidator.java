package com.serevin.partyforboost.validator.pre.impl;

import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.exception.BadCredentialsException;
import com.serevin.partyforboost.exception.ExceededAttemptsException;
import com.serevin.partyforboost.exception.InvalidPasswordOrEmailException;
import com.serevin.partyforboost.model.TokenHandlerType;
import com.serevin.partyforboost.utils.LoginAttemptProperties;
import com.serevin.partyforboost.validator.pre.AbstractTokenPreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CredentialsTokenPreValidator extends AbstractTokenPreValidator {

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void preValidateImpl(User user, TokenRequest tokenRequest) {
        validateExceededAttempts(user);

        if (!passwordEncoder.matches(tokenRequest.password(), user.getPassword())) {
            int invalidPasswordEnteredTimes = user.getInvalidPasswordEnteredTimes() + 1;
            user.setInvalidPasswordEnteredTimes(invalidPasswordEnteredTimes);

            if (invalidPasswordEnteredTimes >= LoginAttemptProperties.MAX_FAILED_ENTERING_ATTEMPTS) {
                user.setInvalidPasswordEnteredLastTimeAt(LocalDateTime.now());
            }

            throw new InvalidPasswordOrEmailException("Invalid email or password provided");
        } else {
            user.setInvalidPasswordEnteredLastTimeAt(null);
            user.setInvalidPasswordEnteredTimes(0);
        }
    }

    private void validateExceededAttempts(User user) {
        LocalDateTime invalidPasswordEnteredLastTimeAt = user.getInvalidPasswordEnteredLastTimeAt();

        if (invalidPasswordEnteredLastTimeAt != null && invalidPasswordEnteredLastTimeAt
                .plusMinutes(LoginAttemptProperties.MAX_FAILED_ENTERING_ATTEMPTS).isAfter(LocalDateTime.now())) {
            throw new ExceededAttemptsException("You have exceeded login attempts.",
                    invalidPasswordEnteredLastTimeAt.plusMinutes(LoginAttemptProperties.MAX_FAILED_ENTERING_ATTEMPTS));
        }
    }

    @Override
    public TokenHandlerType getTokenHandlerType() {
        return TokenHandlerType.CREDENTIALS;
    }
}
