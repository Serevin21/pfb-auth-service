package com.serevin.patyforboost.dto.token;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) {
}
