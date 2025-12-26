package com.search.benchmark.generators;

public class TestCase {
    private final String name;
    private final String text;
    private final String pattern;

    public TestCase(String name, String text, String pattern) {
        this.name = name;
        this.text = text;
        this.pattern = pattern;
    }

    // Геттеры
    public String getName() { return name; }
    public String getText() { return text; }
    public String getPattern() { return pattern; }

    @Override
    public String toString() {
        return String.format("%s: текст=%,d символов, паттерн='%s'",
                name, text.length(), pattern);
    }
}