package com.example.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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

    private static final String TAG = "GameQuiz";

    private int quizId;
    private int score = 0;
    private int currentQuestionIndex = 0;
    private int lives = 1;

    private boolean isJoker5050Used = false;
    private boolean isJokerSkipUsed = false;
    private boolean isJokerAudienceUsed = false;

    private final Button[] answerButtons = new Button[4];
    private TextView questionText, scoreText;
    private JSONObject quizData;
    private String mode, quizTitle;
    private TextToSpeech tts;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_quiz);
        Log.d(TAG, "Activity created");

        retrieveIntentData();
        resetPreferences();
        initializeUI();
        initializeTTS();

        if ("Contre la montre".equals(mode)) {
            startTimer();
        }

        quizData = QuizData.loadQuiz(this, quizId);
        if (quizData != null) {
            loadQuestionSafely();
        } else {
            Toast.makeText(this, "Erreur de chargement du quiz.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "quizData is null");
        }
    }

    private void retrieveIntentData() {
        quizId = getIntent().getIntExtra("quizId", -1);
        mode = getIntent().getStringExtra("mode");
        quizTitle = getIntent().getStringExtra("quizTitle");
        Log.d(TAG, "Intent data retrieved: quizId=" + quizId + ", mode=" + mode + ", title=" + quizTitle);
    }

    private void resetPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("QuizPrefs", MODE_PRIVATE).edit();
        editor.clear().apply();
        Log.d(TAG, "Preferences reset");
    }

    private void initializeUI() {
        questionText = findViewById(R.id.questionText);
        scoreText = findViewById(R.id.scoreText);
        TextView quizTitleText = findViewById(R.id.quizTitle);
        TextView modeText = findViewById(R.id.gameMode);

        quizTitleText.setText(getString(R.string.quiz_title2, quizTitle));
        modeText.setText(getString(R.string.game_mode, mode));

        answerButtons[0] = findViewById(R.id.answerA);
        answerButtons[1] = findViewById(R.id.answerB);
        answerButtons[2] = findViewById(R.id.answerC);
        answerButtons[3] = findViewById(R.id.answerD);

        setupJokers();
        // Modifier cette ligne
        findViewById(R.id.btnMenu_LGame_quiz).setOnClickListener(v -> confirmerQuitterQuiz());
        Log.d(TAG, "UI initialized");
    }

    private void confirmerQuitterQuiz() {
        new android.app.AlertDialog.Builder(this)
            .setTitle("Quitter le Quiz")
            .setMessage("Voulez-vous vraiment quitter le quiz ? Votre progression sera perdue.")
            .setPositiveButton("Oui", (dialog, which) -> {
                if (tts != null) {
                    tts.stop();
                    tts.shutdown();
                }
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                super.onBackPressed(); // Appel de la méthode parent ici
            })
            .setNegativeButton("Non", null)
            .show();
    }

    // Ajouter aussi la gestion du bouton retour
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        confirmerQuitterQuiz();
      
    }

    private void initializeTTS() {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.FRENCH);
                Log.d(TAG, "TTS initialized");
            } else {
                Log.e(TAG, "TTS initialization failed");
            }
        });
    }

    private void setupJokers() {
        findViewById(R.id.joker5050).setOnClickListener(v -> useJoker5050());
        findViewById(R.id.jokerSkip).setOnClickListener(v -> useJokerSkipQuestion());
        findViewById(R.id.jokerAudience).setOnClickListener(v -> useJokerAudience());
    }

    private void loadQuestionSafely() {
        try {
            JSONArray questions = quizData.getJSONArray("questions");
            loadQuestion(questions, currentQuestionIndex);
        } catch (JSONException e) {
            Log.e(TAG, "Erreur de parsing questions", e);
            Toast.makeText(this, "Erreur de traitement des données.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuestion(JSONArray questions, int index) throws JSONException {
        JSONObject question = questions.getJSONObject(index);
        String questionTextValue = question.getString("question");
        JSONArray options = question.getJSONArray("options");

        questionText.setText(questionTextValue);
        StringBuilder speech = new StringBuilder("Question : ").append(questionTextValue).append(". ");

        for (int i = 0; i < answerButtons.length; i++) {
            String option = options.getString(i);
            answerButtons[i].setVisibility(View.VISIBLE);
            answerButtons[i].setText(option);

            speech.append("Réponse ").append((char) ('A' + i)).append(" : ").append(option).append(". ");

            final int answerIndex = i;
            answerButtons[i].setOnClickListener(v -> checkAnswer(answerIndex, question.optInt("correctAnswer")));
        }

        speak(speech.toString());
        Log.d(TAG, "Question chargée : " + questionTextValue);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.timerText)).setText(getString(Integer.parseInt(R.string.temps + millisUntilFinished / 1000 + "s")));
            }

            public void onFinish() {
                Toast.makeText(GameQuiz.this, "Temps écoulé !", Toast.LENGTH_SHORT).show();
                showGameOverDialog(score * 1000);
                Log.d(TAG, "Timer finished");
            }
        }.start();
    }

    private void checkAnswer(int selected, int correct) {
        Log.d(TAG, "Réponse sélectionnée : " + selected + ", correcte : " + correct);
        if (selected == correct) {
            score++;
            Toast.makeText(this, "Bonne réponse!", Toast.LENGTH_SHORT).show();
            speak("Bonne réponse !");
        } else {
            speak("Mauvaise réponse !");
            lives--;
            if (lives <= 0) {
                showGameOverDialog(score * 1000);
                return;
            } else {
                Toast.makeText(this, "Mauvaise réponse, il vous reste une vie.", Toast.LENGTH_SHORT).show();
            }
        }

        scoreText.setText(getString(R.string.score, score * 1000));
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
            Log.e(TAG, "Erreur lors du chargement de la question suivante", e);
        }
    }

    private void useJoker5050() {
        if (isJoker5050Used) {
            Toast.makeText(this, "Joker 50:50 déjà utilisé.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject question = quizData.getJSONArray("questions").getJSONObject(currentQuestionIndex);
            int correct = question.getInt("correctAnswer");

            List<Integer> hiddenIndices = new ArrayList<>();
            while (hiddenIndices.size() < 2) {
                int rand = (int) (Math.random() * 4);
                if (rand != correct && !hiddenIndices.contains(rand)) {
                    hiddenIndices.add(rand);
                    answerButtons[rand].setVisibility(View.INVISIBLE);
                }
            }

            isJoker5050Used = true;
            disableButton(R.id.joker5050);
            Log.d(TAG, "Joker 50:50 utilisé");

        } catch (JSONException e) {
            Log.e(TAG, "Erreur lors de l'utilisation du joker 50:50", e);
        }
    }

    private void useJokerSkipQuestion() {
        if (isJokerSkipUsed) {
            Toast.makeText(this, "Joker déjà utilisé.", Toast.LENGTH_SHORT).show();
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
            disableButton(R.id.jokerSkip);
            Log.d(TAG, "Joker de saut de question utilisé");

        } catch (JSONException e) {
            Log.e(TAG, "Erreur lors de l'utilisation du joker de saut", e);
        }
    }

    private void useJokerAudience() {
        if (isJokerAudienceUsed) {
            Toast.makeText(this, "Joker déjà utilisé.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject question = quizData.getJSONArray("questions").getJSONObject(currentQuestionIndex);
            int correct = question.getInt("correctAnswer");

            int[] votes = generateVotes(correct);
            showVotesDialog(votes);

            isJokerAudienceUsed = true;
            disableButton(R.id.jokerAudience);
            Log.d(TAG, "Joker audience utilisé");

        } catch (JSONException e) {
            Log.e(TAG, "Erreur lors de l'utilisation du joker audience", e);
        }
    }

    private int[] generateVotes(int correctIndex) {
        int[] votes = new int[4];
        votes[correctIndex] = 40 + (int) (Math.random() * 21);
        int remaining = 100 - votes[correctIndex];

        List<Integer> wrongIndices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != correctIndex) wrongIndices.add(i);
        }
        Collections.shuffle(wrongIndices);

        votes[wrongIndices.get(0)] = (int) (Math.random() * remaining);
        votes[wrongIndices.get(1)] = (int) (Math.random() * (remaining - votes[wrongIndices.get(0)]));
        votes[wrongIndices.get(2)] = remaining - votes[wrongIndices.get(0)] - votes[wrongIndices.get(1)];

        return votes;
    }

    private void showVotesDialog(int[] votes) {
        StringBuilder message = new StringBuilder("Vote du public :\n");
        for (int i = 0; i < 4; i++) {
            message.append((char) ('A' + i)).append(" : ").append(votes[i]).append("%\n");
            Log.d(TAG, "Vote Utilisé");
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Résultat du vote")
                .setMessage(message.toString())
                .setPositiveButton("OK", null)

                .show();
    }

    private void disableButton(int buttonId) {
        ImageButton btn = findViewById(buttonId);
        btn.setEnabled(false);
        btn.setAlpha(0.5f);
        Log.d(TAG, "Btn Joker manquant activé");
    }

    private void speak(String text) {
        if (tts != null && !text.isEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void showGameOverDialog(int reward) {
        Log.d(TAG, "Fin de Partie");
        new android.app.AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Vous avez gagné : " + reward + " €")
                .setPositiveButton("Recommencer", (d, w) -> restartGame())
                .setNegativeButton("Quitter", (d, w) -> finish())
                .show();
    }

    private void restartGame() {
        score = 0;
        lives = 1;
        currentQuestionIndex = 0;
        scoreText.setText(getString(R.string.score, 0));

        try {
            loadQuestion(quizData.getJSONArray("questions"), currentQuestionIndex);
        } catch (JSONException e) {
            Log.e(TAG, "Erreur lors du redémarrage", e);
            Toast.makeText(this, "Erreur de redémarrage.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("QuizPrefs", MODE_PRIVATE).edit();
        editor.putInt("score", score);
        editor.putInt("currentQuestionIndex", currentQuestionIndex);
        editor.putInt("lives", lives);
        editor.putBoolean("isJoker5050Used", isJoker5050Used);
        editor.putBoolean("isJokerSkipUsed", isJokerSkipUsed);
        editor.putBoolean("isJokerAudienceUsed", isJokerAudienceUsed);
        editor.apply();
        Log.d(TAG, "État sauvegardé");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("QuizPrefs", MODE_PRIVATE);

        score = prefs.getInt("score", 0);
        currentQuestionIndex = prefs.getInt("currentQuestionIndex", 0);
        lives = prefs.getInt("lives", 1);
        isJoker5050Used = prefs.getBoolean("isJoker5050Used", false);
        isJokerSkipUsed = prefs.getBoolean("isJokerSkipUsed", false);
        isJokerAudienceUsed = prefs.getBoolean("isJokerAudienceUsed", false);

        scoreText.setText(getString(R.string.score, score));
        loadQuestionSafely();
        Log.d(TAG, "État restauré");
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Log.d(TAG, "Activity destroyed");
        super.onDestroy();
    }
}