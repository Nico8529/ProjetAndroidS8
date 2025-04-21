package com.example.quiz;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class MenuSauvegarde extends AppCompatActivity {

    private CardView cardSave1, cardSave2, cardSave3, cardSave4, cardSave5, cardSave6;
    private TextView playerName1, quizName1, gameMode1, score1;
    private TextView playerName2, quizName2, gameMode2, score2;
    private TextView playerName3, quizName3, gameMode3, score3;
    private TextView playerName4, quizName4, gameMode4, score4;
    private TextView playerName5, quizName5, gameMode5, score5;
    private TextView playerName6, quizName6, gameMode6, score6;
    private ImageButton btnLoad1, btnDelete1, btnLoad2, btnDelete2, btnLoad3, btnDelete3, btnLoad4, btnDelete4, btnLoad5, btnDelete5, btnLoad6, btnDelete6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_sauvegarde);

        cardSave1 = findViewById(R.id.cardSave1);
        cardSave2 = findViewById(R.id.cardSave2);
        cardSave3 = findViewById(R.id.cardSave3);
        cardSave4 = findViewById(R.id.cardSave4);
        cardSave5 = findViewById(R.id.cardSave5);
        cardSave6 = findViewById(R.id.cardSave6);

        playerName1 = findViewById(R.id.playerName1);
        quizName1 = findViewById(R.id.quizName1);
        gameMode1 = findViewById(R.id.gameMode1);
        score1 = findViewById(R.id.score1);

        playerName2 = findViewById(R.id.playerName2);
        quizName2 = findViewById(R.id.quizName2);
        gameMode2 = findViewById(R.id.gameMode2);
        score2 = findViewById(R.id.score2);

        playerName3 = findViewById(R.id.playerName3);
        quizName3 = findViewById(R.id.quizName3);
        gameMode3 = findViewById(R.id.gameMode3);
        score3 = findViewById(R.id.score3);

        playerName4 = findViewById(R.id.playerName4);
        quizName4 = findViewById(R.id.quizName4);
        gameMode4 = findViewById(R.id.gameMode4);
        score4 = findViewById(R.id.score4);

        playerName5 = findViewById(R.id.playerName5);
        quizName5 = findViewById(R.id.quizName5);
        gameMode5 = findViewById(R.id.gameMode5);
        score5 = findViewById(R.id.score5);

        playerName6 = findViewById(R.id.playerName6);
        quizName6 = findViewById(R.id.quizName6);
        gameMode6 = findViewById(R.id.gameMode6);
        score6 = findViewById(R.id.score6);

        // Boutons de chargement et suppression pour les sauvegardes
        btnLoad1 = findViewById(R.id.btnLoad1);
        btnDelete1 = findViewById(R.id.btnDelete1);

        btnLoad2 = findViewById(R.id.btnLoad2);
        btnDelete2 = findViewById(R.id.btnDelete2);

        btnLoad3 = findViewById(R.id.btnLoad3);
        btnDelete3 = findViewById(R.id.btnDelete3);

        btnLoad4 = findViewById(R.id.btnLoad4);
        btnDelete4 = findViewById(R.id.btnDelete4);

        btnLoad5 = findViewById(R.id.btnLoad5);
        btnDelete5 = findViewById(R.id.btnDelete5);

        btnLoad6 = findViewById(R.id.btnLoad6);
        btnDelete6 = findViewById(R.id.btnDelete6);


        // Charger les données depuis le fichier JSON
        loadSaveData();

        findViewById(R.id.btnRetour_LSauvegarde).setOnClickListener(v -> finish());

        // Clic sur la carte de sauvegarde 1
        cardSave1.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 1 sélectionnée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur la carte de sauvegarde 2
        cardSave2.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 2 sélectionnée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Charger" pour la sauvegarde 1
        btnLoad1.setOnClickListener(v -> {
            // Implémenter la logique pour charger la sauvegarde 1
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 1 chargée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Supprimer" pour la sauvegarde 1
        btnDelete1.setOnClickListener(v -> {
            // Implémenter la logique pour supprimer la sauvegarde 1
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 1 supprimée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Charger" pour la sauvegarde 2
        btnLoad2.setOnClickListener(v -> {
            // Implémenter la logique pour charger la sauvegarde 2
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 2 chargée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Supprimer" pour la sauvegarde 2
        btnDelete2.setOnClickListener(v -> {
            // Implémenter la logique pour supprimer la sauvegarde 2
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 2 supprimée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur la carte de sauvegarde 3
        cardSave3.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 3 sélectionnée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur la carte de sauvegarde 4
        cardSave4.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 4 sélectionnée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur la carte de sauvegarde 5
        cardSave5.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 5 sélectionnée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur la carte de sauvegarde 6
        cardSave6.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 6 sélectionnée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Charger" pour la sauvegarde 3
        btnLoad3.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 3 chargée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Supprimer" pour la sauvegarde 3
        btnDelete3.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 3 supprimée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Charger" pour la sauvegarde 4
        btnLoad4.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 4 chargée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Supprimer" pour la sauvegarde 4
        btnDelete4.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 4 supprimée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Charger" pour la sauvegarde 5
        btnLoad5.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 5 chargée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Supprimer" pour la sauvegarde 5
        btnDelete5.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 5 supprimée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Charger" pour la sauvegarde 6
        btnLoad6.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 6 chargée", Toast.LENGTH_SHORT).show();
        });

        // Clic sur le bouton "Supprimer" pour la sauvegarde 6
        btnDelete6.setOnClickListener(v -> {
            Toast.makeText(MenuSauvegarde.this, "Sauvegarde 6 supprimée", Toast.LENGTH_SHORT).show();
        });


    }

    private void loadSaveData() {
        try {
            // Charger le fichier JSON
            InputStream inputStream = getAssets().open("save_data.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            Type saveDataType = new TypeToken<SaveData>() {}.getType();
            Gson gson = new Gson();

            // Parser les données JSON dans l'objet SaveData
            SaveData saveData = gson.fromJson(reader, saveDataType);
            List<SaveData.Save> saves = saveData.getSaves();

            // Remplir les vues avec les données des sauvegardes
            if (saves.size() > 0) {
                SaveData.Save save1 = saves.get(0);
                playerName1.setText(save1.getPlayerName());
                quizName1.setText(save1.getQuizTitle());
                gameMode1.setText(save1.getMode());
                score1.setText("Score: " + save1.getScore());
            }

            if (saves.size() > 1) {
                SaveData.Save save2 = saves.get(1);
                playerName2.setText(save2.getPlayerName());
                quizName2.setText(save2.getQuizTitle());
                gameMode2.setText(save2.getMode());
                score2.setText("Score: " + save2.getScore());
            }

            if (saves.size() > 2) {
                SaveData.Save save3 = saves.get(2);
                playerName3.setText(save3.getPlayerName());
                quizName3.setText(save3.getQuizTitle());
                gameMode3.setText(save3.getMode());
                score3.setText("Score: " + save3.getScore());
            }

            if (saves.size() > 3) {
                SaveData.Save save4 = saves.get(3);
                playerName4.setText(save4.getPlayerName());
                quizName4.setText(save4.getQuizTitle());
                gameMode4.setText(save4.getMode());
                score4.setText("Score: " + save4.getScore());
            }

            if (saves.size() > 4) {
                SaveData.Save save5 = saves.get(4);
                playerName5.setText(save5.getPlayerName());
                quizName5.setText(save5.getQuizTitle());
                gameMode5.setText(save5.getMode());
                score5.setText("Score: " + save5.getScore());
            }

            if (saves.size() > 5) {
                SaveData.Save save6 = saves.get(5);
                playerName6.setText(save6.getPlayerName());
                quizName6.setText(save6.getQuizTitle());
                gameMode6.setText(save6.getMode());
                score6.setText("Score: " + save6.getScore());
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de chargement des sauvegardes", Toast.LENGTH_SHORT).show();
        }
    }
}
