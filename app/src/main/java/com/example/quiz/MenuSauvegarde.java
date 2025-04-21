package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class MenuSauvegarde extends AppCompatActivity {

    // Déclaration des vues (boutons et textViews)
    ImageButton btnLoad1, btnLoad2, btnLoad3, btnLoad4, btnLoad5, btnLoad6;
    ImageButton btnDelete1, btnDelete2, btnDelete3, btnDelete4, btnDelete5, btnDelete6;
    TextView playerName1, playerName2, playerName3, playerName4, playerName5, playerName6;
    TextView quizName1, quizName2, quizName3, quizName4, quizName5, quizName6;
    TextView gameMode1, gameMode2, gameMode3, gameMode4, gameMode5, gameMode6;
    TextView gameMode2_1, gameMode2_2, gameMode2_3, gameMode2_4, gameMode2_5, gameMode2_6;
    TextView score1, score2, score3, score4, score5, score6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_sauvegarde);
        findViewById(R.id.btnBack_LSauvegarde).setOnClickListener(v -> finish());

        // Initialiser les vues
        initializeViews();

        // Charger les données de sauvegarde et configurer les actions des boutons
        loadSaveData();
    }

    private void initializeViews() {
        btnLoad1 = findViewById(R.id.btnLoad1);
        btnLoad2 = findViewById(R.id.btnLoad2);
        btnLoad3 = findViewById(R.id.btnLoad3);
        btnLoad4 = findViewById(R.id.btnLoad4);
        btnLoad5 = findViewById(R.id.btnLoad5);
        btnLoad6 = findViewById(R.id.btnLoad6);

        btnDelete1 = findViewById(R.id.btnDelete1);
        btnDelete2 = findViewById(R.id.btnDelete2);
        btnDelete3 = findViewById(R.id.btnDelete3);
        btnDelete4 = findViewById(R.id.btnDelete4);
        btnDelete5 = findViewById(R.id.btnDelete5);
        btnDelete6 = findViewById(R.id.btnDelete6);

        // Initialisation des TextViews pour les données de sauvegarde
        playerName1 = findViewById(R.id.playerName1);
        playerName2 = findViewById(R.id.playerName2);
        playerName3 = findViewById(R.id.playerName3);
        playerName4 = findViewById(R.id.playerName4);
        playerName5 = findViewById(R.id.playerName5);
        playerName6 = findViewById(R.id.playerName6);

        quizName1 = findViewById(R.id.quizName1);
        quizName2 = findViewById(R.id.quizName2);
        quizName3 = findViewById(R.id.quizName3);
        quizName4 = findViewById(R.id.quizName4);
        quizName5 = findViewById(R.id.quizName5);
        quizName6 = findViewById(R.id.quizName6);

        gameMode1 = findViewById(R.id.gameMode1);
        gameMode2 = findViewById(R.id.gameMode2);
        gameMode3 = findViewById(R.id.gameMode3);
        gameMode4 = findViewById(R.id.gameMode4);
        gameMode5 = findViewById(R.id.gameMode5);
        gameMode6 = findViewById(R.id.gameMode6);

        gameMode2_1 = findViewById(R.id.gameMode2_1);
        gameMode2_2 = findViewById(R.id.gameMode2_2);
        gameMode2_3 = findViewById(R.id.gameMode2_3);
        gameMode2_4 = findViewById(R.id.gameMode2_4);
        gameMode2_5 = findViewById(R.id.gameMode2_5);
        gameMode2_6 = findViewById(R.id.gameMode2_6);

        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);
        score4 = findViewById(R.id.score4);
        score5 = findViewById(R.id.score5);
        score6 = findViewById(R.id.score6);
    }

    private void loadSaveData() {
        try {
            // Charger le fichier JSON des sauvegardes depuis les assets
            InputStream inputStream = getAssets().open("save_data.json");
            String json = convertStreamToString(inputStream);
            Gson gson = new Gson();
            SaveData saveData = gson.fromJson(json, SaveData.class);
            List<SaveData.Save> saves = saveData.getSaves();

            for (int i = 0; i < saves.size(); i++) {
                SaveData.Save save = saves.get(i);

                // Mettre à jour les TextViews avec les informations des sauvegardes
                setTextViewData(i + 1, save);

                // Définir les actions des boutons de chargement et de suppression
                setLoadButtonAction(i + 1, save);
                setDeleteButtonAction(i + 1, save);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de chargement des sauvegardes", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTextViewData(int slot, SaveData.Save save) {
        TextView playerName = findViewById(getResources().getIdentifier("playerName" + slot, "id", getPackageName()));
        TextView quizName = findViewById(getResources().getIdentifier("quizName" + slot, "id", getPackageName()));
        TextView gameMode = findViewById(getResources().getIdentifier("gameMode" + slot, "id", getPackageName()));
        TextView gameMode2 = findViewById(getResources().getIdentifier("gameMode2" + slot, "id", getPackageName()));
        TextView score = findViewById(getResources().getIdentifier("score" + slot, "id", getPackageName()));

        // Vérification que les vues ne sont pas nulles avant d'appeler setText
        if (playerName != null) playerName.setText(save.getPlayerName());
        if (quizName != null) quizName.setText(save.getQuizTitle());
        if (gameMode != null) gameMode.setText(save.getMode());
        if (gameMode2 != null) gameMode2.setText(save.getMode2());
        if (score != null) score.setText("Score: " + save.getScore());
    }

    private void setLoadButtonAction(int slot, SaveData.Save save) {
        ImageButton btnLoad = findViewById(getResources().getIdentifier("btnLoad" + slot, "id", getPackageName()));
        if (btnLoad != null) {
            btnLoad.setOnClickListener(v -> loadSave(slot, save));
        }
    }

    private void setDeleteButtonAction(int slot, SaveData.Save save) {
        ImageButton btnDelete = findViewById(getResources().getIdentifier("btnDelete" + slot, "id", getPackageName()));
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> deleteSave(slot, save));
        }
    }

    private void loadSave(int slot, SaveData.Save save) {
        if (save.getPlayerName().isEmpty() || save.getQuizTitle().isEmpty() || save.getMode().isEmpty()) {
            Toast.makeText(this, "Sauvegarde vide détectée, impossible de charger", Toast.LENGTH_SHORT).show();
        } else {
            // Créer l'intention pour charger l'activité GameQuiz
            Intent intent = new Intent(MenuSauvegarde.this, GameQuiz.class);
            intent.putExtra("quizId", save.getQuizId());
            intent.putExtra("mode", save.getMode());
            intent.putExtra("mode2", save.getMode2());
            intent.putExtra("quizTitle", save.getQuizTitle());
            intent.putExtra("score", save.getScore());
            intent.putExtra("currentQuestionIndex", save.getCurrentQuestionIndex());
            intent.putExtra("lives", save.getLives());
            intent.putExtra("isJoker5050Used", save.isJoker5050Used());
            intent.putExtra("isJokerSkipUsed", save.isJokerSkipUsed());
            intent.putExtra("isJokerAudienceUsed", save.isJokerAudienceUsed());

            startActivity(intent);
            Toast.makeText(this, "Sauvegarde " + slot + " chargée", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSave(int slot, SaveData.Save save) {
        try {
            // Charger le fichier JSON des sauvegardes depuis les assets
            InputStream inputStream = getAssets().open("save_data.json");
            String json = convertStreamToString(inputStream);
            Gson gson = new Gson();
            SaveData saveData = gson.fromJson(json, SaveData.class);
            List<SaveData.Save> saves = saveData.getSaves();

            // Réinitialiser la sauvegarde
            save.reset();

            // Réécrire le fichier JSON avec les nouvelles données
            String updatedJson = gson.toJson(saveData);

            // Écrire le fichier JSON mis à jour dans un fichier interne
            FileOutputStream fos = openFileOutput("save_data.json", MODE_PRIVATE);
            fos.write(updatedJson.getBytes());
            fos.close();

            // Afficher un message de confirmation
            Toast.makeText(this, "Sauvegarde " + slot + " réinitialisée", Toast.LENGTH_SHORT).show();
            loadSaveData();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la suppression de la sauvegarde", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String result = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        scanner.close();
        return result;
    }
}
