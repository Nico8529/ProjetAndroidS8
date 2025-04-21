package com.example.quiz;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.*;
import java.util.*;

public class MenuStatistique extends AppCompatActivity {

    private static final String FILE_NAME = "stat_data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_statistique);

        findViewById(R.id.btnBack_LStatistique).setOnClickListener(v -> finish());

        loadStatistics();

        Button resetBtn = findViewById(R.id.btnResetStat);
        resetBtn.setOnClickListener(v -> {
            clearStatsFile();
            loadStatistics(); // refresh UI
            Toast.makeText(this, "Statistiques réinitialisées", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadStatistics() {
        try {
            String jsonString = readFromFile();
            JSONObject data = new JSONObject(jsonString);

            ((TextView)findViewById(R.id.nbrPartie_LStatistique)).setText(String.valueOf(data.getInt("totalPlayed")));
            ((TextView)findViewById(R.id.nbrPartiePerdu_LStatistique)).setText(String.valueOf(data.getInt("totalLost")));
            ((TextView)findViewById(R.id.nbrPartieGagnée_LStatistique)).setText(String.valueOf(data.getInt("totalWon")));
            ((TextView)findViewById(R.id.nbrPartieMulti_LStatistique)).setText(String.valueOf(data.getInt("totalMultiplayer")));

            JSONArray scores = data.getJSONArray("scores");
            List<JSONObject> scoreList = new ArrayList<>();
            for (int i = 0; i < scores.length(); i++) {
                scoreList.add(scores.getJSONObject(i));
            }

            // Trier les scores
            scoreList.sort((a, b) -> {
                try {
                    return b.getInt("score") - a.getInt("score");
                } catch (JSONException e) {
                    return 0;
                }
            });

            // Afficher les 6 premiers
            for (int i = 0; i < 6; i++) {
                TextView name = findViewById(getResources().getIdentifier("nameJ" + (i+1) + "_LStatistique", "id", getPackageName()));
                TextView stat = findViewById(getResources().getIdentifier("statJ" + (i+1) + "_LStatistique", "id", getPackageName()));
                if (i < scoreList.size()) {
                    name.setText(scoreList.get(i).getString("name"));
                    stat.setText(String.valueOf(scoreList.get(i).getInt("score")));
                } else {
                    name.setText("-");
                    stat.setText("-");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFromFile() {
        File file = new File(getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            return getEmptyStats();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line);
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return getEmptyStats();
        }
    }

    private void clearStatsFile() {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(getEmptyStats().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getEmptyStats() {
        return "{ \"totalPlayed\": 0, \"totalLost\": 0, \"totalWon\": 0, \"totalMultiplayer\": 0, \"scores\": [] }";
    }
}
