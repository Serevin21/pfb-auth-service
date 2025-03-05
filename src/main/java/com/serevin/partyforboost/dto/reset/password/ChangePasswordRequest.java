package com.serevin.partyforboost.dto.reset.password;

import com.serevin.partyforboost.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(@Email String email,
                                    @NotBlank String code,
                                    @NotBlank @ValidPassword String password) {}