package com.serevin.partyforboost.dto.token;

import com.serevin.partyforboost.model.TokenHandlerType;
import jakarta.validation.constraints.NotNull;

public record TokenRequest(@NotNull TokenHandlerType handler,
                           String email,
                           String password,
                           String token) {
}
