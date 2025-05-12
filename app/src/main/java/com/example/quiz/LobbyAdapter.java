package com.example.quiz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        holder.lobbyName.setText(party.getPartyName());
        holder.quizAndMode.setText("Quiz: " + party.getQuiz() + " | Mode: " + party.getGameMode());
        holder.playerCount.setText("[" + party.getPlayers().size() + " / " + party.getMaxPlayers() + "]");
        holder.adminName.setText("Créateur: " + party.getCreatorId());

        // Afficher cadenas si la partie a un mot de passe
        if (party.getPassword() != null && !party.getPassword().isEmpty()) {
            holder.lockIcon.setVisibility(View.VISIBLE);
        } else {
            holder.lockIcon.setVisibility(View.GONE);
        }

        // Public ou privé
        holder.privacyStatus.setText(party.isPrivate() ? "Privé" : "Public");

        holder.itemView.setOnClickListener(v -> listener.onLobbyClick(party));
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

        final TextView lobbyName;
        final TextView quizAndMode;
        final TextView playerCount;
        final TextView adminName;
        final ImageView lockIcon;
        final TextView privacyStatus;

        public LobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            lobbyName = itemView.findViewById(R.id.lobbyName);
            quizAndMode = itemView.findViewById(R.id.quizAndMode);
            playerCount = itemView.findViewById(R.id.playerCount);
            adminName = itemView.findViewById(R.id.adminName);
            lockIcon = itemView.findViewById(R.id.lockIcon);
            privacyStatus = itemView.findViewById(R.id.privacyStatus);
        }
    }

}