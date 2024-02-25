package com.aptiv.trainig_tracker.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
@Component
public class Utils {
    private final Random random= new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz";


    private String generatedRandomString(int l){
        StringBuilder returnedVAlue= new StringBuilder();

        for (int i=0; i<l; i++){
            returnedVAlue.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return new String(returnedVAlue);
    }

    public String getGeneratedId(int l){
        return generatedRandomString(l);
    }
}
