package com.serevin.patyforboost.dto.email;

import jakarta.validation.constraints.Email;

public record SendActivationEmailRequest(@Email String email) {
}
