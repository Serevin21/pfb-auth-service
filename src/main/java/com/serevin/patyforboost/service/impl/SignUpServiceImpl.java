package com.serevin.patyforboost.service.impl;

import com.serevin.patyforboost.dto.signup.SignUpRequest;
import com.serevin.patyforboost.entity.User;
import com.serevin.patyforboost.service.SignUpService;
import com.serevin.patyforboost.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        User user = User.builder()
                .email(signUpRequest.email())
                .username(signUpRequest.username())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .build();
        userService.save(user);
    }
}
