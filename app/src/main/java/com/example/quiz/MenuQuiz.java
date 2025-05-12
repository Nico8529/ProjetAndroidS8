package com.example.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MenuQuiz extends AppCompatActivity {

    private EditText playerNameEditText;

    private int score;  // Le score actuel du joueur

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_quiz);

        // Initialisation des éléments de l'interface
        playerNameEditText = findViewById(R.id.playerNameEditText);
        Button saveNameButton = findViewById(R.id.saveNameButton);
        Button quitButton = findViewById(R.id.quitButton);
        Button startQuizButton = findViewById(R.id.startQuizButton);
        TextView scoreText = findViewById(R.id.scoreText);
        TextView moneyText = findViewById(R.id.moneyText);
        TextView palierText = findViewById(R.id.palierText);

        // Initialiser avec des données fictives ou passer des valeurs depuis l'activité précédente
        score = 0;  // Exemple de score
        // L'argent gagné
        int money = 0;   // Exemple d'argent gagné
        // Le palier actuel
        int currentPalier = 0;  // Exemple de palier actuel

        // Mettre à jour les vues avec les données actuelles
        scoreText.setText(getString(R.string.score, score));
        moneyText.setText("Argent gagné : " + money + " €");
        palierText.setText("Palier actuel : Niveau " + currentPalier);

        // Sauvegarder le nom du joueur
        saveNameButton.setOnClickListener(v -> {
            String playerName = playerNameEditText.getText().toString();
            if (!playerName.isEmpty()) {
                // Sauvegarder le nom (par exemple, dans SharedPreferences)
                // Code à ajouter pour sauvegarder le nom dans un fichier ou base de données
                Toast.makeText(MenuQuiz.this, "Nom sauvegardé : " + playerName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MenuQuiz.this, "Veuillez entrer un nom.", Toast.LENGTH_SHORT).show();
            }
        });

        // Quitter le jeu
        quitButton.setOnClickListener(v -> {
            finish();  // Ferme l'activité actuelle et retourne au menu principal ou ferme l'application
        });

        // Revenir au quiz
        startQuizButton.setOnClickListener(v -> {
            finish();  // Ferme l'activité actuelle et retourne au menu principal ou ferme l'application
        });
    }
}
