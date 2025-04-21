package com.example.quiz;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import java.io.*;

public class MenuParametre extends AppCompatActivity {

    private SeekBar audioSeekBar, systemSeekBar;
    private Switch autoSaveSwitch, bonusSwitch, divSwitch;
    private Spinner languageSpinner;
    private TextView audioValue, systemValue;

    private final String fileName = "param_data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_parametre);

        // Liaison des vues
        audioSeekBar = findViewById(R.id.audio1_LParametre);
        systemSeekBar = findViewById(R.id.audio2_LParametre);
        autoSaveSwitch = findViewById(R.id.sauvegardeAuto_LParametre);
        bonusSwitch = findViewById(R.id.bonus_LParametre);
        divSwitch = findViewById(R.id.div_LParametre);
        languageSpinner = findViewById(R.id.selectLanguage_LParametre);
        audioValue = findViewById(R.id.text1_LParametre);
        systemValue = findViewById(R.id.text2_LParametre);

        // Affichage initial des valeurs SeekBar
        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioValue.setText("Audio: " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        systemSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                systemValue.setText("Système: " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.btnBack_LParametre).setOnClickListener(v -> finish());

        // Chargement des préférences
        loadSettings();

        // Bouton Sauvegarder
        findViewById(R.id.btnSauvegarder).setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(this, "Paramètres sauvegardés !", Toast.LENGTH_SHORT).show();
        });

        // Bouton Par défaut
        findViewById(R.id.btnDefault).setOnClickListener(v -> {
            resetToDefault();
            Toast.makeText(this, "Paramètres réinitialisés !", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveSettings() {
        try {
            JSONObject settings = new JSONObject();
            settings.put("audio", audioSeekBar.getProgress());
            settings.put("system", systemSeekBar.getProgress());
            settings.put("autoSave", autoSaveSwitch.isChecked());
            settings.put("bonus", bonusSwitch.isChecked());
            settings.put("div", divSwitch.isChecked());
            settings.put("language", languageSpinner.getSelectedItemPosition());

            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(settings.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() {
        try {
            File file = new File(getFilesDir(), fileName);
            if (!file.exists()) return;

            FileInputStream fis = openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            JSONObject settings = new JSONObject(sb.toString());

            audioSeekBar.setProgress(settings.getInt("audio"));
            systemSeekBar.setProgress(settings.getInt("system"));
            autoSaveSwitch.setChecked(settings.getBoolean("autoSave"));
            bonusSwitch.setChecked(settings.getBoolean("bonus"));
            divSwitch.setChecked(settings.getBoolean("div"));
            languageSpinner.setSelection(settings.getInt("language"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetToDefault() {
        audioSeekBar.setProgress(50);
        systemSeekBar.setProgress(50);
        autoSaveSwitch.setChecked(false);
        bonusSwitch.setChecked(false);
        divSwitch.setChecked(false);
        languageSpinner.setSelection(0);
    }
}
