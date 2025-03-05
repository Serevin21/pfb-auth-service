package com.serevin.partyforboost.controller;

import com.serevin.partyforboost.dto.token.RefreshTokenRequest;
import com.serevin.partyforboost.dto.token.TokenRequest;
import com.serevin.partyforboost.dto.token.TokenResponse;
import com.serevin.partyforboost.security.UserDetailsImpl;
import com.serevin.partyforboost.service.TokenService;
import com.serevin.partyforboost.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final TokenService tokenService;

    @PostMapping(ApiPaths.TOKEN)
    public ResponseEntity<TokenResponse> getToken(@Valid @RequestBody TokenRequest tokenRequest,
                                                  @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
        TokenResponse token = tokenService.generateToken(tokenRequest, userAgent);
        return ResponseEntity.ok(token);
    }

    @PostMapping(ApiPaths.REFRESH_TOKEN)
    public ResponseEntity<TokenResponse> refreshAccessToken(@RequestBody @Valid RefreshTokenRequest request) {
        TokenResponse response = tokenService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(ApiPaths.TOKEN_SIGN_OUT)
    public ResponseEntity<Void> signOut(Authentication authentication) {

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String email = principal.getEmail();

        tokenService.revokeRefreshToken(email);
        return ResponseEntity.noContent().build();
    }

}
