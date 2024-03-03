package com.poly.common;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNumber {

    public int randomNumber() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int value = rand.nextInt(100000, 999999);
        return value;
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

}
