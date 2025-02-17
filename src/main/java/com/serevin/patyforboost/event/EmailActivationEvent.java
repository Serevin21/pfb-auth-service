package com.serevin.patyforboost.event;

public class EmailActivationEvent extends EmailCodeEvent {
    public EmailActivationEvent(Object source, String email, String code) {
        super(source, email, code);
    }
}
