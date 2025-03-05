package com.serevin.partyforboost.service;

import com.serevin.partyforboost.entity.RefreshToken;

import java.util.List;

public interface RefreshTokenService {

    RefreshToken saveRefreshToken(RefreshToken refreshToken);
    RefreshToken findRefreshToken(String refreshToken);
    void deleteRefreshToken(RefreshToken refreshToken);
    List<RefreshToken> findTokensByEmail(String email);

}
