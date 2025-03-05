package com.serevin.partyforboost.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class  EmailCodeEvent extends ApplicationEvent {

    private final String email;
    private final String code;

    public EmailCodeEvent(Object source, String email, String code) {
        super(source);
        this.email = email;
        this.code = code;
    }
}
