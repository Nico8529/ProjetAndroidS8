package com.example.quiz;

import java.util.List;

public class SaveData {
    private List<Save> saves;

    public List<Save> getSaves() {
        return saves;
    }

    public void setSaves(List<Save> saves) {
        this.saves = saves;
    }

    public static class Save {
        private int slot;
        private String playerName;
        private String mode;
        private String quizTitle;
        private int quizId;
        private int currentQuestionIndex;
        private int score;
        private int lives;
        private boolean isJoker5050Used;
        private boolean isJokerSkipUsed;
        private boolean isJokerAudienceUsed;

        // Getters and Setters
        public int getSlot() {
            return slot;
        }

        public void setSlot(int slot) {
            this.slot = slot;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getQuizTitle() {
            return quizTitle;
        }

        public void setQuizTitle(String quizTitle) {
            this.quizTitle = quizTitle;
        }

        public int getQuizId() {
            return quizId;
        }

        public void setQuizId(int quizId) {
            this.quizId = quizId;
        }

        public int getCurrentQuestionIndex() {
            return currentQuestionIndex;
        }

        public void setCurrentQuestionIndex(int currentQuestionIndex) {
            this.currentQuestionIndex = currentQuestionIndex;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getLives() {
            return lives;
        }

        public void setLives(int lives) {
            this.lives = lives;
        }

        public boolean isJoker5050Used() {
            return isJoker5050Used;
        }

        public void setJoker5050Used(boolean joker5050Used) {
            isJoker5050Used = joker5050Used;
        }

        public boolean isJokerSkipUsed() {
            return isJokerSkipUsed;
        }

        public void setJokerSkipUsed(boolean jokerSkipUsed) {
            isJokerSkipUsed = jokerSkipUsed;
        }

        public boolean isJokerAudienceUsed() {
            return isJokerAudienceUsed;
        }

        public void setJokerAudienceUsed(boolean jokerAudienceUsed) {
            isJokerAudienceUsed = jokerAudienceUsed;
        }
    }
}
