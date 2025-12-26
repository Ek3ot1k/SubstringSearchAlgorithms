package com.search.benchmark.stats;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkResult {
    private final String testCaseName;
    private final String text;
    private final String pattern;
    private final List<AlgorithmStats> algorithmStats;

    public BenchmarkResult(String testCaseName, String text, String pattern) {
        this.testCaseName = testCaseName;
        this.text = text;
        this.pattern = pattern;
        this.algorithmStats = new ArrayList<>();
    }

    public void addAlgorithmStats(AlgorithmStats stats) {
        algorithmStats.add(stats);
    }

    public void printSummary() {
        System.out.println("\n" + "=".repeat(70));
        System.out.printf("ТЕСТ: %s\n", testCaseName);
        System.out.printf("Текст: %,d символов, Паттерн: '%s'\n", text.length(), pattern);
        System.out.println("-".repeat(70));

        System.out.printf("%-30s %-12s %-15s %-12s %-10s\n",
                "Алгоритм", "Время(мс)", "Проверок", "Коллизии", "Найдено");
        System.out.println("-".repeat(70));

        for (AlgorithmStats stats : algorithmStats) {
            System.out.printf("%-30s %-12.3f %-,15d %-,12d %-10d\n",
                    stats.getAlgorithmName(),
                    stats.getTimeMillis(),
                    stats.getCharChecks(),
                    stats.getCollisions(),
                    stats.getFoundCount());
        }
    }

    // Геттеры
    public String getTestCaseName() { return testCaseName; }
    public String getText() { return text; }
    public String getPattern() { return pattern; }
    public List<AlgorithmStats> getAlgorithmStats() { return algorithmStats; }
}