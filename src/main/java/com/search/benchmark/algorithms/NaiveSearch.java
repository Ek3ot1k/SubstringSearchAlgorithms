package com.search.benchmark.algorithms;

import com.search.benchmark.stats.AlgorithmStats;

public class NaiveSearch implements StringSearchAlgorithm {

    @Override
    public AlgorithmStats search(String text, String pattern) {
        AlgorithmStats stats = new AlgorithmStats(getName());
        long startTime = System.nanoTime();

        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {
            boolean match = true;
            for (int j = 0; j < m; j++) {
                stats.incrementCharChecks();
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                stats.addPosition(i);
            }
        }

        stats.setTimeNanos(System.nanoTime() - startTime);
        return stats;
    }

    @Override
    public String getName() {
        return "Наивный алгоритм";
    }
}