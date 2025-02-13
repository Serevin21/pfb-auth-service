package com.serevin.patyforboost.service.impl;

import com.serevin.patyforboost.dto.token.TokenDto;
import com.serevin.patyforboost.dto.token.TokenResponse;
import com.serevin.patyforboost.entity.User;
import com.serevin.patyforboost.service.JwtService;
import com.serevin.patyforboost.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final UserService userService;

    @Value("${app.token.secret}")
    private String secret;

    @Value("${app.token.issuer}")
    private String issuer;

    private long accessTokenExpiration;
    private long refreshTokenExpiration;

    @Override
    public TokenResponse generateTokens(User user) {
        Date now = new Date();

        Date accessTokenExpiration = new Date(now.getTime() + this.accessTokenExpiration);
        TokenDto accessToken = new TokenDto(generateToken(user, now, accessTokenExpiration),
                accessTokenExpiration.getTime());

        Date refreshTokenExpiration = new Date(now.getTime() + this.refreshTokenExpiration);
        TokenDto refreshToken = new TokenDto(generateToken(user, now, refreshTokenExpiration),
                refreshTokenExpiration.getTime());

        return new TokenResponse(accessToken, refreshToken);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> jws = getJwsFromToken(token);
            return jws != null;
        } catch (Exception e) {
            log.error("Failed to parse JWT", e);
            return false;
        }
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return getJwsFromToken(token).getPayload();
    }

    @Override
    public User validateRefreshToken(String refreshToken) {
        Jws<Claims> jws = getJwsFromToken(refreshToken);
        String email = jws.getPayload().getSubject();
        return userService.getByEmail(email);
    }

    @Override
    public String getEmailFromToken(String token) {
        Jws<Claims> jws = getJwsFromToken(token);
        return jws.getPayload().getSubject();
    }

    private Jws<Claims> getJwsFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token);
    }

    private String generateToken(User user, Date now, Date expiration) {
        return Jwts.builder()
                .issuer(issuer)
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Value("${app.token.accessTokenExpiration:3600000}")
    public void setAccessTokenExpiration(long accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    @Value("${app.token.refreshTokenExpiration:7884000000}")
    public void setRefreshTokenExpiration(long refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}
