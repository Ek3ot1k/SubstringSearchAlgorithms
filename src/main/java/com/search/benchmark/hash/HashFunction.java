package com.search.benchmark.hash;

public interface HashFunction {
    long hash(String s);
    long update(long oldHash, char oldChar, char newChar, int patternLength);
    String getName();
}