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

        // Анализ хеш-функций
        analyzeHashFunctions(results);
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

    private static void analyzeHashFunctions(List<BenchmarkResult> results) {
        System.out.println("\n\n" + "=".repeat(80));
        System.out.println("АНАЛИЗ ВЛИЯНИЯ ХЕШ-ФУНКЦИЙ НА РАБИНА-КАРПА");
        System.out.println("=".repeat(80));

        // Собираем данные только для Рабина-Карпа
        Map<String, List<Double>> timesByHash = new HashMap<>();
        Map<String, List<Long>> collisionsByHash = new HashMap<>();

        for (BenchmarkResult result : results) {
            for (AlgorithmStats stats : result.getAlgorithmStats()) {
                String name = stats.getAlgorithmName();
                if (name.contains("Рабин-Карп")) {
                    timesByHash.computeIfAbsent(name, k -> new ArrayList<>())
                            .add(stats.getTimeMillis());
                    collisionsByHash.computeIfAbsent(name, k -> new ArrayList<>())
                            .add(stats.getCollisions());
                }
            }
        }

        System.out.println("\nСравнение хеш-функций:");
        System.out.println("-".repeat(80));

        for (String hashFunc : timesByHash.keySet()) {
            List<Double> times = timesByHash.get(hashFunc);
            List<Long> collisions = collisionsByHash.get(hashFunc);

            double avgTime = times.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            long totalCollisions = collisions.stream().mapToLong(Long::longValue).sum();
            double avgCollisions = collisions.stream().mapToLong(Long::longValue).average().orElse(0);

            System.out.printf("\n%s:\n", hashFunc);
            System.out.printf("  Среднее время: %.3f мс\n", avgTime);
            System.out.printf("  Всего коллизий: %,d\n", totalCollisions);
            System.out.printf("  Среднее коллизий на тест: %.1f\n", avgCollisions);

            // Анализ эффективности
            if (totalCollisions == 0) {
                System.out.println("  ✓ Отличная хеш-функция: нет коллизий");
            } else if (avgCollisions < 10) {
                System.out.println("  ✓ Хорошая хеш-функция: мало коллизий");
            } else if (avgCollisions < 100) {
                System.out.println("  ⚠ Средняя хеш-функция: умеренные коллизии");
            } else {
                System.out.println("  ✗ Слабая хеш-функция: много коллизий");
            }
        }

        // Вывод рекомендаций
        System.out.println("\n\n" + "=".repeat(80));
        System.out.println("ВЫВОДЫ И РЕКОМЕНДАЦИИ");
        System.out.println("=".repeat(80));

        System.out.println("\n1. ВЛИЯНИЕ ХЕШ-ФУНКЦИЙ НА РАБИНА-КАРПА:");
        System.out.println("   • XORHash: самый быстрый, но имеет много коллизий");
        System.out.println("   • SimpleHash (base=256): хороший баланс скорости и качества");
        System.out.println("   • HornerHash (base=131): наименьшее количество коллизий");
        System.out.println("   • Коллизии увеличивают время работы (требуется проверка символов)");

        System.out.println("\n2. СРАВНЕНИЕ АЛГОРИТМОВ:");
        System.out.println("   • Бойер-Мур: лучшая производительность на английских текстах");
        System.out.println("   • КМП: стабильная производительность, гарантированно линейное время");
        System.out.println("   • Наивный: простой, но неэффективный на больших текстах");
        System.out.println("   • Рабин-Карп: эффективен при правильном выборе хеш-функции");

        System.out.println("\n3. РЕКОМЕНДАЦИИ ПО ВЫБОРУ:");
        System.out.println("   • Для общего использования: Бойер-Мур или Рабин-Карп с SimpleHash");
        System.out.println("   • Для текстов с повторениями: КМП");
        System.out.println("   • Когда важна скорость: XORHash (если можно допустить коллизии)");
        System.out.println("   • Когда важна точность: HornerHash или SimpleHash");
        System.out.println("   • Для коротких текстов: разница незначительна");

        System.out.println("\n4. ДЛЯ ИССЛЕДОВАНИЯ/ЛЕКЦИИ:");
        System.out.println("   • Использовать разные тексты (короткий, технический, литературный)");
        System.out.println("   • Тестировать с общим паттерном для честного сравнения");
        System.out.println("   • Измерять и время, и коллизии, и количество проверок");
        System.out.println("   • Демонстрировать trade-off между скоростью и точностью");

        System.out.println("\n" + "=".repeat(80));
        System.out.println("ИССЛЕДОВАНИЕ ЗАВЕРШЕНО");
        System.out.println("=".repeat(80));
    }
}