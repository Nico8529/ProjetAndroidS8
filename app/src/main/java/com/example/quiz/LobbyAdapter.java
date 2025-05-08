package com.example.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {

    private List<Party> partyList;
    private OnLobbyClickListener listener;

    public LobbyAdapter(List<Party> partyList, OnLobbyClickListener listener) {
        this.partyList = partyList;
        this.listener = listener;
    }

    @Override
    public LobbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lobby, parent, false);
        return new LobbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LobbyViewHolder holder, int position) {
        Party party = partyList.get(position);
        holder.lobbyName.setText(party.getPartyName());
        holder.itemView.setOnClickListener(v -> listener.onLobbyClick(party));
    }

    @Override
    public int getItemCount() {
        return partyList.size();
    }

    public interface OnLobbyClickListener {
        void onLobbyClick(Party party);
    }

    public static class LobbyViewHolder extends RecyclerView.ViewHolder {
        TextView lobbyName;

        public LobbyViewHolder(View itemView) {
            super(itemView);
            lobbyName = itemView.findViewById(R.id.lobbyName);
        }
    }
}
