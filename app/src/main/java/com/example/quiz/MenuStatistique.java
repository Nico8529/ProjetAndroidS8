package com.example.quiz;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.util.Log;  // Import pour Logcat
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.*;
import java.util.*;

public class MenuStatistique extends AppCompatActivity {

    private static final String FILE_NAME = "stat_data.json";
    private static final String TAG = "MenuStatistique";  // Définition du TAG pour Logcat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_statistique);

        Log.d(TAG, "onCreate: Initialisation de l'activité MenuStatistique");

        // Définir un écouteur pour le bouton "Retour"
        findViewById(R.id.btnBack_LStatistique).setOnClickListener(v -> {
            Log.d(TAG, "onClick: Retour au menu précédent");
            finish();
        });

        // Charger les statistiques au démarrage
        loadStatistics();

        // Bouton pour réinitialiser les statistiques
        Button resetBtn = findViewById(R.id.btnResetStat);
        resetBtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Réinitialisation des statistiques");
            clearStatsFile();
            loadStatistics(); // Rafraîchissement de l'interface utilisateur après réinitialisation
            Toast.makeText(this, "Statistiques réinitialisées", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadStatistics() {
        try {
            Log.d(TAG, "loadStatistics: Tentative de lecture des statistiques depuis le fichier");

            String jsonString = readFromFile();
            JSONObject data = new JSONObject(jsonString);

            // Affichage des statistiques principales dans les TextViews
            Log.d(TAG, "loadStatistics: Mise à jour des statistiques sur l'interface");
            ((TextView)findViewById(R.id.nbrPartie_LStatistique)).setText(String.valueOf(data.getInt("totalPlayed")));
            ((TextView)findViewById(R.id.nbrPartiePerdu_LStatistique)).setText(String.valueOf(data.getInt("totalLost")));
            ((TextView)findViewById(R.id.nbrPartieGagnee_LStatistique)).setText(String.valueOf(data.getInt("totalWon")));
            ((TextView)findViewById(R.id.nbrPartieMulti_LStatistique)).setText(String.valueOf(data.getInt("totalMultiplayer")));

            // Lecture des scores depuis le JSON
            JSONArray scores = data.getJSONArray("scores");
            List<JSONObject> scoreList = new ArrayList<>();
            for (int i = 0; i < scores.length(); i++) {
                scoreList.add(scores.getJSONObject(i));
            }

            // Trier les scores du plus élevé au plus bas
            Log.d(TAG, "loadStatistics: Tri des scores");
            scoreList.sort((a, b) -> {
                try {
                    return b.getInt("score") - a.getInt("score");
                } catch (JSONException e) {
                    Log.e(TAG, "loadStatistics: Erreur lors du tri des scores", e);
                    return 0;
                }
            });

            // Définir les noms et scores dans les TextViews correspondants
            Log.d(TAG, "loadStatistics: Mise à jour des 6 premiers scores");
            TextView[] nameViews = {
                    findViewById(R.id.nameJ1_LStatistique),
                    findViewById(R.id.nameJ2_LStatistique),
                    findViewById(R.id.nameJ3_LStatistique),
                    findViewById(R.id.nameJ4_LStatistique),
                    findViewById(R.id.nameJ5_LStatistique),
                    findViewById(R.id.nameJ6_LStatistique)
            };

            TextView[] scoreViews = {
                    findViewById(R.id.statJ1_LStatistique),
                    findViewById(R.id.statJ2_LStatistique),
                    findViewById(R.id.statJ3_LStatistique),
                    findViewById(R.id.statJ4_LStatistique),
                    findViewById(R.id.statJ5_LStatistique),
                    findViewById(R.id.statJ6_LStatistique)
            };

            // Afficher les 6 premiers scores ou un tiret s'il y a moins de scores
            for (int i = 0; i < 6; i++) {
                if (i < scoreList.size()) {
                    nameViews[i].setText(scoreList.get(i).getString("name"));
                    scoreViews[i].setText(String.valueOf(scoreList.get(i).getInt("score")));
                    Log.d(TAG, "loadStatistics: Score " + (i+1) + " - " + scoreList.get(i).getString("name") + ": " + scoreList.get(i).getInt("score"));
                } else {
                    nameViews[i].setText("-");
                    scoreViews[i].setText("-");
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "loadStatistics: Erreur lors du chargement des statistiques", e);
        }
    }

    private String readFromFile() {
        Log.d(TAG, "readFromFile: Tentative de lecture du fichier de statistiques");
        File file = new File(getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            Log.d(TAG, "readFromFile: Fichier non trouvé, retour aux statistiques vides");
            return getEmptyStats();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            Log.e(TAG, "readFromFile: Erreur lors de la lecture du fichier", e);
            return getEmptyStats();
        }
    }

    private void clearStatsFile() {
        Log.d(TAG, "clearStatsFile: Tentative de réinitialisation des statistiques");
        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(getEmptyStats().getBytes());
            Log.d(TAG, "clearStatsFile: Fichier de statistiques réinitialisé");
        } catch (IOException e) {
            Log.e(TAG, "clearStatsFile: Erreur lors de la réinitialisation des statistiques", e);
        }
    }

    private String getEmptyStats() {
        Log.d(TAG, "getEmptyStats: Retour aux statistiques vides");
        return "{ \"totalPlayed\": 0, \"totalLost\": 0, \"totalWon\": 0, \"totalMultiplayer\": 0, \"scores\": [] }";
    }
}
