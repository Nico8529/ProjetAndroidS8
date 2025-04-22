package com.example.quiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class MenuDuJeu extends AppCompatActivity {

    private MediaPlayer mediaPlayer;  // Déclaration du MediaPlayer pour la musique de fond

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_du_jeu);

        //Log : Initialisation de l'activité MenuDuJeu
        Log.d("MenuDuJeu", "Activité MenuDuJeu initialisée.");

        //Récupérer le logo et appliquer une animation lorsque l'utilisateur clique dessus
        ImageView logo = findViewById(R.id.logoImage);
        android.view.animation.Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);

        //Log : Animation du logo chargée
        Log.d("MenuDuJeu", "Animation de rotation chargée pour le logo.");

        //Appliquer l'animation lorsque l'utilisateur clique sur le logo
        logo.setOnClickListener(v -> {
            logo.startAnimation(rotateAnimation);
            //Log : Animation du logo lancée après le clic
            Log.d("MenuDuJeu", "Logo cliqué, animation de rotation lancée.");
        });

        //Initialisation des boutons du menu
        Button btnContinue = findViewById(R.id.btnContinue_LMenuDuJeu);
        Button btnNewGame = findViewById(R.id.btnNewGame_LMenuDuJeu);
        Button btnStat = findViewById(R.id.btnStat_LMenuDuJeu);
        Button btnSave = findViewById(R.id.btnSave_LMenuDuJeu);
        Button btnParam = findViewById(R.id.btnParam_LMenuDuJeu);
        Button btnExit = findViewById(R.id.btnExit_LMenuDuJeu);

        //Log : Boutons du menu initialisés
        Log.d("MenuDuJeu", "Boutons du menu initialisés.");

        //Démarrer la musique de fond lorsque le menu est chargé
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.surprise);  // Remplace par le fichier audio de ton choix
        mediaPlayer.setLooping(true);  // Faire jouer la musique en boucle

        //Log : Musique de fond prête à être lancée
        Log.d("MenuDuJeu", "Musique de fond prête, en boucle.");

        // Lancer la musique immédiatement (optionnel selon ton choix)
        mediaPlayer.start();
        //PS ne pas lancer si tu tiens à tes oreilles mdr
        // Log : Musique de fond lancée
        //Log.d("MenuDuJeu", "Musique de fond lancée.") ;

        // Configuration des écouteurs pour chaque bouton
        btnContinue.setOnClickListener(v -> {
            // Log : L'utilisateur a cliqué sur le bouton Continuer
            Log.d("MenuDuJeu", "Bouton 'Continuer' cliqué.");
            startActivity(new Intent(MenuDuJeu.this, CreationQuiz.class));  // Lancer l'activité de sélection du mode de jeu
        });

        btnNewGame.setOnClickListener(v -> {
            // Log : L'utilisateur a cliqué sur le bouton Nouveau jeu
            Log.d("MenuDuJeu", "Bouton 'Nouveau Jeu' cliqué.");
            startActivity(new Intent(MenuDuJeu.this, SelectionMode.class));  // Lancer l'activité de sélection du mode de jeu
        });

        btnStat.setOnClickListener(v -> {
            // Log : L'utilisateur a cliqué sur le bouton Statistiques
            Log.d("MenuDuJeu", "Bouton 'Statistiques' cliqué.");
            startActivity(new Intent(MenuDuJeu.this, MenuStatistique.class));  // Lancer l'activité de statistiques
        });

        btnSave.setOnClickListener(v -> {
            // Log : L'utilisateur a cliqué sur le bouton Sauvegarde
            Log.d("MenuDuJeu", "Bouton 'Sauvegarde' cliqué.");
            startActivity(new Intent(MenuDuJeu.this, MenuSauvegarde.class));  // Lancer l'activité de sauvegarde
        });

        btnParam.setOnClickListener(v -> {
            // Log : L'utilisateur a cliqué sur le bouton Paramètres
            Log.d("MenuDuJeu", "Bouton 'Paramètres' cliqué.");
            startActivity(new Intent(MenuDuJeu.this, MenuParametre.class));  // Lancer l'activité de paramètres
        });

        btnExit.setOnClickListener(v -> {
            // Log : L'utilisateur a cliqué sur le bouton Quitter
            Log.d("MenuDuJeu", "Bouton 'Quitter' cliqué.");
            mediaPlayer.stop();  // Arrêter la musique de fond
            mediaPlayer.release();  // Libérer les ressources du MediaPlayer
            finishAffinity();  // Fermer l'application
            Log.d("MenuDuJeu", "Musique arrêtée, application fermée.");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Libérer les ressources du MediaPlayer si nécessaire
        if (mediaPlayer != null) {
            mediaPlayer.release();  // Libération des ressources pour éviter les fuites mémoire
            Log.d("MenuDuJeu", "Ressources du MediaPlayer libérées lors de la destruction de l'activité.");
        }
    }
}
