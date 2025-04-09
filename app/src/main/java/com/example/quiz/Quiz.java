package com.example.quiz;
public class Quiz {
    private int id;
    private String title;
    private int score;
    private boolean isPremium;
    private String keywords;


    public Quiz(int id, String title, int score, boolean isPremium, String keywords) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.isPremium = isPremium;
        this.keywords = keywords;
    }

    public int getId() {
        return id;
    }
    public String getKeywords() {
        return keywords;
    }
    public String getTitle() {
        return title;
    }
    public int getScore() {
        return score;
    }
    public boolean isPremium() {
        return isPremium;
    }
}
