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
import java.io.File;
import java.io.FileInputStream;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.widget.Toast;
import java.io.FileWriter;

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

        // Ajouter après la configuration de l'adapter :
        setupSwipeToDelete();

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

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Quiz quizToDelete = quizList.get(position);

                // Ne pas permettre la suppression des 10 premiers quiz (prédéfinis)
                if (quizToDelete.getId() <= 10) {
                    Toast.makeText(SelectionQuiz.this, "Impossible de supprimer un quiz prédéfini", Toast.LENGTH_SHORT).show();
                    quizAdapter.notifyItemChanged(position);
                    return;
                }

                // Demander confirmation
                new AlertDialog.Builder(SelectionQuiz.this)
                        .setTitle("Supprimer le quiz")
                        .setMessage("Voulez-vous vraiment supprimer le quiz \"" + quizToDelete.getTitle() + "\" ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            deleteQuiz(position, quizToDelete);
                        })
                        .setNegativeButton("Non", (dialog, which) -> {
                            // Annuler la suppression
                            quizAdapter.notifyItemChanged(position);
                        })
                        .show();
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView);
    }

    private void deleteQuiz(int position, Quiz quiz) {
        try {
            // Lire le fichier JSON actuel
            String jsonStr = loadJSONFromAsset();
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray quizzes = jsonObject.getJSONArray("quizzes");

            // Créer un nouveau JSONArray sans le quiz à supprimer
            JSONArray newQuizzes = new JSONArray();
            for (int i = 0; i < quizzes.length(); i++) {
                JSONObject currentQuiz = quizzes.getJSONObject(i);
                if (currentQuiz.getInt("id") != quiz.getId()) {
                    newQuizzes.put(currentQuiz);
                }
            }

            // Mettre à jour le fichier JSON
            jsonObject.put("quizzes", newQuizzes);

            // Sauvegarder dans le stockage interne
            File file = new File(getFilesDir(), "quiz_data.json");
            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toString(4));
            writer.flush();
            writer.close();

            // Mettre à jour la liste et l'interface
            quizList.remove(position);
            quizAdapter.notifyItemRemoved(position);

            Toast.makeText(this, "Quiz supprimé avec succès", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la suppression du quiz : " + e.getMessage());
            Toast.makeText(this, "Erreur lors de la suppression du quiz", Toast.LENGTH_SHORT).show();
            quizAdapter.notifyItemChanged(position);
        }
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

    private String loadJSONFromAsset() {
        String json = null;
        try {
            // D'abord essayer de lire depuis le stockage interne
            File file = new File(getFilesDir(), "quiz_data.json");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                fis.close();
                json = new String(buffer, StandardCharsets.UTF_8);
                Log.d(TAG, "Fichier JSON lu depuis le stockage interne");
            } else {
                // Si non trouvé, lire depuis les assets
                InputStream is = getAssets().open("quiz_data.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, StandardCharsets.UTF_8);
                Log.d(TAG, "Fichier JSON lu depuis les assets");
            }
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