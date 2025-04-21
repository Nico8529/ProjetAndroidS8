package com.example.quiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper; // Importation nécessaire pour Looper
import android.util.Log; // Importation pour les logs
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Chargement extends AppCompatActivity {

    // Déclaration des vues et variables nécessaires pour la gestion du chargement
    private TextView loadingText;
    private ProgressBar progressBar;

    private int progress = 0; // Progression de la barre de chargement
    private final Handler handler = new Handler(Looper.getMainLooper()); // Handler associé à Looper principal (pas obsolète)

    // Tableau des étapes de chargement à afficher
    private final String[] loadingSteps = {
            "Chargement des données...",
            "Initialisation des paramètres...",
            "Récupération des scores...",
            "Optimisation finale...",
            "Vérification de la connexion réseau...",
            "Mise à jour des ressources...",
            "Chargement des images et icônes...",
            "Préparation de l'interface utilisateur...",
            "Chargement des utilisateurs récents...",
            "Initialisation des fonctionnalités avancées...",
            "Récupération des paramètres de l'utilisateur...",
            "Préparation des animations...",
            "Synchronisation des sauvegardes...",
            "Finalisation de l'application..."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chargement);

        // Initialisation du logo et application de l'animation de rotation
        ImageView logo = findViewById(R.id.logo_LChargement);
        android.view.animation.Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);

        // Log pour l'initialisation de l'animation
        Log.d("Chargement", "Logo initialisé, animation de rotation prête.");

        // Appliquer l'animation lorsque l'utilisateur clique sur le logo
        logo.setOnClickListener(v -> {
            logo.startAnimation(rotateAnimation);
            Log.d("Chargement", "Logo cliqué, animation de rotation lancée.");
        });

        // Initialiser les éléments de la vue pour le texte et la barre de progression
        loadingText = findViewById(R.id.text_LChargement);
        progressBar = findViewById(R.id.title_LChargement);

        // Log pour la vue de chargement
        Log.d("Chargement", "Vues de chargement initialisées.");

        // Préparer et jouer le MediaPlayer avec un listener
        final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.amen);
        if (mediaPlayer != null) {
            // Lorsque le son est terminé, libérer le MediaPlayer et passer à l'activité suivante
            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d("Chargement", "Le son est terminé, passage à l'activité suivante.");
                mp.release(); // Libérer les ressources du MediaPlayer
                navigateToNextActivity(); // Passer à l'activité suivante après que le son est terminé
            });

            // Lancer la lecture du son
            mediaPlayer.start();
            Log.d("Chargement", "Son lancé.");
        }

        // Démarrer la séquence de chargement
        startLoadingSequence();

        // Délai de secours si le son ne se joue pas correctement
        handler.postDelayed(() -> {
            if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                Log.d("Chargement", "Délai de secours, passage à l'activité suivante.");
                navigateToNextActivity();
            }
        }, 5000); // Si le son ne joue pas ou prend trop de temps, passer à l'activité suivante après 5 secondes
    }

    // Méthode pour démarrer la séquence de chargement
    private void startLoadingSequence() {
        progressBar.setMax(100); // Définir la barre de progression à une échelle de 100
        simulateLoadingStep(0); // Démarrer la simulation de la séquence de chargement
        Log.d("Chargement", "Séquence de chargement démarrée.");
    }

    // Méthode pour simuler les étapes de chargement, appelée récursivement pour chaque étape
    private void simulateLoadingStep(int stepIndex) {
        if (stepIndex < loadingSteps.length) {
            loadingText.setText(loadingSteps[stepIndex]); // Afficher l'étape actuelle
            progress += 100 / loadingSteps.length; // Calculer la progression
            progressBar.setProgress(progress); // Mettre à jour la barre de progression

            // Log pour chaque étape
            Log.d("Chargement", "Étape " + stepIndex + ": " + loadingSteps[stepIndex] + ", Progression: " + progress + "%");

            // Appeler la méthode récursivement pour passer à l'étape suivante après un délai
            handler.postDelayed(() -> simulateLoadingStep(stepIndex + 1), 400);
        }
    }

    // Méthode pour passer à l'activité suivante après le chargement
    private void navigateToNextActivity() {
        Log.d("Chargement", "Navigation vers l'activité suivante.");
        // Créer une nouvelle Intent pour démarrer l'activité MenuDuJeu
        Intent intent = new Intent(Chargement.this, MenuDuJeu.class);
        startActivity(intent);
        finish(); // Fermer l'activité de chargement
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Libérer les ressources du MediaPlayer si nécessaire
        Log.d("Chargement", "Activité détruite, nettoyage effectué.");
    }
}
