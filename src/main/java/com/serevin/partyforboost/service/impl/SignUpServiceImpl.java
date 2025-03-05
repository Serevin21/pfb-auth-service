package com.serevin.partyforboost.service.impl;

import com.serevin.partyforboost.dto.email.SendActivationEmailRequest;
import com.serevin.partyforboost.dto.signup.SignUpRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.exception.UserAlreadyExistsException;
import com.serevin.partyforboost.service.EmailActivationService;
import com.serevin.partyforboost.service.SignUpService;
import com.serevin.partyforboost.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailActivationService emailActivationService;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.email();
        String username = signUpRequest.username();

        Optional<User> userByEmail = userService.findByEmail(email);
        if (userByEmail.isPresent()) {
            throw new UserAlreadyExistsException("User is already registered by email: %s".formatted(email));
        }
        Optional<User> userByUsername = userService.findByUsername(username);
        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException("User is already registered by username: %s".formatted(username));
        }
        User user = User.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(signUpRequest.password()))
                .build();
        userService.save(user);

        emailActivationService.sendEmail(new SendActivationEmailRequest(email));
    }
}
