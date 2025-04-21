package com.example.quiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Chargement extends AppCompatActivity {

    private TextView loadingText;
    private ProgressBar progressBar;

    private int progress = 0;
    private Handler handler = new Handler();

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

        loadingText = findViewById(R.id.text_LChargement);
        progressBar = findViewById(R.id.title_LChargement);

        // Préparer et jouer le MediaPlayer immédiatement avec listener
        final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.amen);
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release(); // libérer la mémoire après lecture
                navigateToNextActivity(); // Passer à l'activité suivante seulement après que le son ait fini
            });
            mediaPlayer.start();  // Lancer le son après avoir attaché le listener
        }

        // Lancer la séquence de chargement
        startLoadingSequence();

        // Fallback : forcer la navigation si le son ne se joue pas correctement
        handler.postDelayed(() -> {
            if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                navigateToNextActivity();
            }
        }, 5000); // 5 secondes max d'attente
    }

    private void startLoadingSequence() {
        progressBar.setMax(100);
        simulateLoadingStep(0);
    }

    private void simulateLoadingStep(int stepIndex) {
        if (stepIndex < loadingSteps.length) {
            loadingText.setText(loadingSteps[stepIndex]);
            progress += 100 / loadingSteps.length;
            progressBar.setProgress(progress);

            handler.postDelayed(() -> simulateLoadingStep(stepIndex + 1), 400);
        }
    }

    private void navigateToNextActivity() {
        Intent intent = new Intent(Chargement.this, MenuDuJeu.class);
        startActivity(intent);
        finish();
    }
}
