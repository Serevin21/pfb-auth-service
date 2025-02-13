package com.serevin.patyforboost;

import org.springframework.boot.SpringApplication;

public class TestPatyForBoostApplication {

    public static void main(String[] args) {
        SpringApplication.from(PartyForBoostApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
