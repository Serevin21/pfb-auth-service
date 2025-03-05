package com.serevin.partyforboost.dto.reset.password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ValidateCodeRequest(@Email String email, @NotBlank(message = "Code should not be blank") String code) {}
