package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MenuQuiz extends AppCompatActivity {

    private EditText playerNameEditText;
    private Button saveNameButton;
    private Button quitButton;
    private Button startQuizButton;
    private TextView scoreText;
    private TextView moneyText;
    private TextView palierText;

    private int score;  // Le score actuel du joueur
    private int money;  // L'argent gagné
    private int currentPalier;  // Le palier actuel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_quiz);

        // Initialisation des éléments de l'interface
        playerNameEditText = findViewById(R.id.playerNameEditText);
        saveNameButton = findViewById(R.id.saveNameButton);
        quitButton = findViewById(R.id.quitButton);
        startQuizButton = findViewById(R.id.startQuizButton);
        scoreText = findViewById(R.id.scoreText);
        moneyText = findViewById(R.id.moneyText);
        palierText = findViewById(R.id.palierText);

        // Initialiser avec des données fictives ou passer des valeurs depuis l'activité précédente
        score = 0;  // Exemple de score
        money = 0;   // Exemple d'argent gagné
        currentPalier = 0;  // Exemple de palier actuel

        // Mettre à jour les vues avec les données actuelles
        scoreText.setText("Score : " + score);
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
            // Vous pouvez passer des données au GameQuiz via un Intent, par exemple le score actuel
            Intent intent = new Intent(MenuQuiz.this, GameQuiz.class);
            intent.putExtra("score", score);
            startActivity(intent);
        });
    }
}
