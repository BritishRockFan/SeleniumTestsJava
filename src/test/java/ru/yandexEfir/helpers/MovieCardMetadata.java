package ru.yandexEfir.helpers;

public class MovieCardMetadata {
    private String year;
    private String rating;

    public MovieCardMetadata(String year, String rating) {
        this.year = year;
        this.rating = rating;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
