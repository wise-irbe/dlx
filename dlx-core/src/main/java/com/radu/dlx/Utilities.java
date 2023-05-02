package com.radu.dlx;

public final class Utilities {
    private static final int BASE_10 = 10;
    private static final int SMALL_A_CODE = 0x61;
    private static final int LARGE_A_CODE = 0x41;
    private static final int ALPHABET_SIZE = 26;

    private Utilities() {
    }

    public static String decode(int code) {
        if (code < BASE_10) {
            return Integer.toString(code);
        } else if (code < BASE_10 + ALPHABET_SIZE) {//10  - 10 + 0x61 = a
            return Character.toString((char) (code + SMALL_A_CODE - BASE_10));
        } else if (code < BASE_10 + 2 * ALPHABET_SIZE) {// 36 - 26 - 10 + 0x41 = A
            return Character.toString((char) (code + LARGE_A_CODE - BASE_10 - ALPHABET_SIZE));
        } else {
            return "(" + code + ")";
        }
    }

    public static int encode(String code) {
        if (code.length() == 1) {
            char chr = code.charAt(0);
            if (chr >= '0' && chr <= '9') {
                return Integer.valueOf(code);
            } else if (chr >= SMALL_A_CODE && chr < SMALL_A_CODE + ALPHABET_SIZE) {
                return chr - SMALL_A_CODE + BASE_10;
            } else if (chr >= LARGE_A_CODE && chr < LARGE_A_CODE + ALPHABET_SIZE) {
                return chr - LARGE_A_CODE + BASE_10 + ALPHABET_SIZE;
            }
        } else if (code.length() > 2
                && code.charAt(0) == '('
                && code.charAt(code.length() - 1) == ')') {
            try {
                return Integer.valueOf(code.substring(1, code.length() - 1));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        }
        throw new IllegalArgumentException();
    }
}
