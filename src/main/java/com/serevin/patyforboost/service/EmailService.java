package com.serevin.patyforboost.service;

import com.serevin.patyforboost.event.EmailActivationEvent;

public interface EmailService {

    void sendConfirmationEmail(EmailActivationEvent event)

}
