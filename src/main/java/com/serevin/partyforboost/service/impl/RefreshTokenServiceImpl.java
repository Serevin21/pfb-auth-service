package com.serevin.partyforboost.service.impl;

import com.serevin.partyforboost.entity.RefreshToken;
import com.serevin.partyforboost.exception.InvalidRefreshTokenException;
import com.serevin.partyforboost.repository.RefreshTokenRepository;
import com.serevin.partyforboost.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    @Override
    public RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token is invalid or expired"));
    }

    @Transactional
    @Override
    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RefreshToken> findTokensByEmail(String email) {
        return refreshTokenRepository.findTokensByEmail(email);
    }
}
