package com.serevin.partyforboost.integration.controller;

import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.enums.UserStatus;
import com.serevin.partyforboost.integration.ITBase;
import com.serevin.partyforboost.utils.ApiPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class ITEmailActivationController extends ITBase {

    @Value("${app.activation.email.maxActivationEmailAttempts}")
    private int maxActivationEmailAttempts;
    @Value("${app.activation.email.maxFailedCodeEnteringAttempts}")
    private int maxFailedCodeEnteringAttempts;

    private static final String EMAIL = "testEmail@example.com";
    private static final String USERNAME = "Serevin1337";
    private static final String PASSWORD = "tDSADASE1231231231";


    @BeforeEach
    public void setUp(){
        createUser(EMAIL, USERNAME, PASSWORD);
    }

    @Test
    void sendEmail_whenValidRequest_thenReturns200() throws Exception {
        String jsonRequest = createEmailJson(EMAIL);

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
        String jsonRequest = createEmailJson("invalid-email");

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activate_whenValidRequest_thenReturns200() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        String activationCode = byEmail.getActivationCode();
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeJson(EMAIL, activationCode);

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

        String jsonRequest = createEmailAndCodeJson("invalid-email", activationCode);
        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activate_whenCodeMissing_thenReturns400() throws Exception {
        String jsonRequest = createEmailAndCodeJson(EMAIL, "");

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activate_whenInvalidCode_thenReturns400() throws Exception {
        String jsonRequest = createEmailAndCodeJson(EMAIL, "111111111");

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendEmail_whenExceededAttempts_thenReturns429() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setActivationCodeSentTimes(maxFailedCodeEnteringAttempts);
        userService.save(byEmail);

        String jsonRequest = createEmailJson(EMAIL);
        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void activateEmail_whenExceededAttempts_thenReturns429() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setInvalidActivationCodeEnteredTimes(maxActivationEmailAttempts);
        byEmail.setInvalidActivationCodeEnteredLastTimeAt(LocalDateTime.now());
        String activationCode = byEmail.getActivationCode();
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeJson(EMAIL, activationCode);

        mvc.perform(post(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }
}
