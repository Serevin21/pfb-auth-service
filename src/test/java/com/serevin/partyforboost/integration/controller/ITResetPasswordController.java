package com.serevin.partyforboost.integration.controller;

import com.serevin.partyforboost.dto.reset.password.ChangePasswordRequest;
import com.serevin.partyforboost.dto.reset.password.ResetPasswordRequest;
import com.serevin.partyforboost.dto.reset.password.ValidateCodeRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.integration.ITBase;
import com.serevin.partyforboost.utils.ApiPaths;
import com.serevin.partyforboost.utils.ResetPasswordsProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(EMAIL);
        String jsonRequest = objectMapper.writeValueAsString(resetPasswordRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        User updatedUser = userService.getByEmail(EMAIL);
        assertNotNull(updatedUser.getResetPasswordCode());
    }

    @Test
    void sendEmail_shouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("wrongEmail@gmail.com");
        String jsonRequest = objectMapper.writeValueAsString(resetPasswordRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void sendEmail_shouldReturnTooManyRequests_WhenMaxAttemptsExceeded_thenReturns429() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordSentTimes(ResetPasswordsProperties.MAX_RESET_PASSWORD_ATTEMPTS);
        byEmail.setResetPasswordCodeLastSentAt(LocalDateTime.now());
        userService.save(byEmail);

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(EMAIL);
        String jsonRequest = objectMapper.writeValueAsString(resetPasswordRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_SEND_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void sendEmail_shouldResetAttempts_WhenTimeLimitPassed() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordSentTimes(ResetPasswordsProperties.MAX_RESET_PASSWORD_ATTEMPTS);
        byEmail.setResetPasswordCodeLastSentAt(LocalDateTime.now().minusMinutes(6));
        userService.save(byEmail);

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(EMAIL);
        String jsonRequest = objectMapper.writeValueAsString(resetPasswordRequest);

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

        ValidateCodeRequest validateCodeRequest = new ValidateCodeRequest(EMAIL, byEmail.getResetPasswordCode());
        String jsonRequest = objectMapper.writeValueAsString(validateCodeRequest);

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

        ValidateCodeRequest validateCodeRequest = new ValidateCodeRequest(EMAIL,"000000");
        String jsonRequest = objectMapper.writeValueAsString(validateCodeRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertEquals(1, byEmailUpdated.getInvalidResetPasswordCodeEnteredTimes());
    }

    @Test
    void validateCode_shouldNotIncreaseAttempts_whenExceededMaximumAttempts() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(ResetPasswordsProperties.MAX_FAILED_CODE_ENTERING_ATTEMPTS);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now());
        userService.save(byEmail);

        ValidateCodeRequest validateCodeRequest = new ValidateCodeRequest(EMAIL, byEmail.getResetPasswordCode());
        String jsonRequest = objectMapper.writeValueAsString(validateCodeRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());

        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertEquals(ResetPasswordsProperties.MAX_FAILED_CODE_ENTERING_ATTEMPTS, byEmailUpdated.getInvalidResetPasswordCodeEnteredTimes());
    }

    @Test
    void validateCode_shouldDenyAccess_WhenBlockedTimeNotExpired() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(ResetPasswordsProperties.MAX_FAILED_CODE_ENTERING_ATTEMPTS);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now()
                .minusMinutes(ResetPasswordsProperties.FAILED_CODE_ENTERING_LOCK_DURATION_MINUTES - 2L));
        userService.save(byEmail);

        ValidateCodeRequest validateCodeRequest = new ValidateCodeRequest(EMAIL, byEmail.getResetPasswordCode());
        String jsonRequest = objectMapper.writeValueAsString(validateCodeRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void validateCode_shouldAllowValidation_AfterBlockedTimeExpired() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(ResetPasswordsProperties.MAX_FAILED_CODE_ENTERING_ATTEMPTS);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now()
                .minusMinutes(ResetPasswordsProperties.FAILED_CODE_ENTERING_LOCK_DURATION_MINUTES + 2L));
        userService.save(byEmail);

        ValidateCodeRequest validateCodeRequest = new ValidateCodeRequest(EMAIL, byEmail.getResetPasswordCode());
        String jsonRequest = objectMapper.writeValueAsString(validateCodeRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        User byEmailUpdated = userService.getByEmail(EMAIL);
        assertEquals(0, byEmailUpdated.getInvalidResetPasswordCodeEnteredTimes());
    }

    @Test
    void validateCode_shouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        ValidateCodeRequest validateCodeRequest = new ValidateCodeRequest("nonexistent@example.com", "000000");
        String jsonRequest = objectMapper.writeValueAsString(validateCodeRequest);
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

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(EMAIL, byEmail.getResetPasswordCode(), newPassword);
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

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

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(EMAIL, "000000", newPassword);
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

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
        byEmail.setInvalidResetPasswordCodeEnteredTimes(ResetPasswordsProperties.MAX_FAILED_CODE_ENTERING_ATTEMPTS);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now());
        userService.save(byEmail);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(EMAIL, byEmail.getResetPasswordCode(), newPassword);
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_CHANGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void changePassword_shouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("nonexistent@example.com", "000000", newPassword);
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_CHANGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void changePassword_shouldResetFailedAttempts_WhenPasswordChangedSuccessfully() throws Exception {
        User byEmail = userService.getByEmail(EMAIL);
        byEmail.setResetPasswordCode("452367");
        byEmail.setInvalidResetPasswordCodeEnteredTimes(ResetPasswordsProperties.MAX_RESET_PASSWORD_ATTEMPTS - 1);
        userService.save(byEmail);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(EMAIL, byEmail.getResetPasswordCode(), newPassword);
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

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
        byEmail.setInvalidResetPasswordCodeEnteredTimes(ResetPasswordsProperties.MAX_FAILED_CODE_ENTERING_ATTEMPTS);
        byEmail.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now()
                .minusMinutes(ResetPasswordsProperties.FAILED_CODE_ENTERING_LOCK_DURATION_MINUTES - 2L));
        userService.save(byEmail);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(EMAIL, byEmail.getResetPasswordCode(), newPassword);
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequest);

        mvc.perform(post(ApiPaths.RESET_PASSWORD_CHANGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isTooManyRequests());
    }
}
