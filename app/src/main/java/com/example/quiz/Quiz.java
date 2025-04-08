package com.example.quiz;
public class Quiz {
    private int id;
    private String title;
    private int score;
    private boolean isPremium;

    public Quiz(int id, String title, int score, boolean isPremium) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.isPremium = isPremium;
    }

    public int getId() {
        return id;
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
