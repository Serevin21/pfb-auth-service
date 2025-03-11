package com.serevin.partyforboost.integration.controller;

import com.jayway.jsonpath.JsonPath;
import com.serevin.partyforboost.dto.token.RefreshTokenRequest;
import com.serevin.partyforboost.entity.RefreshToken;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.integration.ITBase;
import com.serevin.partyforboost.model.TokenHandlerType;
import com.serevin.partyforboost.repository.RefreshTokenRepository;
import com.serevin.partyforboost.utils.ApiPaths;
import com.serevin.partyforboost.utils.LoginAttemptProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.serevin.partyforboost.utils.ErrorCodes.EXPIRED_REFRESH_TOKEN_ERROR;
import static com.serevin.partyforboost.utils.ErrorCodes.INVALID_REFRESH_TOKEN_ERROR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
public class   ITTokenController extends ITBase {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private static final String BEARER_PREFIX = "Bearer ";

    private static final String EMAIL = "test@example.com";
    private static final String USERNAME = "Serevin228";
    private static final String PASSWORD = "tDSADASE1231231231";

    private String refreshToken;
    private String accessToken;

    @BeforeEach
    public void setUp(){
        createUser(EMAIL, USERNAME, PASSWORD);

        Map<String, String> tokens = createAndReturnTokens(TokenHandlerType.CREDENTIALS, EMAIL, PASSWORD);
        refreshToken = tokens.get("refreshToken");
        accessToken = tokens.get("accessToken");
    }

    private Map<String, String> createAndReturnTokens(TokenHandlerType tokenHandlerType, String email, String password) {
        String jsonRequest = String.format("""
            {
                "handler": "%s",
                "email": "%s",
                "password": "%s"
            }
        """,tokenHandlerType, email, password);

        try {
            String response = mvc.perform(post(ApiPaths.TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            String accessToken = JsonPath.read(response, "$.accessToken.token");
            String refreshToken = JsonPath.read(response, "$.refreshToken.token");

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return tokens;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tokens", e);
        }
    }


    @Test
    void generateToken_whenValidCredentials_thenReturnToken() throws Exception {
        String jsonRequest = String.format("""
            {
                "handler": "%s",
                "email": "%s",
                "password": "%s"
            }
            """, TokenHandlerType.CREDENTIALS.name(), EMAIL, PASSWORD
        );
        mvc.perform(post(ApiPaths.TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void generateToken_whenInvalidPassword_thenUnauthorized() throws Exception {
        String jsonRequest = String.format("""
        {
            "handler": "%s",
            "email": "%s",
            "password": "WrongPassword123"
        }
        """, TokenHandlerType.CREDENTIALS.name(), EMAIL
        );

        mvc.perform(post(ApiPaths.TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void generateToken_whenEmailNotFound_thenNotFound() throws Exception {
        String jsonRequest = String.format("""
        {
            "handler": "%s",
            "email": "notexist@example.com",
            "password": "%s"
        }
        """, TokenHandlerType.CREDENTIALS.name(), PASSWORD
        );

        mvc.perform(post(ApiPaths.TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void generateToken_whenEmptyBody_thenBadRequest() throws Exception {
        mvc.perform(post(ApiPaths.TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateToken_whenNullValues_thenBadRequest() throws Exception {
        String jsonRequest = """
        {
            "handler": null,
            "email": null,
            "password": null
        }
        """;

        mvc.perform(post(ApiPaths.TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateToken_whenInvalidHandler_thenBadRequest() throws Exception {
        String jsonRequest = String.format("""
        {
            "handler": "INVALID_HANDLER",
            "email": "%s",
            "password": "%s"
        }
        """, EMAIL, PASSWORD);

        mvc.perform(post(ApiPaths.TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateToken_whenExceededAttempts_thenReturn429() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setInvalidPasswordEnteredTimes(LoginAttemptProperties.MAX_FAILED_ENTERING_ATTEMPTS);
        byEmail.setInvalidPasswordEnteredLastTimeAt(LocalDateTime.now().minusMinutes(LoginAttemptProperties.LOCK_TIME_IN_MINUTES - 2));
        userService.save(byEmail);

        String jsonRequest = String.format("""
        {
            "handler": "%s",
            "email": "%s",
            "password": "%s"
        }
        """,TokenHandlerType.CREDENTIALS.name(), EMAIL, PASSWORD);

        mvc.perform(post(ApiPaths.TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void refreshAccessToken_whenValidRefreshToken_thenReturnsNewAccessToken() throws Exception {
        RefreshTokenRequest token = new RefreshTokenRequest(refreshToken);
        String json = objectMapper.writeValueAsString(token);

        Thread.sleep(5000);
        String oldAccessToken = accessToken;
        mvc.perform(post(ApiPaths.REFRESH_TOKEN)
                        .content(json)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken.token").exists())
                .andDo(print())
                .andExpect(result -> {
                    String newAccessToken = JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken.token");
                    assertNotEquals(oldAccessToken, newAccessToken);
                });

    }

    @Test
    void refreshAccessToken_whenInvalidRefreshToken_thenReturnsError() throws Exception {
        RefreshTokenRequest invalidToken = new RefreshTokenRequest("invalidToken");
        String json = objectMapper.writeValueAsString(invalidToken);

        mvc.perform(post(ApiPaths.REFRESH_TOKEN)
                        .content(json)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(INVALID_REFRESH_TOKEN_ERROR))
                .andDo(print());
    }

    @Test
    void refreshAccessToken_whenExpiredRefreshToken_thenReturnsError() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Optional<RefreshToken> expiredTokenOpt = refreshTokenRepository.findByToken("expired_token_example");
        if (expiredTokenOpt.isEmpty()) {
            throw new RuntimeException("Test setup error: Expired refresh token not found in the database");
        }
        RefreshToken expiredToken = expiredTokenOpt.get();

        assertThat(expiredToken.getExpiryAt()).isBefore(now);

        RefreshTokenRequest token = new RefreshTokenRequest(expiredToken.getToken());
        String json = objectMapper.writeValueAsString(token);

        mvc.perform(post(ApiPaths.REFRESH_TOKEN)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(EXPIRED_REFRESH_TOKEN_ERROR))
                .andDo(print());
    }

    @Test
    void testSignOut_SuccessfulRevocation() throws Exception {
        mvc.perform(
                        delete(ApiPaths.TOKEN_SIGN_OUT)
                                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + refreshToken)
                )
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void testSignOut_InvalidToken() throws Exception {
        mvc.perform(
                        delete(ApiPaths.TOKEN_SIGN_OUT)
                                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + "invalidToken")
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
