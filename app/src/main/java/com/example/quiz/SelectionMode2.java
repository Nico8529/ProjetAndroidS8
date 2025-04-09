package com.example.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SelectionMode2 extends AppCompatActivity {
    SharedPreferences prefs = getSharedPreferences("QUIZ_DATA", MODE_PRIVATE);
    int quizId = prefs.getInt("selectedQuizId", -1);
    String title = prefs.getString("selectedQuizTitle", "Inconnu");
}