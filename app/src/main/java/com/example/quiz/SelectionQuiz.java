package com.example.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SelectionQuiz extends AppCompatActivity {

    private static final String TAG = "SelectionQuiz"; // Définir un TAG pour Logcat
    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private List<Quiz> quizList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_quiz);

        // 🔙 Gestion du bouton retour
        findViewById(R.id.btnBack_LSelection_Quiz).setOnClickListener(v -> finish());

        // Configuration du RecyclerView
        recyclerView = findViewById(R.id.recyclerView_SelectionQuiz);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        quizList = new ArrayList<>();
        loadQuizData(); // Chargement des données de quiz

        // Configuration de l'adapter
        quizAdapter = new QuizAdapter(quizList, quiz -> {
            saveSelectedQuiz(quiz);
            Intent intent = new Intent(SelectionQuiz.this, SelectionMode2.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(quizAdapter);
        Log.d(TAG, "Adapter assigné au RecyclerView !");

        // 🔍 Barre de recherche
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

    // Méthode pour charger les données JSON
    private void loadQuizData() {
        String jsonData = loadJSONFromAsset();

        if (jsonData == null) {
            Log.e(TAG, "Erreur lors du chargement du fichier JSON.");
            return;
        }

        try {
            JSONObject root = new JSONObject(jsonData);
            JSONArray quizzesArray = root.getJSONArray("quizzes");

            for (int i = 0; i < quizzesArray.length(); i++) {
                JSONObject quizObject = quizzesArray.getJSONObject(i);

                int id = quizObject.getInt("id");
                String title = quizObject.getString("title");
                int scoreMax = quizObject.getInt("scoreMax");
                String premium = quizObject.getString("premium");
                String keywords = quizObject.getString("keywords");
                String quizDescription = quizObject.getString("quizDescription");
                int nombreQuestion = quizObject.getInt("nombreQuestion");

                // Créer l'objet Quiz
                Quiz quiz = new Quiz(id, title, scoreMax, premium, keywords, quizDescription, nombreQuestion);
                quizList.add(quiz);
            }
            Log.d(TAG, "Nombre de quizzes chargés : " + quizList.size());

        } catch (JSONException e) {
            Log.e(TAG, "Erreur JSON : " + e.getMessage(), e); // Remplacer printStackTrace() par Log.e
        }
    }

    // Méthode pour charger le fichier JSON depuis les assets
    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("quiz_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];

            // Lire les octets depuis l'InputStream
            int bytesRead = is.read(buffer);

            if (bytesRead != size) {
                Log.w(TAG, "Nombre d'octets lus ne correspond pas à la taille du fichier.");
            }

            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e(TAG, "Erreur de lecture du fichier JSON : " + ex.getMessage(), ex);
        }
        return json;
    }
    // Méthode pour enregistrer le quiz sélectionné dans SharedPreferences
    private void saveSelectedQuiz(Quiz quiz) {
        SharedPreferences prefs = getSharedPreferences("QUIZ_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("selectedQuizId", quiz.getId());
        editor.putString("selectedQuizTitle", quiz.getTitle());
        editor.putInt("selectedQuizScore", quiz.getScore());
        editor.putString("premium", quiz.premium());
        editor.putString("quizDescription", quiz.quizDescription());
        editor.putInt("nombreQuestion", quiz.getNombreQuestion());
        editor.apply();

        Log.d(TAG, "Quiz sélectionné enregistré : " + quiz.getTitle()); // Log pour la sauvegarde
    }

    // Méthode pour filtrer les quiz selon la recherche
    private void filterQuiz(String text) {
        List<Quiz> filteredList = new ArrayList<>();
        for (Quiz quiz : quizList) {
            if (quiz.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    quiz.getKeywords().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(quiz);
            }
        }

        // Mise à jour de l'adapter avec les résultats filtrés
        quizAdapter = new QuizAdapter(filteredList, quiz -> {
            saveSelectedQuiz(quiz);
            startActivity(new Intent(SelectionQuiz.this, SelectionMode2.class));
        });
        recyclerView.setAdapter(quizAdapter);

        Log.d(TAG, "Filtrage effectué, " + filteredList.size() + " quizzes trouvés.");
    }
}

