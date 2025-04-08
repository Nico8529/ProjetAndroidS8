package com.example.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MenuParametre extends AppCompatActivity {

    private SeekBar audioSeekBar;
    private SeekBar systemSeekBar;
    private Switch autoSaveSwitch;
    private Switch bonusSwitch;
    private Switch divSwitch;
    private Spinner languageSpinner;
    private TextView audioValue;
    private TextView systemValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_parametre);

        // Initialisation des composants
        audioSeekBar = findViewById(R.id.audio1_LParametre);
        systemSeekBar = findViewById(R.id.audio2_LParametre);
        autoSaveSwitch = findViewById(R.id.sauvegardeAuto_LParametre);
        bonusSwitch = findViewById(R.id.bonus_LParametre);
        divSwitch = findViewById(R.id.div_LParametre);
        languageSpinner = findViewById(R.id.selectLanguage_LParametre);

        // Initialisation des TextView pour afficher les valeurs des SeekBar
        audioValue = findViewById(R.id.text1_LParametre);
        systemValue = findViewById(R.id.text2_LParametre);

        // Configuration du bouton de retour
        findViewById(R.id.btnBack_LParametre).setOnClickListener(v -> finish());

        // Configuration des écouteurs pour les SeekBars
        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Mettre à jour la valeur de l'audio
                audioValue.setText("Audio: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        systemSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Mettre à jour la valeur du système
                systemValue.setText("Système: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Configuration des écouteurs pour les Switch
        autoSaveSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Gérer l'état de la sauvegarde automatique
        });

        bonusSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Gérer l'état du bonus
        });

        divSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Gérer l'état de div
        });

        // Configuration de l'écouteur pour le Spinner
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Gérer la sélection de la langue
                String selectedLanguage = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
