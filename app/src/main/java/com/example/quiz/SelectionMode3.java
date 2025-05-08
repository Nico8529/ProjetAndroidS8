package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionMode3 extends AppCompatActivity {

    // Tag pour les logs
    private static final String TAG = "SelectionMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_mode3);

        // Bouton retour
        findViewById(R.id.btnBack_LSelectMode3).setOnClickListener(v -> {
            Log.d(TAG, "onCreate: Bouton retour cliqué, fermeture de l'activité.");
            finish();
        });

        // Initialisation des boutons
        Button btnSolo = findViewById(R.id.btnSolo_LSelectMode3);
        Button btnMulti = findViewById(R.id.btnMulti_LSelectMode3);
        Button btnMulti2 = findViewById(R.id.btnMulti2_LSelectMode3);

        // Configuration spécifique des actions
        btnSolo.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Bouton Solo cliqué, lancement de SelectionQuiz.");
            startActivity(new Intent(SelectionMode3.this, SelectionQuiz.class));
        });

        btnMulti.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Bouton Multi cliqué, lancement de ConfigHub.");
            startActivity(new Intent(SelectionMode3.this, ConfigHub.class));
        });
        btnMulti2.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Bouton Multi2 cliqué, lancement de ConfigHub.");
            startActivity(new Intent(SelectionMode3.this, Lobby.class));
        });
    }

}