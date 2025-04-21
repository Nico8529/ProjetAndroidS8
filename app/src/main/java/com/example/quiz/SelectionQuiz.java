package com.example.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SelectionQuiz extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private List<Quiz> quizList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_quiz);
        findViewById(R.id.btnBack_LSelection_Quiz).setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView_SelectionQuiz);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        quizList = new ArrayList<>();
        loadQuizData();

        quizAdapter = new QuizAdapter(quizList, quiz -> {
            saveSelectedQuiz(quiz);
            Intent intent = new Intent(SelectionQuiz.this, SelectionMode2.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(quizAdapter);

        EditText searchBar = findViewById(R.id.searchBarre_LSelection_Quiz);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterQuiz(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadQuizData() {
        String jsonData = "[{\"id\":1,\"title\":\"Quiz Nature\",\"score\":85,\"isPremium\":true,\"keywords\":\"nature environnement arbre\"}," +
                "{\"id\":2,\"title\":\"Culture Générale\",\"score\":70,\"isPremium\":false,\"keywords\":\"histoire géographie culture\"}," +
                "{\"id\":3,\"title\":\"Mathématiques\",\"score\":90,\"isPremium\":true,\"keywords\":\"maths calcul logique\"}," +
                "{\"id\":4,\"title\":\"Sport\",\"score\":60,\"isPremium\":false,\"keywords\":\"sport football basket\"}," +
                "{\"id\":5,\"title\":\"Cinéma\",\"score\":75,\"isPremium\":false,\"keywords\":\"films cinéma acteurs\"}," +
                "{\"id\":6,\"title\":\"Informatique\",\"score\":95,\"isPremium\":true,\"keywords\":\"informatique code programmation\"}," +
                "{\"id\":7,\"title\":\"Littérature\",\"score\":88,\"isPremium\":false,\"keywords\":\"livres écrivains poésie\"}," +
                "{\"id\":8,\"title\":\"Sciences\",\"score\":92,\"isPremium\":true,\"keywords\":\"sciences physique chimie\"}," +
                "{\"id\":9,\"title\":\"Musique\",\"score\":80,\"isPremium\":false,\"keywords\":\"musique instruments notes\"}," +
                "{\"id\":10,\"title\":\"Gastronomie\",\"score\":78,\"isPremium\":false,\"keywords\":\"cuisine plats chefs\"}]";

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Quiz quiz = new Quiz(
                        jsonObject.getInt("id"),
                        jsonObject.getString("title"),
                        jsonObject.getInt("score"),
                        jsonObject.getBoolean("isPremium"),
                        jsonObject.getString("keywords")
                );

                quizList.add(quiz);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveSelectedQuiz(Quiz quiz) {
        SharedPreferences prefs = getSharedPreferences("QUIZ_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("selectedQuizId", quiz.getId());
        editor.putString("selectedQuizTitle", quiz.getTitle());
        editor.putInt("selectedQuizScore", quiz.getScore());
        editor.putBoolean("isPremium", quiz.isPremium());
        editor.apply();
    }

    private void filterQuiz(String text) {
        List<Quiz> filteredList = new ArrayList<>();
        for (Quiz quiz : quizList) {
            if (quiz.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    quiz.getKeywords().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(quiz);
            }
        }
        quizAdapter = new QuizAdapter(filteredList, quiz -> {
            saveSelectedQuiz(quiz);
            startActivity(new Intent(SelectionQuiz.this, SelectionMode2.class));
        });
        recyclerView.setAdapter(quizAdapter);
    }
}
