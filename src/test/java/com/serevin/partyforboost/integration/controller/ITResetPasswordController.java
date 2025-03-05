package com.serevin.partyforboost.integration.controller;

import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.integration.ITBase;
import com.serevin.partyforboost.utils.ApiPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class ITResetPasswordController extends ITBase {

    @Value("${app.reset.password.maxResetPasswordAttempts}")
    private int maxResetPasswordAttempts;
    @Value("${app.reset.password.maxFailedCodeEnteringAttempts}")
    private int maxFailedCodeEnteringAttempts;

    private static final String EMAIL = "testResetPasswordEmail@example.com";
    private static final String USERNAME = "ResetPassword";
    private static final String PASSWORD = "tDSADASE1231231231";
    private static final String newPassword = "newPassword1";

    @BeforeEach
    public void setUp(){
        createUser(EMAIL, USERNAME, PASSWORD);
    }

    @Test
    void sendEmail_shouldSendResetPasswordEmail_WhenUserExists() throws Exception {
        String jsonRequest = createEmailJson(EMAIL);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        User updatedUser = userService.getByEmail(EMAIL);
        assertNotNull(updatedUser.getResetPasswordCode());
    }

    @Test
    void sendEmail_shouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        String jsonRequest = createEmailJson("wrongEmail");

        mvc.perform(post(ApiPaths.RESET_PASSWORD_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendEmail_shouldReturnTooManyRequests_WhenMaxAttemptsExceeded_thenReturns429() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordSentTimes(maxResetPasswordAttempts);
        byEmail.setResetPasswordCodeLastSentAt(LocalDateTime.now());
        userService.save(byEmail);

        String jsonRequest = createEmailJson(EMAIL);
        mvc.perform(post(ApiPaths.RESET_PASSWORD_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());

    }

    @Test
    void sendEmail_shouldResetAttempts_WhenTimeLimitPassed() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordSentTimes(maxResetPasswordAttempts);
        byEmail.setResetPasswordCodeLastSentAt(LocalDateTime.now().minusMinutes(6));
        userService.save(byEmail);

        String jsonRequest = createEmailJson(EMAIL);
        mvc.perform(post(ApiPaths.RESET_PASSWORD_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void validateCode_shouldValidateResetPasswordCodeSuccessfully() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeJson(EMAIL, byEmail.getResetPasswordCode());

        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());


        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertEquals(0, byEmailUpdated.getInvalidResetPasswordCodeEnteredTimes());
    }

    @Test
    void validateCode_shouldFailValidation_WhenCodeIsInvalid() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeJson(EMAIL, "000000");

        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertEquals(1, byEmailUpdated.getInvalidResetPasswordCodeEnteredTimes());
    }

    @Test
    void validateCode_shouldBlockUser_WhenMaxAttemptsExceeded() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(maxFailedCodeEnteringAttempts);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now());
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeJson(EMAIL, byEmail.getResetPasswordCode());

        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());

        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertEquals(maxFailedCodeEnteringAttempts, byEmailUpdated.getInvalidResetPasswordCodeEnteredTimes());
    }

    @Test
    void validateCode_shouldDenyAccess_WhenBlockedTimeNotExpired() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(maxFailedCodeEnteringAttempts);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now().minusMinutes(3)); // 5 minute lock
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeJson(EMAIL, byEmail.getResetPasswordCode());

        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void validateCode_shouldAllowValidation_AfterBlockedTimeExpired() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(maxFailedCodeEnteringAttempts);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now().minusMinutes(7)); // 5 minute lock
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeJson(EMAIL, byEmail.getResetPasswordCode());

        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertEquals(0, byEmailUpdated.getInvalidResetPasswordCodeEnteredTimes());
    }

    @Test
    void validateCode_shouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        String jsonRequest = createEmailAndCodeJson("nonexistent@example.com", "000000");
        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void changePassword_shouldChangePasswordSuccessfully_WhenCodeIsValid() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeAndPasswordJson(EMAIL, byEmail.getResetPasswordCode(), newPassword);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_CHANGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertTrue(passwordEncoder.matches(newPassword, byEmailUpdated.getPassword()));
        assertNull(byEmailUpdated.getResetPasswordCode());
    }

    @Test
    void changePassword_shouldReturnBadRequest_WhenCodeIsInvalid() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeAndPasswordJson(EMAIL, "000000", newPassword);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_CHANGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertEquals(1, byEmailUpdated.getInvalidResetPasswordCodeEnteredTimes());
    }

    @Test
    void changePassword_shouldReturnTooManyRequests_WhenMaxAttemptsExceeded() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(maxFailedCodeEnteringAttempts);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now());
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeAndPasswordJson(EMAIL, byEmail.getResetPasswordCode(), newPassword);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_CHANGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void changePassword_shouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        String jsonRequest = createEmailAndCodeAndPasswordJson("nonexistent@example.com", "000000", newPassword);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_CHANGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void changePassword_shouldResetFailedAttempts_WhenPasswordChangedSuccessfully() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(2);
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeAndPasswordJson(EMAIL, byEmail.getResetPasswordCode(), newPassword);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_CHANGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertEquals(0, byEmailUpdated.getInvalidResetPasswordCodeEnteredTimes());
    }

    @Test
    void changePassword_shouldNotAllowPasswordChange_WhenLockedOut() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(maxFailedCodeEnteringAttempts);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now().minusMinutes(3));
        userService.save(byEmail);

        String jsonRequest = createEmailAndCodeAndPasswordJson(EMAIL, byEmail.getResetPasswordCode(), newPassword);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_CHANGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }
}
