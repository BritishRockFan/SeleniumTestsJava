package ru.yandexEfir.helpers;

public class SearchResults {
    public String name;
    public String meta;

    public static int resultsCount;

    public SearchResults(String name, String meta) {
        this.name = name;
        this.meta = meta;
        resultsCount++;
    }

    @Override
    public String toString() {
        return "SearchResults{" +
                "name='" + name + '\'' +
                ", meta='" + meta + '\'' +
                '}';
    }
}
