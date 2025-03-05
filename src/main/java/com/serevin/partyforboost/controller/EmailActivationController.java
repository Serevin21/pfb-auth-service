package com.serevin.partyforboost.controller;

import com.serevin.partyforboost.dto.email.ActivationRequest;
import com.serevin.partyforboost.dto.email.SendActivationEmailRequest;
import com.serevin.partyforboost.service.EmailActivationService;
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
public class EmailActivationController {

    private final EmailActivationService emailActivationService;

    @PostMapping(ApiPaths.EMAIL_ACTIVATION_SEND_EMAIL)
    public ResponseEntity<?> resend(@Valid @RequestBody SendActivationEmailRequest request) {
        emailActivationService.sendEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiPaths.EMAIL_ACTIVATION_ACTIVATE)
    public ResponseEntity<?> activate(@Valid @RequestBody ActivationRequest request) {
        emailActivationService.activate(request);
        return ResponseEntity.ok().build();
    }

}
