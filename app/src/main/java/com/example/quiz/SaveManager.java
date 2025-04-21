package com.example.quiz;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SaveManager {

    private static final String SAVE_PREFS = "quiz_saves";  // Nom du fichier de préférences
    private static final String SAVE_KEY = "saves";  // Clé pour récupérer les données

    // Charger les sauvegardes depuis SharedPreferences
    public static SaveData loadSaves(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SAVE_PREFS, Context.MODE_PRIVATE);
        String json = prefs.getString(SAVE_KEY, "");  // Si vide, aucune sauvegarde
        Gson gson = new Gson();
        Type saveListType = new TypeToken<List<SaveData.Save>>(){}.getType(); // Définir le type de la liste
        if (!json.isEmpty()) {
            // Si des données sont présentes, les désérialiser
            SaveData saveData = new SaveData();
            saveData.setSaves(gson.fromJson(json, saveListType));
            return saveData; // Retourner les données chargées
        }
        return new SaveData(); // Retourner une nouvelle instance vide si aucune donnée n'est trouvée
    }

    // Sauvegarder les données dans SharedPreferences
    public static void saveSaves(Context context, SaveData saveData) {
        SharedPreferences prefs = context.getSharedPreferences(SAVE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(saveData.getSaves()); // Sérialiser la liste de sauvegardes
        editor.putString(SAVE_KEY, json);
        editor.apply();  // Appliquer les changements
    }
}
