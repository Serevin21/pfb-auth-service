package com.serevin.partyforboost.integration.controller;

import com.serevin.partyforboost.dto.email.ActivationRequest;
import com.serevin.partyforboost.dto.email.SendActivationEmailRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.enums.UserStatus;
import com.serevin.partyforboost.integration.ITBase;
import com.serevin.partyforboost.utils.ApiPaths;
import com.serevin.partyforboost.utils.EmailActivationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class ITEmailActivationController extends ITBase {

    private static final String EMAIL = "testEmail@example.com";
    private static final String USERNAME = "Serevin1337";
    private static final String PASSWORD = "tDSADASE1231231231";

    @BeforeEach
    public void setUp(){
        createUser(EMAIL, USERNAME, PASSWORD);
    }

    @Test
    void sendEmail_whenValidRequest_thenReturns200() throws Exception {
        SendActivationEmailRequest sendActivationEmailRequest = new SendActivationEmailRequest(EMAIL);
        String jsonRequest = objectMapper.writeValueAsString(sendActivationEmailRequest);

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        User byEmail = userService.getByEmail(EMAIL);
        String activationCode = byEmail.getActivationCode();

        assertFalse(activationCode.isBlank(), "Activation code should not be blank");
    }

    @Test
    void sendEmail_whenInvalidEmail_thenReturns400() throws Exception {
        SendActivationEmailRequest sendActivationEmailRequest = new SendActivationEmailRequest("invalid-email");
        String jsonRequest = objectMapper.writeValueAsString(sendActivationEmailRequest);

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activate_whenValidRequest_thenReturns200() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        String activationCode = byEmail.getActivationCode();

        ActivationRequest activationRequest = new ActivationRequest(EMAIL, activationCode);
        String jsonRequest = objectMapper.writeValueAsString(activationRequest);

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        byEmail = userService.getByEmail(EMAIL);

        assertEquals(UserStatus.ACTIVE, byEmail.getStatus(), "User status should be ACTIVE after activation");
    }

    @Test
    void activate_whenInvalidEmail_thenReturns400() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        String activationCode = byEmail.getActivationCode();
        userService.save(byEmail);

        ActivationRequest activationRequest = new ActivationRequest("invalid-email", activationCode);
        String jsonRequest = objectMapper.writeValueAsString(activationRequest);

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activate_whenCodeMissing_thenReturns400() throws Exception {
        ActivationRequest activationRequest = new ActivationRequest(EMAIL, "");
        String jsonRequest = objectMapper.writeValueAsString(activationRequest);

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activate_whenInvalidCode_thenReturns400() throws Exception {
        ActivationRequest activationRequest = new ActivationRequest(EMAIL, "111111111");
        String jsonRequest = objectMapper.writeValueAsString(activationRequest);

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendEmail_whenExceededAttempts_thenReturns429() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setActivationCodeSentTimes(EmailActivationProperties.MAX_FAILED_CODE_ENTERING_ATTEMPTS);
        userService.save(byEmail);

        SendActivationEmailRequest sendActivationEmailRequest = new SendActivationEmailRequest(EMAIL);
        String jsonRequest = objectMapper.writeValueAsString(sendActivationEmailRequest);

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void activateEmail_whenExceededAttempts_thenReturns429() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setInvalidActivationCodeEnteredTimes(EmailActivationProperties.MAX_ACTIVATION_EMAIL_ATTEMPTS);
        byEmail.setInvalidActivationCodeEnteredLastTimeAt(LocalDateTime.now());
        String activationCode = byEmail.getActivationCode();
        userService.save(byEmail);

        ActivationRequest activationRequest = new ActivationRequest(EMAIL, activationCode);
        String jsonRequest = objectMapper.writeValueAsString(activationRequest);

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }
}
