package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Chargement extends AppCompatActivity {

    private TextView loadingText;
    private ProgressBar progressBar;
    private String[] loadingMessages;
    private int currentMessageIndex = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chargement);

        loadingText = findViewById(R.id.text_LChargement);
        progressBar = findViewById(R.id.title_LChargement);

        loadingMessages = getResources().getStringArray(R.array.loading_messages);

        startLoadingSequence();
    }

    private void startLoadingSequence() {
        progressBar.setMax(loadingMessages.length);
        updateLoadingMessage();
    }

    private void updateLoadingMessage() {
        if (currentMessageIndex < loadingMessages.length) {
            loadingText.setText(loadingMessages[currentMessageIndex]);
            progressBar.setProgress(currentMessageIndex + 1);
            currentMessageIndex++;
            handler.postDelayed(this::updateLoadingMessage, 1000); // Mettre à jour chaque seconde
        } else {
            // Chargement terminé, passer à l'activité suivante
            navigateToNextActivity();
        }
    }

    private void navigateToNextActivity() {
        // Utiliser un Intent pour démarrer l'activité MenuDuJeu
        Intent intent = new Intent(Chargement.this, MenuDuJeu.class);
        startActivity(intent);
        finish(); // Fermer l'activité de chargement pour qu'elle ne reste pas dans la pile
    }
}
