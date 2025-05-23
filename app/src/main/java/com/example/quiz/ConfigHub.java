package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ConfigHub extends AppCompatActivity {
    private static final String TAG = "ConfigHub";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_hub);

        Log.d(TAG, "ConfigHub Activity créée");

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

        // Bouton retour
        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "Bouton retour cliqué");
            finish();
        });

        // Création de la partie
        btnCreateGame.setOnClickListener(v -> {
            Log.d(TAG, "Tentative de création de partie");

            String partyName = editPartyName.getText().toString().trim();
            String maxPlayersStr = editMaxPlayers.getText().toString().trim();
            boolean isPrivate = !switchPrivacy.isChecked(); // true = privé

            if (partyName.isEmpty() || maxPlayersStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir les champs obligatoires.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Champs obligatoires manquants");
                return;
            }

            int maxPlayers;
            try {
                maxPlayers = Integer.parseInt(maxPlayersStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Nombre de joueurs invalide.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Format de nombre invalide pour maxPlayers", e);
                return;
            }

            try {
                // Création de l'objet JSON pour la nouvelle partie
                JSONObject newParty = new JSONObject();
                String partyId = "party" + System.currentTimeMillis();
                newParty.put("partyId", partyId);
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
                newParty.put("creatorId", "admin001"); // Simulé pour l'instant
                newParty.put("players", new JSONArray().put("admin001"));

                Log.i(TAG, "Nouvelle partie créée : " + partyId);

                // Charger ou créer le fichier JSON multi_data.json
                JSONObject allParties;
                try (FileInputStream fis = openFileInput("multi_data.json")) {
                    byte[] data = new byte[fis.available()];
                    int bytesRead = fis.read(data); // ✅ Correction du warning
                    Log.d(TAG, "Chargement de " + bytesRead + " octets depuis multi_data.json");
                    String jsonString = new String(data);
                    allParties = new JSONObject(jsonString);
                } catch (Exception e) {
                    Log.w(TAG, "Fichier multi_data.json introuvable, création d’un nouveau.");
                    allParties = new JSONObject();
                    allParties.put("parties", new JSONArray());
                }

                // Ajout de la nouvelle partie
                allParties.getJSONArray("parties").put(newParty);
                Log.i(TAG, "Partie ajoutée au fichier JSON");

                // Sauvegarde du fichier
                try (FileOutputStream fos = openFileOutput("multi_data.json", MODE_PRIVATE)) {
                    fos.write(allParties.toString().getBytes());
                    Log.d(TAG, "multi_data.json mis à jour");
                }

                Toast.makeText(this, "Partie créée !", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Redirection vers Hub.java");
                Intent intent = new Intent(ConfigHub.this, Hub.class);
                intent.putExtra("justCreated", true); // Pour que Lobby affiche un message ou recharge les données
                startActivity(intent);


            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de la création de la partie", e);
                Toast.makeText(this, "Erreur de création de partie.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}