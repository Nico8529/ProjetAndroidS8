package com.example.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class PalierActivity extends Activity {

    // Tag pour les logs
    private static final String TAG = "PalierActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palier);

        // Log pour savoir quand l'activité est créée
        Log.d(TAG, "onCreate: L'activité PalierActivity est lancée");

        // Récupérer les extras envoyés par l'activité précédente
        int montantGagne = getIntent().getIntExtra("montantGagne", 0);
        int currentLevel = getIntent().getIntExtra("currentLevel", 1);

        // Log pour afficher les données récupérées
        Log.d(TAG, "onCreate: Montant gagné = " + montantGagne + " €; Niveau actuel = " + currentLevel);

        // Initialiser les vues (TextView et Button)
        TextView palierAmountText = findViewById(R.id.palierAmount);
        Button nextQuestionButton = findViewById(R.id.nextQuestionButton);

        // Mettre à jour l'affichage du montant gagné dans le TextView avec la locale par défaut
        palierAmountText.setText(String.format(Locale.getDefault(), "%,d €", montantGagne));

        // Log pour vérifier que l'affichage a été mis à jour correctement
        Log.d(TAG, "onCreate: Montant affiché = " + palierAmountText.getText().toString());

        // Afficher la page pendant 2 secondes avant de passer à la question suivante
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Log pour indiquer qu'on passe à la question suivante
            Log.d(TAG, "run: 2 secondes écoulées, passage à la question suivante");
            nextQuestion();
        }, 2000); // Attendre 2 secondes

        // Bouton suivant (pour tester, il sera visible)
        nextQuestionButton.setVisibility(View.VISIBLE);
        nextQuestionButton.setOnClickListener(v -> {
            // Log pour indiquer qu'on passe à la question suivante via le bouton
            Log.d(TAG, "onClick: Bouton suivant cliqué, passage à la question suivante");
            nextQuestion();
        });
    }

    private void nextQuestion() {
        // Log pour indiquer qu'on passe à la question suivante
        Log.d(TAG, "nextQuestion: Passage à la question suivante");

        // Cette méthode fermera l'activité actuelle et retournera à l'activité principale du quiz.
        finish();  // Fermer l'activité de palier et retourner à l'activité précédente
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Log pour indiquer que l'activité a été mise en pause
        Log.d(TAG, "onPause: L'activité PalierActivity est en pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Log pour indiquer que l'activité a été reprise
        Log.d(TAG, "onResume: L'activité PalierActivity a été reprise");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Log pour indiquer que l'activité est en train d'être détruite
        Log.d(TAG, "onDestroy: L'activité PalierActivity est en train d'être détruite");
    }
}
