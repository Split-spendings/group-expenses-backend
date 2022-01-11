package com.splitspendings.groupexpensesbackend.util;

import java.util.Random;

public class RandomInviteCodeUtil {
    private static final int INVITE_CODE_LENGTH = 8;
    private static final int ALPHABET_LENGTH = 26;
    private static final int NUMBERS_LENGTH = 10;

    private static final Random random = new Random();

    public static String generateInviteCode(){
        StringBuilder sb = new StringBuilder(INVITE_CODE_LENGTH);

        for (int i = 0; i < INVITE_CODE_LENGTH; i++){
            sb.append(generateRandomCharacter());
        }

        return sb.toString();
    }

    private static char generateRandomCharacter(){
        switch (random.nextInt(3)){
            case 0:
                return (char) (random.nextInt(NUMBERS_LENGTH) + '0');
            case 1:
                return (char) (random.nextInt(ALPHABET_LENGTH) + 'a');
            default:
                return (char) (random.nextInt(ALPHABET_LENGTH) + 'A');
        }
    }
}