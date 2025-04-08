package com.example.quiz;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
public class MenuStatistique extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_statistique);

        findViewById(R.id.btnBack_LStatistique).setOnClickListener(v -> finish());

    }
}
