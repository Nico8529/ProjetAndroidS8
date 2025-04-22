package com.example.quiz;

import android.util.Log;

public class Quiz {

    // Déclaration des variables de la classe Quiz avec 'final' pour les rendre immuables après l'initialisation
    private final int id;
    private final String title;
    private final int score;
    private final int nombreQuestion;
    private final String premium;
    private final String keywords;
    private final String quizDescription;

    // Tag pour les logs
    private static final String TAG = "Quiz";

    // Constructeur de la classe Quiz
    public Quiz(int id, String title, int score, String premium, String keywords, String quizDescription, int nombreQuestion) {
        // Initialisation des variables avec les valeurs passées en paramètre
        this.id = id;
        this.title = title;
        this.score = score;
        this.premium = premium;
        this.quizDescription = quizDescription;
        this.nombreQuestion = nombreQuestion;
        this.keywords = keywords;

        // Log pour afficher que l'objet Quiz a été créé
        Log.d(TAG, "Quiz created: ID = " + id + ", Title = " + title + ", Score = " + score + ", Premium = " + premium);
    }

    // Méthode pour obtenir l'ID du quiz
    public int getId() {
        Log.d(TAG, "getId: ID du quiz = " + id);
        return id;
    }

    // Méthode pour obtenir le nombre de questions du quiz
    public int getNombreQuestion() {
        Log.d(TAG, "nombreQuestion: Nombre de questions = " + nombreQuestion);
        return nombreQuestion;
    }

    // Méthode pour obtenir les mots-clés du quiz
    public String getKeywords() {
        Log.d(TAG, "getKeywords: Mots-clés du quiz = " + keywords);
        return keywords;
    }

    // Méthode pour obtenir la description du quiz
    public String quizDescription() {
        Log.d(TAG, "quizDescription: Description du quiz = " + quizDescription);
        return quizDescription;
    }

    // Méthode pour obtenir le titre du quiz
    public String getTitle() {
        Log.d(TAG, "getTitle: Titre du quiz = " + title);
        return title;
    }

    // Méthode pour obtenir le score du quiz
    public int getScore() {
        Log.d(TAG, "getScore: Score du quiz = " + score);
        return score;
    }

    // Méthode pour obtenir le statut premium du quiz
    public String premium() {
        Log.d(TAG, "premium: Statut premium du quiz = " + premium);
        return premium;
    }
}
