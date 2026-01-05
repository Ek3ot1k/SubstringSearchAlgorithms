package com.search.benchmark;

import com.search.benchmark.algorithms.*;
import com.search.benchmark.hash.*;
import com.search.benchmark.generators.*;
import com.search.benchmark.stats.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Main {

    // Метод для чтения текста из файла
    private static String readTextFromFile(String filename) {
        try {
            // Путь к файлу в папке texts
            Path filePath = Paths.get("texts", filename);
            if (Files.exists(filePath)) {
                return new String(Files.readAllBytes(filePath), "UTF-8");
            }

            // Если не найден, попробуем найти в resources
            InputStream inputStream = Main.class.getClassLoader()
                    .getResourceAsStream("texts/" + filename);
            if (inputStream != null) {
                return new String(inputStream.readAllBytes(), "UTF-8");
            }

            System.err.println("Файл не найден: texts/" + filename);
            return "ТЕКСТ НЕ ЗАГРУЖЕН. Файл " + filename + " не найден.\n" +
                    "Пожалуйста, создайте файл в папке texts/";

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла " + filename + ": " + e.getMessage());
            return "ОШИБКА ЧТЕНИЯ ФАЙЛА: " + filename;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== ИССЛЕДОВАНИЕ АЛГОРИТМОВ ПОИСКА ПОДСТРОК ===\n");
        System.out.println("Анализ влияния хеш-функций на алгоритм Рабина-Карпа\n");

        // Создаем тестовые случаи из файлов
        List<TestCase> testCases = createTestCasesFromFiles();

        // Создаем алгоритмы для тестирования
        List<StringSearchAlgorithm> algorithms = createAlgorithms();

        // Запускаем тесты
        List<BenchmarkResult> results = new ArrayList<>();

        for (TestCase testCase : testCases) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("ТЕСТ: " + testCase);
            System.out.println("=".repeat(80));

            BenchmarkResult benchmarkResult = new BenchmarkResult(
                    testCase.getName(),
                    testCase.getText(),
                    testCase.getPattern()
            );

            // Запускаем каждый алгоритм
            for (StringSearchAlgorithm algorithm : algorithms) {
                System.out.print("  Запуск " + algorithm.getName() + "... ");

                AlgorithmStats stats = algorithm.search(
                        testCase.getText(),
                        testCase.getPattern()
                );

                benchmarkResult.addAlgorithmStats(stats);
                System.out.println("✓");
            }

            results.add(benchmarkResult);
            benchmarkResult.printSummary();

            // Дополнительная информация о тексте
            System.out.println("\nДополнительная информация:");
            System.out.println("-".repeat(40));
            System.out.println("Длина текста: " + testCase.getText().length() + " символов");
            System.out.println("Паттерн: '" + testCase.getPattern() + "' (длина: " + testCase.getPattern().length() + ")");

            // Подсчет вхождений паттерна в тексте (примерно)
            int occurrences = countOccurrences(testCase.getText(), testCase.getPattern());
            System.out.println("Примерное количество вхождений: " + occurrences);
        }

        // Выводим общую сводку
        printOverallSummary(results);
    }

    private static List<TestCase> createTestCasesFromFiles() {
        List<TestCase> testCases = new ArrayList<>();

        // Загружаем тексты из файлов
        System.out.println("Загрузка текстов из файлов...\n");

        String text1 = readTextFromFile("programming.txt");
        String text2 = readTextFromFile("lotr_two_towers.txt");
        String text3 = readTextFromFile("java_algorithms.txt");
        String text4 = readTextFromFile("shawshank.txt");

        // ОБЩИЙ ПАТТЕРН ДЛЯ ВСЕХ ТЕСТОВ
        String COMMON_PATTERN = "algorithm";

        // Если паттерн слишком длинный для короткого текста, используем подстроку
        if (text1.length() < COMMON_PATTERN.length()) {
            COMMON_PATTERN = "program";
        }

        // Тест 1: Текст о программисте (~500 символов)
        testCases.add(new TestCase(
                "1. Текст о программисте (короткий)",
                text1,
                COMMON_PATTERN
        ));

        // Тест 2: Властелин колец
        testCases.add(new TestCase(
                "2. Властелин колец: Две крепости (литература)",
                text2,
                COMMON_PATTERN
        ));

        // Тест 3: Технический текст про Java/алгоритмы
        testCases.add(new TestCase(
                "3. Технический текст: Java и алгоритмы (технический)",
                text3,
                COMMON_PATTERN
        ));

        // Тест 4: Побег из Шоушенка
        testCases.add(new TestCase(
                "4. Побег из Шоушенка (длинный литературный)",
                text4,
                COMMON_PATTERN
        ));

        // Выводим информацию о загруженных текстах
        System.out.println("Успешно загружено " + testCases.size() + " текстов:");
        for (TestCase tc : testCases) {
            System.out.printf("  • %-40s %,7d символов\n",
                    tc.getName(), tc.getText().length());
        }
        System.out.println("\nОбщий паттерн для всех тестов: '" + COMMON_PATTERN + "'");
        System.out.println("-".repeat(60));

        return testCases;
    }

    private static int countOccurrences(String text, String pattern) {
        // Простой подсчет вхождений (без учета регистра)
        String lowerText = text.toLowerCase();
        String lowerPattern = pattern.toLowerCase();
        int count = 0;
        int index = 0;

        while ((index = lowerText.indexOf(lowerPattern, index)) != -1) {
            count++;
            index += pattern.length();
        }

        return count;
    }

    private static List<StringSearchAlgorithm> createAlgorithms() {
        List<StringSearchAlgorithm> algorithms = new ArrayList<>();

        // Наивный алгоритм (базовое сравнение)
        algorithms.add(new NaiveSearch());

        // Рабин-Карп с разными хеш-функциями (основное исследование)
        algorithms.add(new RabinKarpSearch(new SimpleHash()));
        algorithms.add(new RabinKarpSearch(new HornerHash()));
        algorithms.add(new RabinKarpSearch(new XORHash()));

        // Другие алгоритмы для сравнения
        algorithms.add(new BoyerMooreSearch());
        algorithms.add(new KMPSearch());

        return algorithms;
    }

    private static void printOverallSummary(List<BenchmarkResult> results) {
        System.out.println("\n\n" + "=".repeat(80));
        System.out.println("ОБЩАЯ СВОДКА РЕЗУЛЬТАТОВ");
        System.out.println("=".repeat(80));

        Map<String, Double> totalTime = new HashMap<>();
        Map<String, Long> totalChecks = new HashMap<>();
        Map<String, Long> totalCollisions = new HashMap<>();
        Map<String, Integer> testCount = new HashMap<>();

        // Собираем статистику
        for (BenchmarkResult result : results) {
            for (AlgorithmStats stats : result.getAlgorithmStats()) {
                String name = stats.getAlgorithmName();

                totalTime.put(name,
                        totalTime.getOrDefault(name, 0.0) + stats.getTimeMillis());
                totalChecks.put(name,
                        totalChecks.getOrDefault(name, 0L) + stats.getCharChecks());
                totalCollisions.put(name,
                        totalCollisions.getOrDefault(name, 0L) + stats.getCollisions());
                testCount.put(name, testCount.getOrDefault(name, 0) + 1);
            }
        }

        // Выводим таблицу
        System.out.printf("\n%-40s %-12s %-15s %-12s\n",
                "АЛГОРИТМ / ХЕШ-ФУНКЦИЯ", "ВРЕМЯ (мс)", "ПРОВЕРОК", "КОЛЛИЗИЙ");
        System.out.println("-".repeat(85));

        // Сортируем по времени (от быстрого к медленному)
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(totalTime.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());

        for (Map.Entry<String, Double> entry : sortedEntries) {
            String algorithm = entry.getKey();
            int count = testCount.get(algorithm);
            double avgTime = totalTime.get(algorithm) / count;
            long avgChecks = totalChecks.get(algorithm) / count;
            long avgCollisions = totalCollisions.get(algorithm) / count;

            System.out.printf("%-40s %-12.3f %-,15d %-,12d\n",
                    algorithm, avgTime, avgChecks, avgCollisions);
        }
    }


}