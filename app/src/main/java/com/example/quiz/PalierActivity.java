package com.example.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PalierActivity extends Activity {

    private TextView palierAmountText;
    private Button nextQuestionButton;
    private int montantGagne;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palier);

        // Récupérer les extras
        montantGagne = getIntent().getIntExtra("montantGagne", 0);
        currentLevel = getIntent().getIntExtra("currentLevel", 1);

        // Initialiser les vues
        palierAmountText = findViewById(R.id.palierAmount);
        nextQuestionButton = findViewById(R.id.nextQuestionButton);

        // Mettre à jour l'affichage du palier
        palierAmountText.setText(String.format("%,d €", montantGagne));

        // Afficher la page pendant 2 secondes
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Passer à la question suivante
                nextQuestion();
            }
        }, 2000); // Attendre 2 secondes

        // Bouton suivant (optionnel pour tester)
        nextQuestionButton.setVisibility(View.VISIBLE);
        nextQuestionButton.setOnClickListener(v -> nextQuestion());
    }

    private void nextQuestion() {
        // Retour à la question suivante
        // Cette action dépend de la structure de votre jeu. Par exemple :
        finish();  // Cette méthode fermera l'activité de palier et retournera à l'activité principale du quiz.
    }
}
