package com.search.benchmark.hash;

public class HornerHash implements HashFunction {
    private final long base = 131;
    private final long mod = 2147483647L; // 2^31-1
    private final String name = "HornerHash (base=131)";

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