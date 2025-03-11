package com.serevin.partyforboost.integration.controller;

import com.serevin.partyforboost.dto.signup.SignUpRequest;
import com.serevin.partyforboost.integration.ITBase;
import com.serevin.partyforboost.utils.ApiPaths;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
public class ITSignUpController extends ITBase {

    @Test
    void signUp_whenValidRequest_thenReturns200() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(
                "testsignup@gmail.com",
                "Doe12312",
                "password123QQ");
        String jsonRequest = objectMapper.writeValueAsString(signUpRequest);

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void signUp_whenInvalidPassword_missing8Characters_thenReturns400() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(
                "test@gmail.com",
                "Doe123",
                "pas1Q");
        String jsonRequest = objectMapper.writeValueAsString(signUpRequest);

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenInvalidPassword_moreThan30Characters_thenReturns400() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(
                "test@gmail.com",
                "Doe123",
                "pas1Q123456789123456789123456789123456789");
        String jsonRequest = objectMapper.writeValueAsString(signUpRequest);

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenInvalidPassword_thereIsNoNumber_thenReturns400() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(
                "test@gmail.com",
                "Doe123",
                "passwordNoNumber");
        String jsonRequest = objectMapper.writeValueAsString(signUpRequest);

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenInvalidPassword_thereIsNoCapitalLetter_thenReturns400() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(
                "test@gmail.com",
                "Doe123",
                "password1");
        String jsonRequest = objectMapper.writeValueAsString(signUpRequest);

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }


    @Test
    void signUp_whenInvalidUsername_thenReturns400() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(
                "test@gmail.com",
                "Do1",
                "password123Q");
        String jsonRequest = objectMapper.writeValueAsString(signUpRequest);

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

}
