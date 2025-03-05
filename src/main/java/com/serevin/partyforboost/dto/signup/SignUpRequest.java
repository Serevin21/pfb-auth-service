package com.serevin.partyforboost.dto.signup;

import com.serevin.partyforboost.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(@Email @NotBlank String email,
                            @NotBlank @Size(min = 5) String username,
                            @ValidPassword String password) {
}
