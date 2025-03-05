package com.serevin.partyforboost.controller;

import com.serevin.partyforboost.dto.reset.password.ChangePasswordRequest;
import com.serevin.partyforboost.dto.reset.password.ResetPasswordRequest;
import com.serevin.partyforboost.dto.reset.password.ValidateCodeRequest;
import com.serevin.partyforboost.service.ResetPasswordService;
import com.serevin.partyforboost.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping(ApiPaths.RESET_PASSWORD_SEND_EMAIL)
    public ResponseEntity<?> sendEmail(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiPaths.RESET_PASSWORD_VALIDATE_CODE)
    public ResponseEntity<?> validateCode(@Valid @RequestBody ValidateCodeRequest request) {
        resetPasswordService.validateCode(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiPaths.RESET_PASSWORD_CHANGE)
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        resetPasswordService.changePassword(request);
        return ResponseEntity.ok().build();
    }

}
