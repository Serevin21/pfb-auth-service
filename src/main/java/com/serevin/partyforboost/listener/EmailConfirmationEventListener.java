package com.serevin.partyforboost.listener;

import com.serevin.partyforboost.event.EmailActivationEvent;
import com.serevin.partyforboost.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConfirmationEventListener {

    private final EmailService emailService;

    @Async
    @EventListener(EmailActivationEvent.class)
    public void handleEmailConfirmationEventListener(EmailActivationEvent event) {
        log.info("Email confirmation event: {}", event.getEmail());
        emailService.sendConfirmationEmail(event);
    }

}
