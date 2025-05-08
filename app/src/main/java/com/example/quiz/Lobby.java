package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Lobby extends AppCompatActivity {

    private RecyclerView recyclerViewLobbies;
    private LobbyAdapter lobbyAdapter;
    private List<Party> partyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);

        // Bouton retour
        findViewById(R.id.btnBack_Lobby).setOnClickListener(v -> finish());

        // RecyclerView setup
        recyclerViewLobbies = findViewById(R.id.recyclerViewLobbies);
        recyclerViewLobbies.setLayoutManager(new LinearLayoutManager(this));

        // Load parties from multi_data.json
        loadPartiesFromJson();

        // Setup the adapter
        lobbyAdapter = new LobbyAdapter(partyList, new LobbyAdapter.OnLobbyClickListener() {
            @Override
            public void onLobbyClick(Party party) {
                // Redirect to HubActivity, passing the party details
                Intent intent = new Intent(Lobby.this, Hub.class);
                intent.putExtra("partyId", party.getPartyId());  // Pass partyId to Hub
                startActivity(intent);
            }
        });
        recyclerViewLobbies.setAdapter(lobbyAdapter);
    }

    // Method to load parties from multi_data.json
    private void loadPartiesFromJson() {
        try {
            InputStreamReader reader = new InputStreamReader(getAssets().open("multi_data.json"));
            PartyList partyListObject = new Gson().fromJson(reader, PartyList.class);
            if (partyListObject != null) {
                partyList = partyListObject.getParties();
            } else {
                partyList = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load parties", Toast.LENGTH_SHORT).show();
        }
    }
}
