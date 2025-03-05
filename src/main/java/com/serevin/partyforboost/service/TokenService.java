package com.serevin.partyforboost.service;

import com.serevin.partyforboost.dto.token.RefreshTokenRequest;
import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.dto.token.TokenResponse;
import jakarta.validation.Valid;

public interface TokenService {

    TokenResponse generateToken(TokenRequest tokenRequest, String userAgent);
    TokenResponse refreshAccessToken(@Valid RefreshTokenRequest tokenRequest);
    void revokeRefreshToken(String email);

}
