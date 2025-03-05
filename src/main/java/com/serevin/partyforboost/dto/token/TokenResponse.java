package com.serevin.partyforboost.dto.token;

public record TokenResponse(TokenDto accessToken, TokenDto refreshToken) {
}
