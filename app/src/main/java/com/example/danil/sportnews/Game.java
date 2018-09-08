package com.example.danil.sportnews;

public class Game {
    private String title; // время
    private String coefficient;  // коэффициент
    private String time; // время
    private String place;  // место
    private String preview; // превью
    private String article;  // ссылка на статью

    public Game(String title, String coefficient, String time, String place, String preview, String article){

        this.title=title;
        this.coefficient=coefficient;
        this.time=time;
        this.title=title;
        this.place=place;
        this.preview=preview;
        this.article=article;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoefficient() {
        return this.coefficient;
    }

    public void setCoefficient(String coefficient) {
        this.coefficient = coefficient;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPreview() {
        return this.preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getArticle() {
        return this.article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
