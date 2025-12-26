package com.search.benchmark.generators;

import java.util.Random;

public class TextGenerator {
    private static final Random RANDOM = new Random(42);

    public static String generateRandomText(int length, String alphabet) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(alphabet.length());
            sb.append(alphabet.charAt(index));
        }
        return sb.toString();
    }

    public static String generateTextWithPatterns(
            int length,
            String mainPattern,
            double patternFrequency) {
        StringBuilder sb = new StringBuilder(length);
        int patternLength = mainPattern.length();

        for (int i = 0; i < length; i++) {
            if (RANDOM.nextDouble() < patternFrequency && i + patternLength <= length) {
                sb.append(mainPattern);
                i += patternLength - 1;
            } else {
                sb.append((char) ('a' + RANDOM.nextInt(26)));
            }
        }

        // Обрезаем до нужной длины
        return sb.length() > length ? sb.substring(0, length) : sb.toString();
    }

    public static String generateRepeatedText(String pattern, int repetitions) {
        return pattern.repeat(repetitions);
    }

    public static String getRussianAlphabet() {
        StringBuilder sb = new StringBuilder();
        for (char c = 'а'; c <= 'я'; c++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String getEnglishAlphabet() {
        StringBuilder sb = new StringBuilder();
        for (char c = 'a'; c <= 'z'; c++) {
            sb.append(c);
        }
        return sb.toString();
    }
}