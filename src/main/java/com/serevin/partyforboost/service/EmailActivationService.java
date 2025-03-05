package com.serevin.partyforboost.service;

import com.serevin.partyforboost.dto.email.ActivationRequest;
import com.serevin.partyforboost.dto.email.SendActivationEmailRequest;

public interface EmailActivationService {

    void sendEmail(SendActivationEmailRequest request);

    void activate(ActivationRequest request);

}
