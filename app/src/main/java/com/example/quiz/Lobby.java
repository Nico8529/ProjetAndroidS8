package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Lobby extends AppCompatActivity {

    private static final String TAG = "Lobby";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);

        Log.d(TAG, "Lobby activity created.");

        // Bouton retour
        findViewById(R.id.btnBack_Lobby).setOnClickListener(v -> {
            Log.d(TAG, "Retour en arrière depuis le Lobby.");
            finish();
        });

        // Initialisation de la RecyclerView
        RecyclerView recyclerViewLobbies = findViewById(R.id.recyclerViewLobbies);
        recyclerViewLobbies.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "RecyclerView initialisée.");

        // Chargement des parties depuis le fichier JSON
        List<Party> partyList = loadPartiesFromJson();

        // Création de l'adapter avec un listener en lambda
        LobbyAdapter lobbyAdapter = new LobbyAdapter(partyList, party -> {
            Log.d(TAG, "Lobby sélectionné : " + party.getPartyId());
            Intent intent = new Intent(Lobby.this, Hub.class);
            intent.putExtra("partyId", party.getPartyId());  // Passage de l'ID au Hub
            startActivity(intent);
        });

        // Attachement de l'adapter
        recyclerViewLobbies.setAdapter(lobbyAdapter);
    }


    private List<Party> loadPartiesFromJson() {
        Log.d(TAG, "Chargement des parties depuis multi_data.json...");
        try (InputStreamReader reader = new InputStreamReader(getAssets().open("multi_data.json"))) {
            PartyList partyListObject = new Gson().fromJson(reader, PartyList.class);
            if (partyListObject != null) {
                Log.d(TAG, "Chargement réussi. Nombre de parties : " + partyListObject.getParties().size());
                return partyListObject.getParties();
            } else {
                Log.w(TAG, "Aucune partie trouvée.");
                return new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors du chargement des parties", e);
            Toast.makeText(this, "Échec du chargement des parties", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
    }
}