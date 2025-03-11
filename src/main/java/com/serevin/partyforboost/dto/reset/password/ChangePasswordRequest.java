package com.serevin.partyforboost.dto.reset.password;

import com.serevin.partyforboost.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(@Email String email,
                                    @NotBlank String code,
                                    @ValidPassword String password) {}