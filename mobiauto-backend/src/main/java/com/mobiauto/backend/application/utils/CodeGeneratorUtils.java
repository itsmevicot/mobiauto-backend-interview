package com.mobiauto.backend.application.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class CodeGeneratorUtils {

    private static final Random RANDOM = new SecureRandom();

    public String generateUsuarioCodigo() {
        return "USU" + generateRandomNumber();
    }

    public String generateRevendaCodigo() {
        return "REV" + generateRandomNumber();
    }

    public String generateVeiculoCodigo() {
        return "VEI" + generateRandomNumber();
    }

    public String generateOportunidadeCodigo() {
        return "OPO" + generateRandomNumber();
    }

    private String generateRandomNumber() {
        int randomNumber = RANDOM.nextInt(9000000) + 1000000;
        return String.valueOf(randomNumber);
    }
}

