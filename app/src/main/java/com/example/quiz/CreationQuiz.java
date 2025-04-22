package com.example.quiz;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

//pour toi Nico ^^
public class CreationQuiz extends AppCompatActivity {

    private static final String TAG = "CreationQuiz"; // Tag pour Logcat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_quiz);
        Log.d(TAG, "onCreate: CreationQuiz initialisé");

        // Bouton retour
        findViewById(R.id.btnBack_LCreation_Quiz).setOnClickListener(v -> {
            Log.d(TAG, "Retour au menu précédent");
            finish();
        });
    }
}