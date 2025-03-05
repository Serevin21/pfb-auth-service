package com.serevin.partyforboost.listener;

import com.serevin.partyforboost.event.EmailResetPasswordEvent;
import com.serevin.partyforboost.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailResetPasswordEventListener {

    private final EmailService emailService;

    @Async
    @EventListener(EmailResetPasswordEvent.class)
    public void handleEmailResetPasswordEventListener(EmailResetPasswordEvent event) {
        log.info("Email reset password event: {}", event.getEmail());
        emailService.sendResetPasswordEmail(event);
    }

}
