package com.serevin.partyforboost.service;

import com.serevin.partyforboost.dto.token.TokenResponse;
import com.serevin.partyforboost.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

    TokenResponse generateTokens(User user);

    boolean validateToken(String token);

    String getEmailFromToken(String token);

    User validateRefreshToken(String refreshToken);

    Claims getClaimsFromToken(String token);

}
