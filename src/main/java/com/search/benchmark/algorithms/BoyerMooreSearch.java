package com.search.benchmark.algorithms;

import com.search.benchmark.stats.AlgorithmStats;
import java.util.Arrays;

public class BoyerMooreSearch implements StringSearchAlgorithm {

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

        // Увеличиваем размер таблицы для поддержки Unicode
        int[] badChar = new int[65536];  // Для поддержки UTF-16
        Arrays.fill(badChar, -1);

        // Заполняем таблицу плохого символа
        for (int i = 0; i < m; i++) {
            badChar[pattern.charAt(i)] = i;
        }

        int s = 0; // сдвиг паттерна относительно текста
        while (s <= n - m) {
            int j = m - 1;

            // Сравниваем справа налево
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                stats.incrementCharChecks();
                j--;
            }

            if (j < 0) {
                // Найдено совпадение
                stats.addPosition(s);

                // Сдвигаем паттерн для следующего поиска
                s += (s + m < n) ? m - badChar[text.charAt(s + m)] : 1;
            } else {
                stats.incrementCharChecks(); // за последнюю неудачную проверку

                // Сдвигаем паттерн на максимальное из двух значений
                int shift = Math.max(1, j - badChar[text.charAt(s + j)]);
                s += shift;
            }
        }

        stats.setTimeNanos(System.nanoTime() - startTime);
        return stats;
    }

    @Override
    public String getName() {
        return "Бойер-Мур";
    }
}