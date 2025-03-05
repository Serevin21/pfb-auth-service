package com.serevin.partyforboost.integration.controller;

import com.serevin.partyforboost.integration.ITBase;
import com.serevin.partyforboost.utils.ApiPaths;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ITSignUpController extends ITBase {

    @Test
    void signUp_whenValidRequest_thenReturns200() throws Exception {
        String jsonRequest = """
                    {
                        "email": "test@gmail.com",
                        "password": "password123Q",
                        "username": "Doe123"
                    }
                """;

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void signUp_whenInvalidPassword_missing8Characters_thenReturns400() throws Exception {
        String jsonRequest = """
                     {
                        "email": "test@gmail.com",
                        "password": "pas1Q",
                        "username": "Doe123"
                    }
                """;

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenInvalidPassword_moreThan30Characters_thenReturns400() throws Exception {
        String jsonRequest = """
                     {
                        "email": "test@gmail.com",
                        "password": "pas1Q123456789123456789123456789123456789",
                        "username": "Doe123"
                    }
                """;

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenInvalidPassword_thereIsNoNumber_thenReturns400() throws Exception {
        String jsonRequest = """
                     {
                        "email": "test@gmail.com",
                        "password": "passwordNoNumber",
                        "username": "Doe123"
                    }
                """;

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenInvalidPassword_thereIsNoCapitalLetter_thenReturns400() throws Exception {
        String jsonRequest = """
                     {
                        "email": "test@gmail.com",
                        "password": "password1",
                        "username": "Doe123"
                    }
                """;

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }


    @Test
    void signUp_whenInvalidUsername_thenReturns400() throws Exception {
        String jsonRequest = """
                     {
                        "email": "test@gmail.com",
                        "password": "password123",
                        "username": "Do1"
                    }
                """;

        mvc.perform(post(ApiPaths.SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

}
