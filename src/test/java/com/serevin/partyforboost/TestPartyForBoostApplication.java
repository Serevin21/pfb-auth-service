package com.serevin.partyforboost;

import org.springframework.boot.SpringApplication;

public class TestPartyForBoostApplication {

    public static void main(String[] args) {
        SpringApplication.from(PartyForBoostApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
