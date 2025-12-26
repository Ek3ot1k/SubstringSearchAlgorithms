package com.search.benchmark.hash;

public class DJB2Hash implements HashFunction {
    private final String name = "DJB2Hash";

    @Override
    public long hash(String s) {
        long h = 5381;
        for (int i = 0; i < s.length(); i++) {
            h = ((h << 5) + h) + s.charAt(i);
        }
        return h & 0xFFFFFFFFL;
    }

    @Override
    public long update(long oldHash, char oldChar, char newChar, int patternLength) {
        throw new UnsupportedOperationException(
                "DJB2 не поддерживает rolling update. Нужно пересчитывать полностью."
        );
    }

    @Override
    public String getName() {
        return name;
    }
}