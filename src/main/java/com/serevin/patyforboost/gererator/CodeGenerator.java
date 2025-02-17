package com.serevin.patyforboost.gererator;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeGenerator {

    private static final Random RANDOM = new Random();

    public String generate() {
        return "" + RANDOM.nextInt(100000, 999999);
    }

}
