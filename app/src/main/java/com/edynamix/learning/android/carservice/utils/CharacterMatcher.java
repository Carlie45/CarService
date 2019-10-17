package com.edynamix.learning.android.carservice.utils;

public class CharacterMatcher {

    private static final char[] SPECIAL_CHARACTERS = {'~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
            '_', '-', '+', '=', '[', ']', '{', '}', ';', ':', '\'', '\"', '\\', '<', '>', ',', '.', '?', '/'};

    static boolean isALetter(char c) {
        if (c >= 'a' && c <= 'z') {
            return true;
        } else if (c > 'A' && c < 'Z') {
            return true;
        }
        return false;
    }

    static boolean isADigit(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    static boolean isASpecialCharacter(char c) {
        for (char currentSpecialChar : SPECIAL_CHARACTERS) {
            if (currentSpecialChar == c) {
                return true;
            }
        }

        return false;
    }

}
