package com.serevin.partyforboost.dto.reset.password;

import jakarta.validation.constraints.Email;

public record ResetPasswordRequest(@Email String email) {
}
