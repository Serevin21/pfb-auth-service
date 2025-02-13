package com.serevin.patyforboost.service.impl;

import com.serevin.patyforboost.entity.RefreshToken;
import com.serevin.patyforboost.exception.InvalidRefreshTokenException;
import com.serevin.patyforboost.repository.RefreshTokenRepository;
import com.serevin.patyforboost.service.RefreshTokenService;
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
