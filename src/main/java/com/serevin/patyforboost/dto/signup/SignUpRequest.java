package com.serevin.patyforboost.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(@Email @NotBlank String email,
                            @NotBlank @Size(min = 5) String username,
                            @Size(min = 8) String password) {
}
