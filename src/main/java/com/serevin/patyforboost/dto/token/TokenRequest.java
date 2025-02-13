package com.serevin.patyforboost.dto.token;

import com.serevin.patyforboost.model.TokenHandlerType;
import jakarta.validation.constraints.NotNull;

public record TokenRequest(@NotNull TokenHandlerType handler,
                           String email,
                           String password,
                           String token) {
}
