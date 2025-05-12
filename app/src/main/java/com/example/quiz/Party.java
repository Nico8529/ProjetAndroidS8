package com.example.quiz;

import java.util.Collection;
import java.util.List;

public class Party {
    public String partyId;
    public String partyName;
    public String quiz;
    public String gameMode;
    public boolean jokers;
    public int maxPlayers;
    public int presencePlayers;
    public String password;
    public String serverRegion;
    public boolean textChat;
    public boolean voiceChat;
    public boolean isPrivate;
    public String creatorId;
    public List<String> players;

    public String getPartyName() {
        return partyName;
    }

    public String getQuiz() {
        return quiz;
    }

    public String getGameMode() {
        return gameMode;
    }

    public List<String> getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getPassword() {
        return password;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getPartyId() {
        return partyId;
    }
}
