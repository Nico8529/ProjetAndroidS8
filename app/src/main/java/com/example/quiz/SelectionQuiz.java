package com.example.quiz;

import android.os.Bundle;
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
        findViewById(R.id.btnBack_Lselection_quiz).setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView_SelectionQuiz);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        quizList = new ArrayList<>();
        loadQuizData();

        quizAdapter = new QuizAdapter(quizList);
        recyclerView.setAdapter(quizAdapter);
    }

    private void loadQuizData() {
        String jsonData = "[{\"id\":1,\"title\":\"Quiz sur la nature\",\"score\":85,\"isPremium\":true},{\"id\":2,\"title\":\"Quiz de culture générale\",\"score\":70,\"isPremium\":false},{\"id\":3,\"title\":\"Quiz de mathématiques\",\"score\":90,\"isPremium\":true}]";

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Quiz quiz = new Quiz(
                        jsonObject.getInt("id"),
                        jsonObject.getString("title"),
                        jsonObject.getInt("score"),
                        jsonObject.getBoolean("isPremium")
                );
                quizList.add(quiz);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
