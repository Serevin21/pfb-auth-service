package com.serevin.partyforboost.service;

import com.serevin.partyforboost.event.EmailActivationEvent;
import com.serevin.partyforboost.event.EmailResetPasswordEvent;

public interface EmailService {

    void sendConfirmationEmail(EmailActivationEvent event);
    void sendResetPasswordEmail(EmailResetPasswordEvent event);

}
