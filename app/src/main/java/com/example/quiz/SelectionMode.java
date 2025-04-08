package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionMode extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_mode);
        findViewById(R.id.btnBack_Lselection_mode).setOnClickListener(v -> finish());

        // Initialisation des boutons
        Button btnSolo = findViewById(R.id.btnSolo_LSelectMode);
        Button btnMultiLocalJ1 = findViewById(R.id.btnMultiLocalJ1_LSelectMode);
        Button btnMultiLocalJ2 = findViewById(R.id.btnMultiLocalJ2_LSelectMode);
        Button btnMultiLocalJ3 = findViewById(R.id.btnMultiLocalJ3_LSelectMode);
        Button btnMultiLocalJ4 = findViewById(R.id.btnMultiLocalJ4_LSelectMode);
        Button btnMultiOnlineJ1 = findViewById(R.id.btnMultiOnlineJ1_LSelectMode);
        Button btnMultiOnlineJ2 = findViewById(R.id.btnMultiOnlineJ2_LSelectMode);
        Button btnMultiOnlineJ3 = findViewById(R.id.btnMultiOnlineJ3_LSelectMode);
        Button btnMultiOnlineJ4 = findViewById(R.id.btnMultiOnlineJ4_LSelectMode);

        // Configuration des écouteurs pour les boutons
        btnSolo.setOnClickListener(v -> startActivity(new Intent(SelectionMode.this, SelectionQuiz.class)));
        btnMultiLocalJ1.setOnClickListener(v -> startActivity(new Intent(SelectionMode.this, SelectionQuiz.class)));
        btnMultiLocalJ2.setOnClickListener(v -> startActivity(new Intent(SelectionMode.this, SelectionQuiz.class)));
        btnMultiLocalJ3.setOnClickListener(v -> startActivity(new Intent(SelectionMode.this, SelectionQuiz.class)));
        btnMultiLocalJ4.setOnClickListener(v -> startActivity(new Intent(SelectionMode.this, SelectionQuiz.class)));
        btnMultiOnlineJ1.setOnClickListener(v -> startActivity(new Intent(SelectionMode.this, SelectionQuiz.class)));
        btnMultiOnlineJ2.setOnClickListener(v -> startActivity(new Intent(SelectionMode.this, SelectionQuiz.class)));
        btnMultiOnlineJ3.setOnClickListener(v -> startActivity(new Intent(SelectionMode.this, SelectionQuiz.class)));
        btnMultiOnlineJ4.setOnClickListener(v -> startActivity(new Intent(SelectionMode.this, SelectionQuiz.class)));

    }

    private void navigateToSelectionQuiz() {
        // Utiliser un Intent pour démarrer l'activité SelectionQuiz
        Intent intent = new Intent(SelectionMode.this, SelectionQuiz.class);
        startActivity(intent);
    }
}