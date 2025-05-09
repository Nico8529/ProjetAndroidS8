package com.example.quiz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quiz.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import com.example.quiz.QuizData;

public class PreviewQuizFragment extends Fragment {
    private static final String TAG = "PreviewQuizFragment";
    private QuizCreationViewModel viewModel;
    private RecyclerView questionsRecyclerView;
    private TextView quizPreviewTitle;
    private Button saveQuizButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(QuizCreationViewModel.class);

        // Initialiser les vues
        quizPreviewTitle = view.findViewById(R.id.quizPreviewTitle);
        questionsRecyclerView = view.findViewById(R.id.questionsRecyclerView);
        saveQuizButton = view.findViewById(R.id.saveQuizButton);

        // Configuration du RecyclerView avec un adapter personnalisé
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        QuestionsPreviewAdapter adapter = new QuestionsPreviewAdapter();
        questionsRecyclerView.setAdapter(adapter);

        // Observer les changements du titre
        viewModel.getQuizTitle().observe(getViewLifecycleOwner(), title -> {
            if (title != null && !title.isEmpty()) {
                quizPreviewTitle.setText("Aperçu du quiz : " + title);
            }
        });

        // Observer les changements des questions
        viewModel.getQuestions().observe(getViewLifecycleOwner(), questions -> {
            if (questions != null) {
                adapter.setQuestions(questions);
                adapter.notifyDataSetChanged();
            }
        });

        saveQuizButton.setOnClickListener(v -> saveQuiz());
    }

private void saveQuiz() {
    if (!viewModel.isQuizValid()) {
        Toast.makeText(getContext(), "Le quiz n'est pas complet", Toast.LENGTH_SHORT).show();
        return;
    }

    try {
        Context context = requireContext();
        // Lire le fichier JSON existant
        JSONObject existingData = loadExistingQuizData(context);
        if (existingData == null) {
            existingData = new JSONObject();
            existingData.put("quizzes", new JSONArray());
        }

        // Créer et ajouter le nouveau quiz
        JSONObject newQuiz = createQuizJson();
        
        // Ajouter des champs supplémentaires requis
        int newId = getNextQuizId(existingData.getJSONArray("quizzes"));
        newQuiz.put("id", newId);
        newQuiz.put("scoreMax", 1000); // Score par défaut
        newQuiz.put("premium", "non");  // Non premium par défaut
        newQuiz.put("keywords", viewModel.getQuizTitle().getValue().toLowerCase());
        newQuiz.put("quizDescription", "Quiz créé par l'utilisateur");
        newQuiz.put("nombreQuestion", viewModel.getQuestions().getValue().size());

        // Ajouter le nouveau quiz à la liste existante
        existingData.getJSONArray("quizzes").put(newQuiz);

        // Sauvegarder dans le fichier
        saveQuizToFile(context, existingData);
        
        // Rafraîchir les données
        QuizData.refreshQuizData(context);

        Toast.makeText(context, "Quiz sauvegardé avec succès", Toast.LENGTH_SHORT).show();
        requireActivity().finish();
    } catch (JSONException e) {
        Log.e(TAG, "Erreur lors de la sauvegarde du quiz", e);
        Toast.makeText(getContext(), "Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show();
    }
}

private JSONObject loadExistingQuizData(Context context) {
    try {
        // Essayer de lire depuis le stockage interne
        File file = new File(context.getFilesDir(), "quiz_data.json");
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            return new JSONObject(new String(buffer, "UTF-8"));
        }
        
        // Si non trouvé, lire depuis les assets
        InputStream is = context.getAssets().open("quiz_data.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new JSONObject(new String(buffer, "UTF-8"));
    } catch (Exception e) {
        Log.e(TAG, "Erreur lors de la lecture du fichier quiz_data.json", e);
        return null;
    }
}

private JSONObject createQuizJson() throws JSONException {
    JSONObject quizJson = new JSONObject();
    quizJson.put("title", viewModel.getQuizTitle().getValue());

    JSONArray questionsArray = new JSONArray();
    List<QuizCreationViewModel.Question> questions = viewModel.getQuestions().getValue();

    if (questions != null) {
        for (int i = 0; i < questions.size(); i++) {
            QuizCreationViewModel.Question question = questions.get(i);
            JSONObject questionJson = new JSONObject();
            questionJson.put("index", i + 1); // Ajouter l'index
            questionJson.put("question", question.getQuestionText());
            questionJson.put("correctAnswer", question.getCorrectAnswerIndex());
            questionJson.put("difficulty", 1); // Difficulté par défaut

            JSONArray optionsArray = new JSONArray();
            for (String option : question.getOptions()) {
                optionsArray.put(option);
            }
            questionJson.put("options", optionsArray);

            questionsArray.put(questionJson);
        }
    }

    quizJson.put("questions", questionsArray);
    return quizJson;
}

    private int getNextQuizId(JSONArray quizzes) throws JSONException {
        int maxId = 0;
        for (int i = 0; i < quizzes.length(); i++) {
            int currentId = quizzes.getJSONObject(i).getInt("id");
            if (currentId > maxId) {
                maxId = currentId;
            }
        }
        return maxId + 1;
    }

private void saveQuizToFile(Context context, JSONObject jsonData) {
    try {
        // Sauvegarder dans le stockage interne de l'application
        FileOutputStream fos = context.openFileOutput("quiz_data.json", Context.MODE_PRIVATE);
        OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
        writer.write(jsonData.toString(4));
        writer.flush();
        writer.close();
        fos.close();

        Log.d(TAG, "Quiz sauvegardé avec succès dans le stockage interne");
    } catch (Exception e) {
        Log.e(TAG, "Erreur lors de la sauvegarde : " + e.getMessage());
        e.printStackTrace();
    }
}

private void copyFile(File source, File dest) throws IOException {
    try (InputStream in = new FileInputStream(source);
         OutputStream out = new FileOutputStream(dest)) {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
    }
}
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
        
        // Log du contenu pour déboguer
        Log.d(TAG, "Contenu JSON chargé : " + jsonString);
        
        JSONArray quizzes = jsonObject.getJSONArray("quizzes");
        for (int i = 0; i < quizzes.length(); i++) {
            JSONObject quiz = quizzes.getJSONObject(i);
            if (quiz.getInt("id") == quizId) {
                return quiz;
            }
        }
    } catch (Exception e) {
        Log.e(TAG, "Erreur lors du chargement du fichier JSON: ", e);
    }

    return null;
}
private class QuestionsPreviewAdapter extends RecyclerView.Adapter<QuestionsPreviewAdapter.QuestionViewHolder> {
    private List<QuizCreationViewModel.Question> questions = new ArrayList<>();

    public void setQuestions(List<QuizCreationViewModel.Question> questions) {
        this.questions = questions != null ? questions : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_preview, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuizCreationViewModel.Question question = questions.get(position);
        holder.bind(question, position);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        private final TextView questionNumberText;
        private final TextView questionText;
        private final TextView optionsText;

        QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberText = itemView.findViewById(R.id.questionNumberText);
            questionText = itemView.findViewById(R.id.questionText);
            optionsText = itemView.findViewById(R.id.optionsText);
        }

        void bind(QuizCreationViewModel.Question question, int position) {
            questionNumberText.setText(String.format("Question %d", position + 1));
            questionText.setText(question.getQuestionText());
            
            // Formatage des options
            StringBuilder optionsBuilder = new StringBuilder();
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                optionsBuilder.append(String.format("%c) %s\n", 
                    'A' + i, 
                    options.get(i)
                ));
                if (i == question.getCorrectAnswerIndex()) {
                    optionsBuilder.append("(Réponse correcte)\n");
                }
            }
            optionsText.setText(optionsBuilder.toString());
        }
    }
}
    // ... le reste du code reste identique ...
}