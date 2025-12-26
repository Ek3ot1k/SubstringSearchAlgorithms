package com.search.benchmark.hash;

public class XORHash implements HashFunction {
    private final String name = "XORHash";

    @Override
    public long hash(String s) {
        long h = 0;
        for (int i = 0; i < s.length(); i++) {
            h = (h ^ s.charAt(i)) & 0xFFFFFFFFL;
        }
        return h;
    }

    @Override
    public long update(long oldHash, char oldChar, char newChar, int patternLength) {
        return oldHash ^ oldChar ^ newChar;
    }

    @Override
    public String getName() {
        return name;
    }
}