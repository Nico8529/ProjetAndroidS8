package com.example.quiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuDuJeu extends AppCompatActivity {

    private MediaPlayer mediaPlayer;  // Déclaration du MediaPlayer pour la musique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_du_jeu);

        // Initialisation des boutons
        Button btnContinue = findViewById(R.id.btnContinue_LMenuDuJeu);
        Button btnNewGame = findViewById(R.id.btnNewGame_LMenuDuJeu);
        Button btnStat = findViewById(R.id.btnStat_LMenuDuJeu);
        Button btnSave = findViewById(R.id.btnSave_LMenuDuJeu);
        Button btnParam = findViewById(R.id.btnParam_LMenuDuJeu);
        Button btnExit = findViewById(R.id.btnExit_LMenuDuJeu);

        // Démarrer la musique de fond quand le menu est chargé
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.surprise);  // Remplace par le nom de ton fichier audio
        mediaPlayer.setLooping(true);  // Optionnel : pour que la musique se répète en boucle
        mediaPlayer.start();  // Lancer la musique immédiatement

        // Configuration des écouteurs pour chaque bouton
        btnContinue.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, SelectionMode.class)));

        btnNewGame.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, SelectionMode.class)));

        btnStat.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, MenuStatistique.class)));

        btnSave.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, MenuSauvegarde.class)));

        btnParam.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, MenuParametre.class)));

        btnExit.setOnClickListener(v -> {
            mediaPlayer.stop();  // Arrêter la musique quand l'utilisateur quitte l'application
            finishAffinity();    // Ferme l'application
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();  // Libérer les ressources du MediaPlayer quand l'activité est détruite
        }
    }
}
