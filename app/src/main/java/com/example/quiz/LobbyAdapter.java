package com.example.quiz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Adapter pour afficher une liste de salons (lobbies) dans un RecyclerView
public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {

    private static final String TAG = "LobbyAdapter";

    // Liste des parties à afficher (salons)
    private final List<Party> partyList;

    // Listener pour gérer les clics sur un item
    private final OnLobbyClickListener listener;

    // Constructeur de l'adapter
    public LobbyAdapter(List<Party> partyList, OnLobbyClickListener listener) {
        this.partyList = partyList;
        this.listener = listener;
        Log.d(TAG, "LobbyAdapter initialized with " + partyList.size() + " parties");
    }

    // Création du ViewHolder (une cellule de la liste)
    @NonNull
    @Override
    public LobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lobby, parent, false);
        return new LobbyViewHolder(view);
    }

    // Liaison des données à une cellule donnée
    @Override
    public void onBindViewHolder(@NonNull LobbyViewHolder holder, int position) {
        Party party = partyList.get(position);
        Log.d(TAG, "onBindViewHolder called for position " + position + " with party: " + party.getPartyName());

        holder.lobbyName.setText(party.getPartyName());

        // Gérer le clic sur un lobby
        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "Lobby clicked: " + party.getPartyName() + " (ID: " + party.getPartyId() + ")");
            listener.onLobbyClick(party);
        });
    }

    // Retourne le nombre total d'éléments dans la liste
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount called: " + partyList.size());
        return partyList.size();
    }

    // Interface pour gérer les clics sur un lobby
    public interface OnLobbyClickListener {
        void onLobbyClick(Party party);
    }

    // Classe interne représentant une cellule de RecyclerView
    public static class LobbyViewHolder extends RecyclerView.ViewHolder {

        // Référence au TextView qui affichera le nom du lobby
        final TextView lobbyName;

        public LobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            lobbyName = itemView.findViewById(R.id.lobbyName);
            Log.d(TAG, "LobbyViewHolder initialized");
        }
    }
}
