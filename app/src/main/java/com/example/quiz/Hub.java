package com.example.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class Hub extends AppCompatActivity {

    private LinearLayout playersList;
    private Button btnStartGame, btnEditSettings, btnDeleteHub;
    private ImageButton btnBack;

    private Party currentParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hub);

        // Initialisation des composants
        playersList = findViewById(R.id.playersList);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnEditSettings = findViewById(R.id.btnEditSettings);
        btnDeleteHub = findViewById(R.id.btnDeleteHub);
        btnBack = findViewById(R.id.btnBack);

        // Retour arrière
        btnBack.setOnClickListener(v -> onBackPressed());

        // Lancement de la partie
        btnStartGame.setOnClickListener(v -> {
            if (playersList.getChildCount() >= 2) {
                // TODO : Lancer la partie ici
            }
        });

        btnEditSettings.setOnClickListener(v -> {
            // TODO : Rediriger vers les paramètres
        });

        btnDeleteHub.setOnClickListener(v -> {
            // TODO : Supprimer la partie
        });

        loadPartyData();
        if (currentParty != null) {
            updatePlayersList();
        }
    }

    private void loadPartyData() {
        String json = loadJSONFromAsset("multi_data.json");
        if (json != null) {
            Gson gson = new Gson();
            PartyList partyList = gson.fromJson(json, PartyList.class);
            String targetPartyId = "party001"; // À adapter dynamiquement

            for (Party party : partyList.parties) {
                if (party.partyId.equals(targetPartyId)) {
                    currentParty = party;
                    break;
                }
            }
        }
    }

    private void updatePlayersList() {
        playersList.removeAllViews();

        Iterator<String> iterator = currentParty.players.iterator();
        while (iterator.hasNext()) {
            String playerName = iterator.next();
            View playerView = LayoutInflater.from(this).inflate(R.layout.item_player, playersList, false);

            TextView txtName = playerView.findViewById(R.id.txtPlayerName);
            ImageButton btnEject = playerView.findViewById(R.id.btnEject);

            txtName.setText(playerName);

            btnEject.setOnClickListener(v -> {
                iterator.remove(); // Retirer de la liste
                updatePlayersList(); // Mise à jour affichage
                // TODO : Sauvegarder modif si connecté à Firebase ou fichier
            });

            playersList.addView(playerView);
        }
    }

    private String loadJSONFromAsset(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
