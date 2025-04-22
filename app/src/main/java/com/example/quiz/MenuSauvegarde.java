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
import android.util.Log;

public class MenuSauvegarde extends AppCompatActivity {

    // Déclaration des vues (boutons et textViews)
    private static final String TAG = "MenuSauvegarde";  // Utilisation d'un TAG pour Logcat
    private final ImageButton[] btnLoadButtons = new ImageButton[6];
    private final ImageButton[] btnDeleteButtons = new ImageButton[6];
    private final TextView[] playerNames = new TextView[6];
    private final TextView[] quizNames = new TextView[6];
    private final TextView[] gameModes = new TextView[6];
    private final TextView[] gameModes2 = new TextView[6];
    private final TextView[] scores = new TextView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_sauvegarde);
        Log.d(TAG, "onCreate: Initialisation de l'activité MenuSauvegarde");

        // Définir l'action du bouton de retour
        findViewById(R.id.btnBack_LSauvegarde).setOnClickListener(v -> {
            Log.d(TAG, "onClick: Retour à l'écran précédent");
            finish();
        });

        // Initialiser les vues
        initializeViews();

        // Charger les données de sauvegarde et configurer les actions des boutons
        loadSaveData();
    }

    // Initialisation des vues
    private void initializeViews() {
        Log.d(TAG, "initializeViews: Initialisation des vues");

        // Initialiser les boutons de chargement et de suppression
        btnLoadButtons[0] = findViewById(R.id.btnLoad1);
        btnLoadButtons[1] = findViewById(R.id.btnLoad2);
        btnLoadButtons[2] = findViewById(R.id.btnLoad3);
        btnLoadButtons[3] = findViewById(R.id.btnLoad4);
        btnLoadButtons[4] = findViewById(R.id.btnLoad5);
        btnLoadButtons[5] = findViewById(R.id.btnLoad6);

        btnDeleteButtons[0] = findViewById(R.id.btnDelete1);
        btnDeleteButtons[1] = findViewById(R.id.btnDelete2);
        btnDeleteButtons[2] = findViewById(R.id.btnDelete3);
        btnDeleteButtons[3] = findViewById(R.id.btnDelete4);
        btnDeleteButtons[4] = findViewById(R.id.btnDelete5);
        btnDeleteButtons[5] = findViewById(R.id.btnDelete6);

        // Initialisation des TextViews pour les données de sauvegarde
        playerNames[0] = findViewById(R.id.playerName1);
        playerNames[1] = findViewById(R.id.playerName2);
        playerNames[2] = findViewById(R.id.playerName3);
        playerNames[3] = findViewById(R.id.playerName4);
        playerNames[4] = findViewById(R.id.playerName5);
        playerNames[5] = findViewById(R.id.playerName6);

        quizNames[0] = findViewById(R.id.quizName1);
        quizNames[1] = findViewById(R.id.quizName2);
        quizNames[2] = findViewById(R.id.quizName3);
        quizNames[3] = findViewById(R.id.quizName4);
        quizNames[4] = findViewById(R.id.quizName5);
        quizNames[5] = findViewById(R.id.quizName6);

        gameModes[0] = findViewById(R.id.gameMode1);
        gameModes[1] = findViewById(R.id.gameMode2);
        gameModes[2] = findViewById(R.id.gameMode3);
        gameModes[3] = findViewById(R.id.gameMode4);
        gameModes[4] = findViewById(R.id.gameMode5);
        gameModes[5] = findViewById(R.id.gameMode6);

        gameModes2[0] = findViewById(R.id.gameMode2_1);
        gameModes2[1] = findViewById(R.id.gameMode2_2);
        gameModes2[2] = findViewById(R.id.gameMode2_3);
        gameModes2[3] = findViewById(R.id.gameMode2_4);
        gameModes2[4] = findViewById(R.id.gameMode2_5);
        gameModes2[5] = findViewById(R.id.gameMode2_6);

        scores[0] = findViewById(R.id.score1);
        scores[1] = findViewById(R.id.score2);
        scores[2] = findViewById(R.id.score3);
        scores[3] = findViewById(R.id.score4);
        scores[4] = findViewById(R.id.score5);
        scores[5] = findViewById(R.id.score6);
    }

    // Chargement des données de sauvegarde depuis le fichier JSON
    private void loadSaveData() {
        Log.d(TAG, "loadSaveData: Tentative de chargement des données de sauvegarde");

        try {
            // Charger le fichier JSON des sauvegardes depuis les assets
            InputStream inputStream = getAssets().open("save_data.json");
            String json = convertStreamToString(inputStream);
            Gson gson = new Gson();
            SaveData saveData = gson.fromJson(json, SaveData.class);

            // Vérification si des sauvegardes existent
            List<SaveData.Save> saves = saveData.getSaves();
            if (saves.isEmpty()) {
                Log.d(TAG, "loadSaveData: Aucune sauvegarde disponible");
            }

            // Mise à jour de l'interface pour chaque sauvegarde
            for (int i = 0; i < saves.size(); i++) {
                SaveData.Save save = saves.get(i);
                setTextViewData(i + 1, save);
                setLoadButtonAction(i + 1, save);
                setDeleteButtonAction(i + 1, save);
            }

        } catch (IOException e) {
            Log.e(TAG, "loadSaveData: Erreur lors du chargement des sauvegardes", e);
            Toast.makeText(this, "Erreur de chargement des sauvegardes", Toast.LENGTH_SHORT).show();
        }
    }

    // Mise à jour des TextViews avec les informations des sauvegardes
    private void setTextViewData(int slot, SaveData.Save save) {
        Log.d(TAG, "setTextViewData: Mise à jour des TextViews pour la sauvegarde dans le slot " + slot);

        // Mise à jour des TextViews avec les informations spécifiques à chaque sauvegarde
        playerNames[slot - 1].setText(save.getPlayerName());
        quizNames[slot - 1].setText(save.getQuizTitle());
        gameModes[slot - 1].setText(save.getMode());
        gameModes2[slot - 1].setText(save.getMode2());
        scores[slot - 1].setText(getString(R.string.score_placeholder, save.getScore()));
    }

    // Définir les actions des boutons de chargement
    private void setLoadButtonAction(int slot, SaveData.Save save) {
        Log.d(TAG, "setLoadButtonAction: Définition de l'action du bouton de chargement pour le slot " + slot);
        btnLoadButtons[slot - 1].setOnClickListener(v -> loadSave(slot, save));
    }

    // Définir les actions des boutons de suppression
    private void setDeleteButtonAction(int slot, SaveData.Save save) {
        Log.d(TAG, "setDeleteButtonAction: Définition de l'action du bouton de suppression pour le slot " + slot);
        btnDeleteButtons[slot - 1].setOnClickListener(v -> deleteSave(slot, save));
    }

    // Charger une sauvegarde dans l'activité GameQuiz
    private void loadSave(int slot, SaveData.Save save) {
        Log.d(TAG, "loadSave: Chargement de la sauvegarde dans le slot " + slot);
        if (save.getPlayerName().isEmpty() || save.getQuizTitle().isEmpty() || save.getMode().isEmpty()) {
            Toast.makeText(this, "Sauvegarde vide détectée, impossible de charger", Toast.LENGTH_SHORT).show();
        } else {
            // Créer l'intention pour charger l'activité GameQuiz avec les données de sauvegarde
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

    // Supprimer une sauvegarde
    private void deleteSave(int slot, SaveData.Save save) {
        Log.d(TAG, "deleteSave: Suppression de la sauvegarde dans le slot " + slot);
        try {
            // Charger le fichier JSON des sauvegardes depuis les assets
            InputStream inputStream = getAssets().open("save_data.json");
            String json = convertStreamToString(inputStream);
            Gson gson = new Gson();
            SaveData saveData = gson.fromJson(json, SaveData.class);

            // Réinitialiser la sauvegarde
            save.reset();

            // Réécrire le fichier JSON avec les nouvelles données
            String updatedJson = gson.toJson(saveData);

            // Écrire le fichier JSON mis à jour dans un fichier interne
            try (FileOutputStream fos = openFileOutput("save_data.json", MODE_PRIVATE)) {
                fos.write(updatedJson.getBytes());
            }

            // Afficher un message de confirmation
            Toast.makeText(this, "Sauvegarde " + slot + " réinitialisée", Toast.LENGTH_SHORT).show();
            loadSaveData(); // Recharger les données après la suppression

        } catch (IOException e) {
            Log.e(TAG, "deleteSave: Erreur lors de la suppression de la sauvegarde", e);
            Toast.makeText(this, "Erreur lors de la suppression de la sauvegarde", Toast.LENGTH_SHORT).show();
        }
    }

    // Convertir le flux d'entrée en chaîne de caractères
    private String convertStreamToString(InputStream inputStream) {
        Log.d(TAG, "convertStreamToString: Conversion du flux d'entrée en chaîne de caractères");
        try (Scanner scanner = new Scanner(inputStream)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
