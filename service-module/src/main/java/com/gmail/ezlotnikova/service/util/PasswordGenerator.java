package com.gmail.ezlotnikova.service.util;

import java.util.Random;

import org.springframework.stereotype.Component;

import static com.gmail.ezlotnikova.service.util.PasswordGeneratorConstant.GENERATED_PASSWORD_LENGTH;

@Component
public class PasswordGenerator {

    public String generatePassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(GENERATED_PASSWORD_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}