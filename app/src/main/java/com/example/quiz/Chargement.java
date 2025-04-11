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

    // Étapes à simuler (modifiable selon ton app)
    private final String[] loadingSteps = {
            "Chargement des données...",
            "Initialisation des paramètres...",
            "Récupération des scores...",
            "Optimisation finale..."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chargement);

        loadingText = findViewById(R.id.text_LChargement);
        progressBar = findViewById(R.id.title_LChargement);

        // Préparer le MediaPlayer et le jouer
        final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.amen);
        if (mediaPlayer != null) {
            mediaPlayer.start();  // Lancer le son immédiatement
        }

        // Lancement de la séquence de chargement
        startLoadingSequence();

        // Une fois le chargement terminé, passer à l'activité suivante après la fin du son
        handler.postDelayed(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release(); // libérer la mémoire après lecture
                    navigateToNextActivity(); // Passer à l'activité suivante seulement après que le son ait fini
                });
            }
        }, 1000); // Délai de 1 seconde avant de mettre le listener (donne le temps au MediaPlayer de se charger)
    }

    private void startLoadingSequence() {
        progressBar.setMax(100);
        simulateLoadingStep(0);
    }

    private void simulateLoadingStep(int stepIndex) {
        if (stepIndex < loadingSteps.length) {
            loadingText.setText(loadingSteps[stepIndex]);
            progress += 100 / loadingSteps.length;

            // Mise à jour de la barre de progression
            progressBar.setProgress(progress);

            // Attendre 1 seconde avant de passer à l'étape suivante
            handler.postDelayed(() -> simulateLoadingStep(stepIndex + 1), 1000);
        } else {
            // Si les étapes sont terminées et que le son n'est pas encore joué, on s'assure de passer à l'activité
        }
    }

    private void navigateToNextActivity() {
        // Lancer l'activité MenuDuJeu après le chargement
        Intent intent = new Intent(Chargement.this, MenuDuJeu.class); // Remplacer par ton activité de jeu
        startActivity(intent);
        finish(); // Fermer l'activité de chargement pour qu'elle ne reste pas dans la pile
    }
}
