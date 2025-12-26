package com.search.benchmark.algorithms;

import com.search.benchmark.stats.AlgorithmStats;

public interface StringSearchAlgorithm {
    AlgorithmStats search(String text, String pattern);
    String getName();
}