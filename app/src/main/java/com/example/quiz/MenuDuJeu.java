package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuDuJeu extends AppCompatActivity {

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

        // Configuration des écouteurs pour chaque bouton
        btnContinue.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, SelectionMode.class)));

        btnNewGame.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, SelectionMode.class)));

        btnStat.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, MenuStatistique.class)));

        btnSave.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, MenuSauvegarde.class)));

        btnParam.setOnClickListener(v -> startActivity(new Intent(MenuDuJeu.this, MenuParametre.class)));

        btnExit.setOnClickListener(v -> finishAffinity()); // Ferme l'application
    }
}
