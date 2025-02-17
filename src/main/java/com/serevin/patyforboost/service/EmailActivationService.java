package com.serevin.patyforboost.service;

import com.serevin.patyforboost.dto.email.ActivationRequest;
import com.serevin.patyforboost.dto.email.SendActivationEmailRequest;

public interface EmailActivationService {

    void sendEmail(SendActivationEmailRequest request);

    void activate(ActivationRequest request);

}
