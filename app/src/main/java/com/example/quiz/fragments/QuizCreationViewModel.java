package com.example.quiz.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class QuizCreationViewModel extends ViewModel {
    private final MutableLiveData<String> quizTitle = new MutableLiveData<>();
    private final MutableLiveData<List<Question>> questions = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> currentQuestionNumber = new MutableLiveData<>(0);

    // Getters et Setters pour le titre du quiz
    public void setQuizTitle(String title) {
        quizTitle.setValue(title);
    }

    public LiveData<String> getQuizTitle() {
        return quizTitle;
    }

    // Méthodes pour la gestion des questions
    public void addQuestion(Question question) {
        List<Question> currentQuestions = questions.getValue();
        if (currentQuestions != null) {
            currentQuestions.add(question);
            questions.setValue(currentQuestions);
            currentQuestionNumber.setValue(currentQuestions.size());
        }
    }

    public void updateQuestion(int index, Question question) {
        List<Question> currentQuestions = questions.getValue();
        if (currentQuestions != null && index >= 0 && index < currentQuestions.size()) {
            currentQuestions.set(index, question);
            questions.setValue(currentQuestions);
        }
    }

    public void removeQuestion(int index) {
        List<Question> currentQuestions = questions.getValue();
        if (currentQuestions != null && index >= 0 && index < currentQuestions.size()) {
            currentQuestions.remove(index);
            questions.setValue(currentQuestions);
            currentQuestionNumber.setValue(currentQuestions.size());
        }
    }

    public LiveData<List<Question>> getQuestions() {
        return questions;
    }

    public LiveData<Integer> getCurrentQuestionNumber() {
        return currentQuestionNumber;
    }

    // Classe interne pour représenter une question
    public static class Question {
        private String questionText;
        private List<String> options;
        private int correctAnswerIndex;

        public Question(String questionText, List<String> options, int correctAnswerIndex) {
            this.questionText = questionText;
            this.options = new ArrayList<>(options);
            this.correctAnswerIndex = correctAnswerIndex;
        }

        // Getters
        public String getQuestionText() {
            return questionText;
        }

        public List<String> getOptions() {
            return new ArrayList<>(options);
        }

        public int getCorrectAnswerIndex() {
            return correctAnswerIndex;
        }

        // Setters
        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public void setOptions(List<String> options) {
            this.options = new ArrayList<>(options);
        }

        public void setCorrectAnswerIndex(int correctAnswerIndex) {
            this.correctAnswerIndex = correctAnswerIndex;
        }
    }

    // Méthode pour vérifier si le quiz est valide
    public boolean isQuizValid() {
        String title = quizTitle.getValue();
        List<Question> questionList = questions.getValue();

        return title != null && !title.isEmpty() &&
                questionList != null && !questionList.isEmpty();
    }

    // Méthode pour réinitialiser le ViewModel
    public void reset() {
        quizTitle.setValue("");
        questions.setValue(new ArrayList<>());
        currentQuestionNumber.setValue(0);
    }
}