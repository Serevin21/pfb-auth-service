package com.serevin.partyforboost.unit.service;

import com.serevin.partyforboost.dto.token.TokenDto;
import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.dto.token.TokenResponse;
import com.serevin.partyforboost.entity.RefreshToken;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.exchanger.TokenExchanger;
import com.serevin.partyforboost.mapper.RefreshTokenMapper;
import com.serevin.partyforboost.model.TokenHandlerType;
import com.serevin.partyforboost.service.JwtService;
import com.serevin.partyforboost.service.RefreshTokenService;
import com.serevin.partyforboost.service.impl.TokenServiceImpl;
import com.serevin.partyforboost.validator.pre.TokenPreValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TokenServiceImplTest {

//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private RefreshTokenService refreshTokenService;
//
//    @Mock
//    private RefreshTokenMapper refreshTokenMapper;
//
//    @Mock
//    private TokenExchanger tokenExchanger;
//
//    @Mock
//    private TokenPreValidator tokenPreValidator;
//
//    @InjectMocks
//    private TokenServiceImpl tokenService;
//
//    private User user;
//    private TokenRequest tokenRequest;
//    private TokenResponse tokenResponse;
//    private RefreshToken refreshToken;
//
//    @BeforeEach
//    public void setUp() {
//        user = new User();
//        tokenRequest = mock(TokenRequest.class);
//        tokenResponse = mock(TokenResponse.class);
//        refreshToken = mock(RefreshToken.class);
//    }
//
//    @Test
//    void testGenerateToken_Success() {
//        when(tokenRequest.handler()).thenReturn(TokenHandlerType.CREDENTIALS);
//        when(tokenExchanger.exchange(tokenRequest)).thenReturn(user);
//        when(jwtService.generateTokens(user)).thenReturn(tokenResponse);
//        when(tokenResponse.refreshToken()).thenReturn(new TokenDto("refreshToken", System.currentTimeMillis() + 10000));
//        when(refreshTokenMapper.map(any(), any(), any(), any())).thenReturn(refreshToken);
//
//        TokenResponse response = tokenService.generateToken(tokenRequest, "user-agent");
//        assertNotNull(response);
//
//        verify(refreshTokenService, times(1)).saveRefreshToken(any());
//    }

}
