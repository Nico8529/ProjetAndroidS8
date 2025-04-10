package com.example.quiz;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class QuizData {

    public static JSONObject loadQuiz(Context context, int quizId) {
        try {
            InputStream inputStream = context.getAssets().open("quiz_data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray quizzes = jsonObject.getJSONArray("quizzes");

            for (int i = 0; i < quizzes.length(); i++) {
                JSONObject quiz = quizzes.getJSONObject(i);
                if (quiz.getInt("id") == quizId) {
                    return quiz;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("QuizData", "Erreur lors du chargement du fichier JSON: " + e.getMessage());
        }
        return null;
    }



}