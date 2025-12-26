package com.search.benchmark.hash;

public class SimpleHash implements HashFunction {
    private final long base = 256;
    private final long mod = 1_000_000_007L;
    private final String name = "SimpleHash (base=256)";

    @Override
    public long hash(String s) {
        long h = 0;
        for (int i = 0; i < s.length(); i++) {
            h = (h * base + s.charAt(i)) % mod;
        }
        return h;
    }

    @Override
    public long update(long oldHash, char oldChar, char newChar, int patternLength) {
        long power = 1;
        for (int i = 0; i < patternLength - 1; i++) {
            power = (power * base) % mod;
        }

        long newHash = (oldHash - oldChar * power % mod + mod) % mod;
        newHash = (newHash * base + newChar) % mod;
        return newHash;
    }

    @Override
    public String getName() {
        return name;
    }
}