package com.example.quiz;

import java.util.List;

public class Party {

    private String partyId;
    private String partyName;
    private String quiz;
    private String gameMode;
    private boolean jokers;
    private int maxPlayers;
    private String password;
    private String serverRegion;
    private boolean textChat;
    private boolean voiceChat;
    private boolean isPrivate;
    private String creatorId;
    private List<String> players;

    // Getters and setters
    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isJokers() {
        return jokers;
    }

    public void setJokers(boolean jokers) {
        this.jokers = jokers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerRegion() {
        return serverRegion;
    }

    public void setServerRegion(String serverRegion) {
        this.serverRegion = serverRegion;
    }

    public boolean isTextChat() {
        return textChat;
    }

    public void setTextChat(boolean textChat) {
        this.textChat = textChat;
    }

    public boolean isVoiceChat() {
        return voiceChat;
    }

    public void setVoiceChat(boolean voiceChat) {
        this.voiceChat = voiceChat;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
