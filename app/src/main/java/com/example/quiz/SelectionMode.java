package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionMode extends AppCompatActivity {

    // Tag pour les logs
    private static final String TAG = "SelectionMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_mode);

        // Bouton retour
        findViewById(R.id.btnBack_Lselection_mode).setOnClickListener(v -> {
            // Log lorsque l'utilisateur clique sur le bouton retour
            Log.d(TAG, "onCreate: Bouton retour cliqué, fermeture de l'activité.");
            finish();
        });

        // Initialisation des boutons
        Button btnSolo = findViewById(R.id.btnSolo_LSelectMode);
        Button btnMultiLocalJ1 = findViewById(R.id.btnMultiLocalJ1_LSelectMode);
        Button btnMultiLocalJ2 = findViewById(R.id.btnMultiLocalJ2_LSelectMode);
        Button btnMultiLocalJ3 = findViewById(R.id.btnMultiLocalJ3_LSelectMode);
        Button btnMultiLocalJ4 = findViewById(R.id.btnMultiLocalJ4_LSelectMode);
        Button btnMultiOnlineJ1 = findViewById(R.id.btnMultiOnlineJ1_LSelectMode);
        Button btnMultiOnlineJ2 = findViewById(R.id.btnMultiOnlineJ2_LSelectMode);
        Button btnMultiOnlineJ3 = findViewById(R.id.btnMultiOnlineJ3_LSelectMode);
        Button btnMultiOnlineJ4 = findViewById(R.id.btnMultiOnlineJ4_LSelectMode);

        // Configuration des écouteurs pour les boutons avec la méthode générique
        setButtonClickListener(btnSolo);
        setButtonClickListener(btnMultiLocalJ1);
        setButtonClickListener(btnMultiLocalJ2);
        setButtonClickListener(btnMultiLocalJ3);
        setButtonClickListener(btnMultiLocalJ4);
        setButtonClickListener(btnMultiOnlineJ1);
        setButtonClickListener(btnMultiOnlineJ2);
        setButtonClickListener(btnMultiOnlineJ3);
        setButtonClickListener(btnMultiOnlineJ4);
    }

    // Méthode générique pour associer un clic au démarrage de l'activité SelectionQuiz
    private void setButtonClickListener(Button button) {
        button.setOnClickListener(v -> {
            // Log pour chaque bouton cliqué
            Log.d(TAG, "onClick: Bouton cliqué, lancement de SelectionQuiz.");
            startActivity(new Intent(SelectionMode.this, SelectionQuiz.class));
        });
    }
}
