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

import java.io.IOException;
import java.io.InputStream;
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
        android.util.Log.d("QUIZ_DEBUG", "Adapter assigné au RecyclerView !");

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
        String jsonData = loadJSONFromAsset();

        try {
            JSONObject root = new JSONObject(jsonData);
            JSONArray quizzesArray = root.getJSONArray("quizzes");

            for (int i = 0; i < quizzesArray.length(); i++) {
                JSONObject quizObject = quizzesArray.getJSONObject(i);
                if (jsonData == null) {
                    android.util.Log.e("QUIZ_DEBUG", "Fichier JSON non chargé !");
                    return;
                } else {
                    android.util.Log.d("QUIZ_DEBUG", "JSON chargé !");
                }

                int id = quizObject.getInt("id");
                String title = quizObject.getString("title");
                int scoreMax = quizObject.getInt("scoreMax");
                String premium = quizObject.getString("premium");
                String keywords = quizObject.getString("keywords");
                String quizDescription = quizObject.getString("quizDescription");
                int nombreQuestion = quizObject.getInt("nombreQuestion");
                // Crée ton objet Quiz (adapté à ta classe)
                Quiz quiz = new Quiz(id, title, scoreMax, premium, keywords,quizDescription,nombreQuestion);

                quizList.add(quiz);
            }
            android.util.Log.d("QUIZ_DEBUG", "Nombre de quizzes chargés : " + quizList.size());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("quiz_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void saveSelectedQuiz(Quiz quiz) {
        SharedPreferences prefs = getSharedPreferences("QUIZ_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("selectedQuizId", quiz.getId());
        editor.putString("selectedQuizTitle", quiz.getTitle());
        editor.putInt("selectedQuizScore", quiz.getScore());
        editor.putString("premium", quiz.premium());
        editor.putString("quizDescription", quiz.quizDescription());
        editor.putInt("nombreQuestion", quiz.nombreQuestion());
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
