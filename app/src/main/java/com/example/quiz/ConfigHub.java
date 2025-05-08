package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.io.*;
import android.widget.Button;
import android.widget.Toast;

public class ConfigHub extends AppCompatActivity {
    private static final String TAG = "ConfigHub";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_hub);

        // Références UI
        EditText editPartyName = findViewById(R.id.editPartyName);
        Spinner spinnerQuiz = findViewById(R.id.spinnerQuiz);
        Spinner spinnerGameMode = findViewById(R.id.spinnerGameMode);
        CheckBox checkboxJokers = findViewById(R.id.checkboxJokers);
        EditText editMaxPlayers = findViewById(R.id.editMaxPlayers);
        EditText editPassword = findViewById(R.id.editPassword);
        Spinner spinnerServerRegion = findViewById(R.id.spinnerServerRegion);
        CheckBox checkboxTextChat = findViewById(R.id.checkboxTextChat);
        CheckBox checkboxVoiceChat = findViewById(R.id.checkboxVoiceChat);
        Switch switchPrivacy = findViewById(R.id.switchPrivacy);
        Button btnCreateGame = findViewById(R.id.btnCreateGame);
        ImageButton btnBack = findViewById(R.id.btnBack);

        // Adapter pour spinnerQuiz
        ArrayAdapter<CharSequence> quizAdapter = ArrayAdapter.createFromResource(
                this, R.array.quiz_list, android.R.layout.simple_spinner_item);
        quizAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuiz.setAdapter(quizAdapter);

        // Adapter pour spinnerGameMode
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(
                this, R.array.game_modes, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGameMode.setAdapter(modeAdapter);

        // Adapter pour spinnerServerRegion
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(
                this, R.array.server_regions, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServerRegion.setAdapter(regionAdapter);

        // Retour à la page précédente
        btnBack.setOnClickListener(v -> finish());

        // Création de la partie
        btnCreateGame.setOnClickListener(v -> {
            String partyName = editPartyName.getText().toString().trim();
            String maxPlayersStr = editMaxPlayers.getText().toString().trim();
            boolean isPrivate = !switchPrivacy.isChecked();

            if (partyName.isEmpty() || maxPlayersStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir les champs obligatoires.", Toast.LENGTH_SHORT).show();
                return;
            }

            int maxPlayers;
            try {
                maxPlayers = Integer.parseInt(maxPlayersStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Nombre de joueurs invalide.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Créer un nouvel objet de partie
                JSONObject newParty = new JSONObject();
                newParty.put("partyId", "party" + System.currentTimeMillis()); // Utilise l'horodatage pour générer un ID unique
                newParty.put("partyName", partyName);
                newParty.put("quiz", spinnerQuiz.getSelectedItem().toString());
                newParty.put("gameMode", spinnerGameMode.getSelectedItem().toString());
                newParty.put("jokers", checkboxJokers.isChecked());
                newParty.put("maxPlayers", maxPlayers);
                newParty.put("password", editPassword.getText().toString());
                newParty.put("serverRegion", spinnerServerRegion.getSelectedItem().toString());
                newParty.put("textChat", checkboxTextChat.isChecked());
                newParty.put("voiceChat", checkboxVoiceChat.isChecked());
                newParty.put("isPrivate", isPrivate);
                newParty.put("creatorId", "admin001"); // simulé
                newParty.put("players", new org.json.JSONArray().put("admin001")); // Ajoute le créateur

                // Charger les parties existantes ou en créer un nouveau fichier si nécessaire
                JSONObject allParties;
                try {
                    FileInputStream fis = openFileInput("multi_data.json");
                    byte[] data = new byte[fis.available()];
                    fis.read(data);
                    fis.close();
                    String jsonString = new String(data);
                    allParties = new JSONObject(jsonString);
                } catch (Exception e) {
                    allParties = new JSONObject();
                    allParties.put("parties", new org.json.JSONArray());
                }

                // Ajouter la nouvelle partie
                allParties.getJSONArray("parties").put(newParty);

                // Sauvegarder les parties mises à jour dans le fichier
                FileOutputStream fos = openFileOutput("multi_data.json", MODE_PRIVATE);
                fos.write(allParties.toString().getBytes());
                fos.close();

                Toast.makeText(this, "Partie créée !", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(ConfigHub.this, Hub.class));

            } catch (Exception e) {
                Log.e(TAG, "Erreur JSON", e);
                Toast.makeText(this, "Erreur de création de partie.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
