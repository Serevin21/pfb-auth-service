package com.serevin.patyforboost.service;

import com.serevin.patyforboost.dto.token.RefreshTokenRequest;
import com.serevin.patyforboost.dto.token.TokenRequest;
import com.serevin.patyforboost.dto.token.TokenResponse;
import jakarta.validation.Valid;

public interface TokenService {

    TokenResponse generateToken(TokenRequest tokenRequest, String userAgent);
    TokenResponse refreshAccessToken(@Valid RefreshTokenRequest tokenRequest);
    void revokeRefreshToken(String email);

}
