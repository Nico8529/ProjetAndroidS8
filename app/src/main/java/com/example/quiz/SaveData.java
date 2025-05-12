package com.example.quiz;

import java.util.List;

public class SaveData {
    private final List<Save> saves;

    public SaveData(List<Save> saves) {
        this.saves = saves;
    }

    public List<Save> getSaves() {
        return saves;
    }

    public static class Save {
        private String playerName;
        private String mode;
        private String mode2;
        private String quizTitle;
        private int quizId;
        private int currentQuestionIndex;
        private int score;
        private int lives;
        private boolean isJoker5050Used;
        private boolean isJokerSkipUsed;
        private boolean isJokerAudienceUsed;

        public void reset() {
            this.playerName = "Sauvegarde Libre";
            this.mode = "";
            this.mode2 = "";
            this.quizTitle = "";
            this.quizId = 0;
            this.currentQuestionIndex = 0;
            this.score = 0;
            this.lives = 0;
            this.isJoker5050Used = false;
            this.isJokerSkipUsed = false;
            this.isJokerAudienceUsed = false;
        }
        public String getPlayerName() {
            return playerName;
        }
        public String getMode() {
            return mode;
        }
        public String getMode2() {
            return mode2;
        }
        public String getQuizTitle() {
            return quizTitle;
        }
        public int getQuizId() {
            return quizId;
        }
        public int getCurrentQuestionIndex() {
            return currentQuestionIndex;
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
        public boolean isJoker5050Used() {
            return isJoker5050Used;
        }
        public boolean isJokerSkipUsed() {
            return isJokerSkipUsed;
        }
        public boolean isJokerAudienceUsed() {
            return isJokerAudienceUsed;
        }

    }
}
