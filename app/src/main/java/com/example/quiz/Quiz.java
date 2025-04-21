package com.example.quiz;
public class Quiz {
    private int id;
    private String title;
    private int score;
    private int nombreQuestion;
    private String premium;
    private String keywords;
    private String quizDescription;


    public Quiz(int id, String title, int score, String premium, String keywords, String quizDescription, int nombreQuestion) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.premium = premium;
        this.quizDescription = quizDescription;
        this.nombreQuestion = nombreQuestion;
        this.keywords = keywords;
    }

    public int getId() {
        return id;
    }
    public int nombreQuestion() {
        return nombreQuestion;
    }
    public String getKeywords() {
        return keywords;
    }
    public String quizDescription() {
        return quizDescription;
    }
    public String getTitle() {
        return title;
    }
    public int getScore() {
        return score;
    }
    public String premium() {
        return premium;
    }
}
