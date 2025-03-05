package com.serevin.partyforboost.event;

public class EmailResetPasswordEvent  extends EmailCodeEvent{
    public EmailResetPasswordEvent(Object source, String email, String code) {
        super(source, email, code);
    }
}
