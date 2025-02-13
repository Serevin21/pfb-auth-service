package com.serevin.patyforboost.dto.token;

public record TokenResponse(TokenDto accessToken, TokenDto refreshToken) {
}
