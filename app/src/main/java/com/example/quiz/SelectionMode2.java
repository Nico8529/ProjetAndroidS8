package com.example.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectionMode2 extends AppCompatActivity {

    private int quizId;
    private String quizTitle;
    private String premium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_mode2);

        // 🔙 Bouton retour
        ImageButton btnBack = findViewById(R.id.btnBack_LSelection_mode2);
        btnBack.setOnClickListener(v -> finish());

        // 🧠 Récupérer les données du quiz sélectionné
        SharedPreferences prefs = getSharedPreferences("QUIZ_DATA", MODE_PRIVATE);
        quizId = prefs.getInt("selectedQuizId", -1);
        quizTitle = prefs.getString("selectedQuizTitle", "Quiz inconnu");
        premium = prefs.getString("premium", "no");
        int score = prefs.getInt("selectedQuizScore", -1);

        // 🖋 Affichage du titre et score du quiz
        TextView titleText = findViewById(R.id.title_Lselection_mode2);
        titleText.setText(getString(R.string.game_mode_for, quizTitle));

        TextView quizTitleText = findViewById(R.id.selectedQuizTitleText);
        quizTitleText.setText(getString(R.string.quiz_title, quizTitle));

        TextView quizScoreText = findViewById(R.id.selectedQuizScoreText);
        if (score != -1) {
            quizScoreText.setText(getString(R.string.score, score));
        } else {
            quizScoreText.setText(getString(R.string.no_score));
        }

        // 🕹 Boutons de mode
        Button btnNormal = findViewById(R.id.btnNormal_LSelection_mode2);
//        Button btnMontre = findViewById(R.id.btnMontre_LSelection_mode2);
//        Button btnFast = findViewById(R.id.btnFast_LSelection_mode2);
//        Button btn3Best = findViewById(R.id.btn3Best_LSelection_mode2);
//        Button btnDiffPoint = findViewById(R.id.btnDiffPoint_LSelection_mode2);

        // 🧪 Actions des boutons
        btnNormal.setOnClickListener(v -> launchMode("Normal"));
//        btnMontre.setOnClickListener(v -> launchMode("Contre la montre"));
//        btnFast.setOnClickListener(v -> launchMode("Le plus rapide"));
//        btn3Best.setOnClickListener(v -> launchMode("3 meilleurs sur 4"));
//        btnDiffPoint.setOnClickListener(v -> launchMode("Répartition régressive"));
    }

    private void launchMode(String modeName) {
        Intent intent = new Intent(this, GameQuiz.class);
        intent.putExtra("quizId", quizId);
        intent.putExtra("mode", modeName);
        intent.putExtra("quizTitle", quizTitle);
        intent.putExtra("premium", premium);
        startActivity(intent);
    }
}

