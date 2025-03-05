package com.serevin.partyforboost.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ActivationRequest(@Email String email, @NotBlank String code) {
}
