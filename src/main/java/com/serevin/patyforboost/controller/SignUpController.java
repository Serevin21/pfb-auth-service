package com.serevin.patyforboost.controller;

import com.serevin.patyforboost.dto.signup.SignUpRequest;
import com.serevin.patyforboost.service.SignUpService;
import com.serevin.patyforboost.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.SIGN_UP)
@RequiredArgsConstructor
@Validated
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request){
        signUpService.signUp(request);
        return ResponseEntity.ok().build();
    }

}
