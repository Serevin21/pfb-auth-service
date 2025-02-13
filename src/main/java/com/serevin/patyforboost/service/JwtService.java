package com.serevin.patyforboost.service;

import com.serevin.patyforboost.dto.token.TokenResponse;
import com.serevin.patyforboost.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

    TokenResponse generateTokens(User user);

    boolean validateToken(String token);

    String getEmailFromToken(String token);

    User validateRefreshToken(String refreshToken);

    Claims getClaimsFromToken(String token);

}
