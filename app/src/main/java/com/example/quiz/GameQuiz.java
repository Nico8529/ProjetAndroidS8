package com.example.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GameQuiz extends AppCompatActivity {

    private int quizId;
    private boolean isJoker5050Used = false;
    private boolean isJokerSkipUsed = false;
    private boolean isJokerAudienceUsed = false;
    private int score = 0;
    private int currentQuestionIndex = 0;
    private int lives = 1;

    private TextView scoreText;
    private TextView questionText;
    private Button[] answerButtons = new Button[4];
    private JSONObject quizData;
    private String mode;
    private String quizTitle;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_quiz);

        quizId = getIntent().getIntExtra("quizId", -1);
        mode = getIntent().getStringExtra("mode");
        quizTitle = getIntent().getStringExtra("quizTitle");

        SharedPreferences sharedPreferences = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        questionText = findViewById(R.id.questionText);
        answerButtons[0] = findViewById(R.id.answerA);
        answerButtons[1] = findViewById(R.id.answerB);
        answerButtons[2] = findViewById(R.id.answerC);
        answerButtons[3] = findViewById(R.id.answerD);
        scoreText = findViewById(R.id.scoreText);

        TextView quizTitleText = findViewById(R.id.quizTitle);
        quizTitleText.setText("Quiz : " + quizTitle);

        TextView modeText = findViewById(R.id.gameMode);
        modeText.setText("Mode de jeu : " + mode);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.FRENCH);
            }
        });

        quizData = QuizData.loadQuiz(this, quizId);
        if (quizData != null) {
            try {
                JSONArray questions = quizData.getJSONArray("questions");
                loadQuestion(questions, currentQuestionIndex);
                StringBuilder questionSpeech = new StringBuilder("Question : ");
                questionSpeech.append(questions).append(". ");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur de traitement des données du quiz", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Erreur de chargement des données du quiz", Toast.LENGTH_SHORT).show();
        };

        ImageButton joker5050Btn = findViewById(R.id.joker5050);
        joker5050Btn.setOnClickListener(v -> useJoker5050());

        ImageButton jokerSkipBtn = findViewById(R.id.jokerSkip);
        jokerSkipBtn.setOnClickListener(v -> useJokerSkipQuestion());

        ImageButton jokerAudienceBtn = findViewById(R.id.jokerAudience);
        jokerAudienceBtn.setOnClickListener(v -> useJokerAudience());

        ImageButton menuButton = findViewById(R.id.btnMenu_LGame_quiz);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameQuiz.this, MenuQuiz.class);
            startActivity(intent);
        });
    }

    private void speak(String text) {
        if (tts != null && !text.isEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void loadQuestion(JSONArray questions, int index) {
        try {
            JSONObject question = questions.getJSONObject(index);
            String questionTextValue = question.getString("question");
            JSONArray options = question.getJSONArray("options");

            for (Button btn : answerButtons) {
                btn.setVisibility(View.VISIBLE);
            }

            questionText.setText(questionTextValue);

            StringBuilder questionSpeech = new StringBuilder("Question : ");
            questionSpeech.append(questionTextValue).append(". ");

            for (int i = 0; i < options.length(); i++) {
                String optionText = options.getString(i);
                answerButtons[i].setText(optionText);
                questionSpeech.append("Réponse ").append((char)('A' + i)).append(" : ").append(optionText).append(". ");
                final int finalI = i;
                answerButtons[i].setOnClickListener(v -> {
                    try {
                        checkAnswer(finalI, question.getInt("correctAnswer"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            speak(questionSpeech.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors du chargement de la question", Toast.LENGTH_SHORT).show();
        }
    }

    private void useJoker5050() {
        if (isJoker5050Used) {
            Toast.makeText(this, "Le joker 50:50 a déjà été utilisé.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject question = quizData.getJSONArray("questions").getJSONObject(currentQuestionIndex);
            int correctAnswer = question.getInt("correctAnswer");

            int wrongAnswer;
            do {
                wrongAnswer = (int) (Math.random() * 4);
            } while (wrongAnswer == correctAnswer);

            int anotherWrongAnswer;
            do {
                anotherWrongAnswer = (int) (Math.random() * 4);
            } while (anotherWrongAnswer == correctAnswer || anotherWrongAnswer == wrongAnswer);

            answerButtons[wrongAnswer].setVisibility(View.INVISIBLE);
            answerButtons[anotherWrongAnswer].setVisibility(View.INVISIBLE);

            isJoker5050Used = true;

            ImageButton joker5050Btn = findViewById(R.id.joker5050);
            joker5050Btn.setEnabled(false);
            joker5050Btn.setAlpha(0.5f);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de l'utilisation du joker", Toast.LENGTH_SHORT).show();
        }
    }

    private void useJokerSkipQuestion() {
        if (isJokerSkipUsed) {
            Toast.makeText(this, "Ce joker a déjà été utilisé.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONArray questions = quizData.getJSONArray("questions");
            int newIndex;
            do {
                newIndex = (int) (Math.random() * questions.length());
            } while (newIndex == currentQuestionIndex);

            currentQuestionIndex = newIndex;
            loadQuestion(questions, currentQuestionIndex);

            isJokerSkipUsed = true;

            ImageButton skipBtn = findViewById(R.id.jokerSkip);
            skipBtn.setEnabled(false);
            skipBtn.setAlpha(0.5f);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void useJokerAudience() {
        if (isJokerAudienceUsed) {
            Toast.makeText(this, "Ce joker a déjà été utilisé.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject question = quizData.getJSONArray("questions").getJSONObject(currentQuestionIndex);
            int correctAnswer = question.getInt("correctAnswer");

            int[] votes = new int[4];
            votes[correctAnswer] = 40 + (int)(Math.random() * 21);

            int remaining = 100 - votes[correctAnswer];
            List<Integer> wrongIndexes = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (i != correctAnswer) wrongIndexes.add(i);
            }

            Collections.shuffle(wrongIndexes);
            int part1 = (int)(Math.random() * (remaining + 1));
            int part2 = (int)(Math.random() * (remaining - part1 + 1));
            int part3 = remaining - part1 - part2;

            votes[wrongIndexes.get(0)] = part1;
            votes[wrongIndexes.get(1)] = part2;
            votes[wrongIndexes.get(2)] = part3;

            StringBuilder result = new StringBuilder("Vote du public :\n");
            for (int i = 0; i < 4; i++) {
                result.append((char)('A' + i)).append(" : ").append(votes[i]).append("%\n");
            }

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Résultat du vote")
                    .setMessage(result.toString())
                    .setPositiveButton("OK", null)
                    .show();

            isJokerAudienceUsed = true;

            ImageButton audienceBtn = findViewById(R.id.jokerAudience);
            audienceBtn.setEnabled(false);
            audienceBtn.setAlpha(0.5f);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de l'utilisation du joker", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAnswer(int selectedAnswer, int correctAnswer) {
        if (selectedAnswer == correctAnswer) {
            score++;
            Toast.makeText(this, "Bonne réponse!", Toast.LENGTH_SHORT).show();
            speak("Bonne réponse !");
            scoreText.setText("Score : " + score * 1000);
            //showPalier(score * 1000, currentQuestionIndex);
        } else {
            speak("Mauvaise réponse !");
            scoreText.setText("Score : " + score * 1000);
            lives--;
            if (lives <= 0) {
                showGameOverDialog(score * 1000);
            } else {
                Toast.makeText(this, "Mauvaise réponse, mais vous avez encore des vies!", Toast.LENGTH_SHORT).show();
            }
        }

        currentQuestionIndex++;
        try {
            JSONArray questions = quizData.getJSONArray("questions");
            if (currentQuestionIndex < questions.length()) {
                loadQuestion(questions, currentQuestionIndex);
            } else {
                Toast.makeText(this, "Quiz terminé !", Toast.LENGTH_LONG).show();
                speak("Quiz terminé !");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showPalier(int montantGagne, int currentLevel) {
        Intent intent = new Intent(this, PalierActivity.class);
        intent.putExtra("montantGagne", montantGagne);
        intent.putExtra("currentLevel", currentLevel);
        if (tts != null) {
            tts.stop();
        }
        startActivity(intent);
    }

    private void showGameOverDialog(int montantGagne) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Désolé, vous avez perdu. Vous avez gagné : " + montantGagne + " €")
                .setPositiveButton("Recommencer", (dialog, which) -> restartGame())
                .setNegativeButton("Quitter", (dialog, which) -> finish())
                .show();
    }

    private void restartGame() {
        lives = 1;
        score = 0;
        currentQuestionIndex = 0;
        scoreText.setText("Score : 0");
        try {
            JSONArray questions = quizData.getJSONArray("questions");
            loadQuestion(questions, currentQuestionIndex);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de redémarrage du quiz", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Sauvegarder l'état du jeu
        editor.putInt("score", score);
        editor.putInt("currentQuestionIndex", currentQuestionIndex);
        editor.putInt("lives", lives);
        editor.putBoolean("isJoker5050Used", isJoker5050Used);
        editor.putBoolean("isJokerSkipUsed", isJokerSkipUsed);
        editor.putBoolean("isJokerAudienceUsed", isJokerAudienceUsed);
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        score = sharedPreferences.getInt("score", 0);
        currentQuestionIndex = sharedPreferences.getInt("currentQuestionIndex", 0);
        lives = sharedPreferences.getInt("lives", 1);
        isJoker5050Used = sharedPreferences.getBoolean("isJoker5050Used", false);
        isJokerSkipUsed = sharedPreferences.getBoolean("isJokerSkipUsed", false);
        isJokerAudienceUsed = sharedPreferences.getBoolean("isJokerAudienceUsed", false);

        scoreText.setText("Score : " + score);
        try {
            JSONArray questions = quizData.getJSONArray("questions");
            loadQuestion(questions, currentQuestionIndex);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la restauration de l'état", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
