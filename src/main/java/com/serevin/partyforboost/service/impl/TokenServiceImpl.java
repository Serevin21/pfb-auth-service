package com.serevin.partyforboost.service.impl;

import com.serevin.partyforboost.dto.token.RefreshTokenRequest;
import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.dto.token.TokenResponse;
import com.serevin.partyforboost.entity.RefreshToken;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.exception.ExpiredRefreshTokenException;
import com.serevin.partyforboost.exception.InvalidRefreshTokenException;
import com.serevin.partyforboost.exchanger.TokenExchanger;
import com.serevin.partyforboost.mapper.RefreshTokenMapper;
import com.serevin.partyforboost.model.TokenHandlerType;
import com.serevin.partyforboost.service.JwtService;
import com.serevin.partyforboost.service.RefreshTokenService;
import com.serevin.partyforboost.service.TokenService;
import com.serevin.partyforboost.validator.pre.TokenPreValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.serevin.partyforboost.utils.DateTimeUtils.convertMillisToLocalDateTime;

@Service
public class TokenServiceImpl implements TokenService {

    private final Map<TokenHandlerType, TokenExchanger> tokenExchangerMap;
    private final Map<TokenHandlerType, TokenPreValidator> tokenPreValidatorMap;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenMapper refreshTokenMapper;

    public TokenServiceImpl(List<TokenExchanger> tokenExchangers,
                            List<TokenPreValidator> tokenPreValidators,
                            JwtService jwtService, RefreshTokenService refreshTokenService, RefreshTokenMapper refreshTokenMapper) {
        this.tokenExchangerMap = tokenExchangers.stream()
                .collect(Collectors.toMap(
                        TokenExchanger::getTokenHandlerType,
                        tokenExchanger -> tokenExchanger
                ));
        this.tokenPreValidatorMap = tokenPreValidators.stream()
                .collect(Collectors.toMap(
                        TokenPreValidator::getTokenHandlerType,
                        tokenPreValidator -> tokenPreValidator
                ));
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @Transactional
    @Override
    public TokenResponse generateToken(TokenRequest tokenRequest, String userAgent) {
        TokenHandlerType handler = tokenRequest.handler();
        TokenExchanger tokenExchanger = tokenExchangerMap.get(handler);
        User user = tokenExchanger.exchange(tokenRequest);

        Optional.ofNullable(tokenPreValidatorMap.get(handler))
                .ifPresent(tokenPreValidator -> tokenPreValidator.preValidate(user, tokenRequest));

        TokenResponse tokenResponse = jwtService.generateTokens(user);
        LocalDateTime dateTimeExpiryAt = convertMillisToLocalDateTime(tokenResponse.refreshToken().expiresAt());

        RefreshToken refreshToken = refreshTokenMapper.map(user, userAgent, tokenResponse.refreshToken().token(), dateTimeExpiryAt);
        refreshTokenService.saveRefreshToken(refreshToken);
        return tokenResponse;
    }

    @Transactional
    @Override
    public TokenResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenRequest.refreshToken());

        if(refreshToken.getExpiryAt().isBefore(LocalDateTime.now())) {
            refreshTokenService.deleteRefreshToken(refreshToken);
            throw new ExpiredRefreshTokenException("Refresh token has expired");
        }

        User user = jwtService.validateRefreshToken(refreshToken.getToken());
        return jwtService.generateTokens(user);
    }

    @Transactional
    @Override
    public void revokeRefreshToken(String email) {
        List<RefreshToken> tokens = refreshTokenService.findTokensByEmail(email);

        if (tokens.isEmpty()) {
            throw new InvalidRefreshTokenException("No refresh tokens found for the given email.");
        }

        tokens.forEach(refreshTokenService::deleteRefreshToken);
    }

}
