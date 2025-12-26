package com.search.benchmark.algorithms;

import com.search.benchmark.stats.AlgorithmStats;

public class KMPSearch implements StringSearchAlgorithm {

    @Override
    public AlgorithmStats search(String text, String pattern) {
        AlgorithmStats stats = new AlgorithmStats(getName());
        long startTime = System.nanoTime();

        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            stats.setTimeNanos(System.nanoTime() - startTime);
            return stats;
        }

        int[] lps = computeLPSArray(pattern);

        int i = 0;
        int j = 0;

        while (i < n) {
            stats.incrementCharChecks();

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                stats.addPosition(i - j);
                j = lps[j - 1];
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        stats.setTimeNanos(System.nanoTime() - startTime);
        return stats;
    }

    private int[] computeLPSArray(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = len;
                    i++;
                }
            }
        }

        return lps;
    }

    @Override
    public String getName() {
        return "Кнут-Моррис-Пратт";
    }
}