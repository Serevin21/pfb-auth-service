package com.serevin.partyforboost.event;

public class EmailActivationEvent extends EmailCodeEvent {
    public EmailActivationEvent(Object source, String email, String code) {
        super(source, email, code);
    }
}
