package com.search.benchmark.stats;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmStats {
    private final String algorithmName;
    private long timeNanos;
    private long charChecks;
    private long collisions;
    private final List<Integer> positions;

    public AlgorithmStats(String algorithmName) {
        this.algorithmName = algorithmName;
        this.positions = new ArrayList<>();
    }

    public void incrementCharChecks() {
        this.charChecks++;
    }

    public void incrementCollisions() {
        this.collisions++;
    }

    public void addPosition(int position) {
        this.positions.add(position);
    }

    public void setTimeNanos(long timeNanos) {
        this.timeNanos = timeNanos;
    }

    // Геттеры
    public String getAlgorithmName() { return algorithmName; }
    public long getTimeNanos() { return timeNanos; }
    public double getTimeMillis() { return timeNanos / 1_000_000.0; }
    public long getCharChecks() { return charChecks; }
    public long getCollisions() { return collisions; }
    public List<Integer> getPositions() { return positions; }
    public int getFoundCount() { return positions.size(); }

    public void printStats() {
        System.out.printf("\n=== %s ===\n", algorithmName);
        System.out.printf("Время: %.6f мс\n", getTimeMillis());
        System.out.printf("Проверок символов: %,d\n", charChecks);
        if (collisions > 0) {
            System.out.printf("Коллизии: %,d\n", collisions);
        }
        System.out.printf("Найдено вхождений: %d\n", getFoundCount());
    }
}