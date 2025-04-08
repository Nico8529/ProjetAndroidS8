package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuSauvegarde extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_sauvegarde);

        findViewById(R.id.btnRetour_LSauvegarde).setOnClickListener(v -> finish());

    }


}