package com.search.benchmark.algorithms;

import com.search.benchmark.hash.HashFunction;
import com.search.benchmark.stats.AlgorithmStats;

public class RabinKarpSearch implements StringSearchAlgorithm {
    private final HashFunction hashFunction;
    private final String name;

    public RabinKarpSearch(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
        this.name = "Рабин-Карп (" + hashFunction.getName() + ")";
    }

    @Override
    public AlgorithmStats search(String text, String pattern) {
        AlgorithmStats stats = new AlgorithmStats(name);
        long startTime = System.nanoTime();

        int n = text.length();
        int m = pattern.length();

        if (n < m) {
            stats.setTimeNanos(System.nanoTime() - startTime);
            return stats;
        }

        long patternHash = hashFunction.hash(pattern);
        long textHash = hashFunction.hash(text.substring(0, m));

        for (int i = 0; i <= n - m; i++) {
            stats.incrementCharChecks();

            if (textHash == patternHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    stats.incrementCharChecks();
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        stats.incrementCollisions();
                        match = false;
                        break;
                    }
                }
                if (match) {
                    stats.addPosition(i);
                }
            }

            if (i < n - m) {
                textHash = hashFunction.update(
                        textHash,
                        text.charAt(i),
                        text.charAt(i + m),
                        m
                );
            }
        }

        stats.setTimeNanos(System.nanoTime() - startTime);
        return stats;
    }

    @Override
    public String getName() {
        return name;
    }
}