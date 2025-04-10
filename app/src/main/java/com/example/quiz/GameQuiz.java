package com.example.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class GameQuiz extends AppCompatActivity {

    private int quizId;
    private boolean isJoker5050Used = false;
    private boolean isJokerSkipUsed = false;
    private boolean isJokerAudienceUsed = false;
    private int score = 0; // Score gagné jusqu'à présent
    private int currentQuestionIndex = 0;
    private TextView scoreText;
    private JSONObject quizData;
    private String mode;
    private String quizTitle;
    private int lives = 1; // Nombre de vies
    private TextView questionText;
    private Button[] answerButtons = new Button[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_quiz);

        // Récupérer les extras passés via l'intent
        quizId = getIntent().getIntExtra("quizId", -1);
        mode = getIntent().getStringExtra("mode");
        quizTitle = getIntent().getStringExtra("quizTitle");

        // Initialiser les vues
        questionText = findViewById(R.id.questionText);
        answerButtons[0] = findViewById(R.id.answerA);
        answerButtons[1] = findViewById(R.id.answerB);
        answerButtons[2] = findViewById(R.id.answerC);
        answerButtons[3] = findViewById(R.id.answerD);

        scoreText = findViewById(R.id.scoreText);

        // Afficher le nom du quiz
        TextView quizTitleText = findViewById(R.id.quizTitle);
        quizTitleText.setText("Quiz :" + quizTitle); // Afficher le nom du quiz

        // Afficher le mode de jeu
        TextView modeText = findViewById(R.id.gameMode);
        modeText.setText("Mode de jeu: " + mode); // Afficher le mode de jeu

        // Charger les données du quiz depuis le fichier JSON
        quizData = QuizData.loadQuiz(this, quizId);

        if (quizData != null) {
            try {
                // Charger les questions
                JSONArray questions = quizData.getJSONArray("questions");
                loadQuestion(questions, currentQuestionIndex);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur de traitement des données du quiz", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Erreur de chargement des données du quiz", Toast.LENGTH_SHORT).show();
        }

        // Joker 50:50
        ImageButton joker5050Btn = findViewById(R.id.joker5050);
        joker5050Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useJoker5050();
            }
        });
        ImageButton jokerSkipBtn = findViewById(R.id.jokerSkip);
        jokerSkipBtn.setOnClickListener(v -> useJokerSkipQuestion());

        ImageButton jokerAudienceBtn = findViewById(R.id.jokerAudience);
        jokerAudienceBtn.setOnClickListener(v -> useJokerAudience());

        // Dans GameQuiz, ouvrez MenuQuizActivity depuis un bouton
        ImageButton menuButton = findViewById(R.id.btnMenu_LGame_quiz);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameQuiz.this, MenuQuiz.class);
            startActivity(intent);
        });


    }
    private void loadQuestion(JSONArray questions, int index) {
        try {
            JSONObject question = questions.getJSONObject(index);
            String questionTextValue = question.getString("question");
            JSONArray options = question.getJSONArray("options");

            // Réaffiche tous les boutons au cas où un 50:50 les aurait masqués
            for (Button btn : answerButtons) {
                btn.setVisibility(View.VISIBLE);
            }

            // Affichage de la question
            questionText.setText(questionTextValue);

            // Affichage des options de réponses
            for (int i = 0; i < options.length(); i++) {
                answerButtons[i].setText(options.getString(i));
                final int finalI = i;
                answerButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            checkAnswer(finalI, question.getInt("correctAnswer"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
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

            // Marquer le joker comme utilisé
            isJoker5050Used = true;

            // Optionnel : désactiver le bouton pour le feedback visuel
            ImageButton joker5050Btn = findViewById(R.id.joker5050);
            joker5050Btn.setEnabled(false);
            joker5050Btn.setAlpha(0.5f); // rend le bouton visuellement désactivé

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

            // Choisir un index différent de la question actuelle
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

            // Attribuer une majorité au bon choix
            votes[correctAnswer] = 40 + (int)(Math.random() * 21); // 40-60%

            int remaining = 100 - votes[correctAnswer];

            // Répartir les votes restants sur les autres réponses
            List<Integer> wrongIndexes = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (i != correctAnswer) wrongIndexes.add(i);
            }

            // Répartition aléatoire du reste des votes
            Collections.shuffle(wrongIndexes);
            int part1 = (int)(Math.random() * (remaining + 1));
            int part2 = (int)(Math.random() * (remaining - part1 + 1));
            int part3 = remaining - part1 - part2;

            votes[wrongIndexes.get(0)] = part1;
            votes[wrongIndexes.get(1)] = part2;
            votes[wrongIndexes.get(2)] = part3;

            // Construction du message
            StringBuilder result = new StringBuilder("Vote du public :\n");
            for (int i = 0; i < 4; i++) {
                result.append((char)('A' + i)).append(" : ").append(votes[i]).append("%\n");
            }

            // Affichage dans une boîte de dialogue simple
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
            scoreText.setText("Score : " + score * 1000);
            // Appeler la fonction pour afficher le palier entre chaque question
            showPalier(score * 1000, currentQuestionIndex); // Exemple: multiplier le score par 1000 pour chaque bonne réponse
        } else {
            scoreText.setText("Score : " + score * 1000);
            lives--; // Perdre une vie
            if (lives <= 0) {
                // Le joueur a perdu
                showGameOverDialog(score * 1000);
            } else {
                // Si on a encore une vie, on continue normalement
                Toast.makeText(this, "Mauvaise réponse, mais vous avez encore des vies!", Toast.LENGTH_SHORT).show();
            }
        }
        // Passer à la prochaine question
        currentQuestionIndex++;
        try {
            JSONArray questions = quizData.getJSONArray("questions");
            if (currentQuestionIndex < questions.length()) {
                loadQuestion(questions, currentQuestionIndex);
            } else {
                Toast.makeText(this, "Quiz terminé !", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showPalier(int montantGagne, int currentLevel) {
        Intent intent = new Intent(this, PalierActivity.class);
        intent.putExtra("montantGagne", montantGagne);
        intent.putExtra("currentLevel", currentLevel);
        startActivity(intent);

        // Attendre que l'activité Palier soit terminée et ensuite continuer
        // Optionnellement, vous pouvez ajouter une logique pour "pause" ici.
    }
    private void showGameOverDialog(int montantGagne) {
        // Affichage du message de fin avec montant gagné
        new android.app.AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Désolé, vous avez perdu. Vous avez gagné : " + montantGagne + " €")
                .setPositiveButton("Recommencer", (dialog, which) -> {
                    // Redémarrer le quiz ou retourner au menu
                    restartGame();
                })
                .setNegativeButton("Quitter", (dialog, which) -> {
                    // Quitter l'application ou retourner au menu principal
                    finish();  // Ferme l'activité actuelle
                })
                .show();
    }
    private void restartGame() {
        // Réinitialiser les valeurs
        int lives = 1;
        score = 0;
        currentQuestionIndex = 0;
        scoreText.setText("Score : 0");

        // Recommencer le quiz
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

        // Sauvegarder l'état du quiz dans SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("score", score);
        editor.putInt("currentQuestionIndex", currentQuestionIndex);
        editor.putInt("lives", lives);

        editor.putBoolean("isJoker5050Used", isJoker5050Used);
        editor.putBoolean("isJokerSkipUsed", isJokerSkipUsed);
        editor.putBoolean("isJokerAudienceUsed", isJokerAudienceUsed);

        editor.apply(); // Appliquer les modifications
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Restaurer l'état du quiz depuis SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("QuizPrefs", MODE_PRIVATE);

        score = sharedPreferences.getInt("score", 0); // Valeur par défaut 0
        currentQuestionIndex = sharedPreferences.getInt("currentQuestionIndex", 0); // Valeur par défaut 0
        lives = sharedPreferences.getInt("lives", 1); // Valeur par défaut 1

        isJoker5050Used = sharedPreferences.getBoolean("isJoker5050Used", false);
        isJokerSkipUsed = sharedPreferences.getBoolean("isJokerSkipUsed", false);
        isJokerAudienceUsed = sharedPreferences.getBoolean("isJokerAudienceUsed", false);

        // Mettre à jour l'UI avec les valeurs restaurées
        scoreText.setText("Score : " + score);
        // Vous pouvez également restaurer la question en appelant `loadQuestion` avec le bon index.
        try {
            JSONArray questions = quizData.getJSONArray("questions");
            loadQuestion(questions, currentQuestionIndex);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la restauration de l'état", Toast.LENGTH_SHORT).show();
        }
    }


}
