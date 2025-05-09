package com.example.quiz;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;

public class QuizData {
    private static final String TAG = "QuizData";

    public static void refreshQuizData(Context context) {
        try {
            // Vérifier le contenu du fichier dans assets
            File assetsDir = new File(context.getFilesDir(), "assets");
            File file = new File(assetsDir, "quiz_data.json");
            
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                fis.close();
                String jsonString = new String(buffer, "UTF-8");
                
                Log.d(TAG, "Contenu du fichier JSON : " + jsonString);
            } else {
                Log.e(TAG, "Le fichier n'existe pas dans : " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors du rafraîchissement des quiz data", e);
            e.printStackTrace();
        }
    }

    // Garder la méthode loadQuiz existante
    public static JSONObject loadQuiz(Context context, int quizId) {
        Log.d(TAG, "loadQuiz: Tentative de chargement du quiz avec l'ID " + quizId);

        try {
            // D'abord essayer de lire depuis le dossier assets local
            File assetsDir = new File(context.getFilesDir(), "assets");
            File file = new File(assetsDir, "quiz_data.json");
            InputStream inputStream;

            if (file.exists()) {
                inputStream = new FileInputStream(file);
            } else {
                inputStream = context.getAssets().open("quiz_data.json");
            }

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String jsonString = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray quizzes = jsonObject.getJSONArray("quizzes");

            for (int i = 0; i < quizzes.length(); i++) {
                JSONObject quiz = quizzes.getJSONObject(i);
                if (quiz.getInt("id") == quizId) {
                    Log.d(TAG, "loadQuiz: Quiz trouvé avec l'ID " + quizId);
                    return quiz;
                }
            }

            Log.d(TAG, "loadQuiz: Aucun quiz trouvé avec l'ID " + quizId);

        } catch (Exception e) {
            Log.e(TAG, "Erreur lors du chargement du fichier JSON: ", e);
        }

        return null;
    }
}