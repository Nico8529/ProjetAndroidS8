package com.example.quiz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    // Liste des Quiz que l'adaptateur va gérer
    private final List<Quiz> quizList;

    // Interface pour gérer les clics sur les items
    private final OnQuizClickListener listener;

    // Tag pour les logs
    private static final String TAG = "QuizAdapter";

    // Constructeur de l'adaptateur
    public QuizAdapter(List<Quiz> quizList, OnQuizClickListener listener) {
        this.quizList = quizList;
        this.listener = listener;

        // Log pour indiquer que l'adaptateur a été créé
        Log.d(TAG, "QuizAdapter: Constructeur appelé, liste de quiz de taille = " + quizList.size());
    }

    // Interface pour écouter les clics sur un quiz
    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Log pour indiquer que la création du ViewHolder commence
        Log.d(TAG, "onCreateViewHolder: Création du ViewHolder pour un item de quiz");

        // Créer la vue pour l'item de quiz
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz, parent, false);

        // Retourner un nouveau ViewHolder
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        // Récupérer le quiz actuel à la position donnée
        Quiz quiz = quizList.get(position);

        // Log pour afficher les données du quiz bind
        Log.d(TAG, "onBindViewHolder: Position = " + position + ", Titre du quiz = " + quiz.getTitle());

        // Remplir les vues avec les informations du quiz
        holder.quizTitle.setText(quiz.getTitle());
        holder.quizDescription.setText(quiz.quizDescription());
        // Utilisation de getString() pour récupérer les chaînes avec des espaces réservés
        holder.nombreQuestion.setText(holder.itemView.getContext().getString(R.string.questions_text, quiz.getNombreQuestion()));
        holder.quizScore.setText(holder.itemView.getContext().getString(R.string.score_max_text, quiz.getScore()));

        // Vérifier si le quiz est premium
        if (Objects.equals(quiz.premium(), "yes")) {
            holder.premiumIcon.setVisibility(View.VISIBLE);
            // Log pour indiquer que l'icône premium est visible
            Log.d(TAG, "onBindViewHolder: Icône premium visible pour le quiz " + quiz.getTitle());
        } else {
            holder.premiumIcon.setVisibility(View.GONE);
            // Log pour indiquer que l'icône premium est cachée
            Log.d(TAG, "onBindViewHolder: Icône premium cachée pour le quiz " + quiz.getTitle());
        }

        // Ajouter un listener pour le clic sur l'item
        holder.itemView.setOnClickListener(v -> {
            // Log pour indiquer qu'un quiz a été cliqué
            Log.d(TAG, "onClick: Quiz cliqué, ID = " + quiz.getId());
            listener.onQuizClick(quiz);
        });
    }

    @Override
    public int getItemCount() {
        // Log pour afficher le nombre total d'items dans l'adaptateur
        Log.d(TAG, "getItemCount: Nombre total d'items = " + quizList.size());
        return quizList.size();
    }

    // ViewHolder qui contient les vues pour chaque item
    public static class QuizViewHolder extends RecyclerView.ViewHolder {

        // Déclaration des vues
        private final TextView quizTitle;
        private final TextView quizScore;
        private final TextView nombreQuestion;
        private final TextView quizDescription;
        private final ImageView premiumIcon;

        // Constructeur pour initialiser les vues
        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.quizTitle);
            quizScore = itemView.findViewById(R.id.quizscore);
            nombreQuestion = itemView.findViewById(R.id.nombreQuestion);
            quizDescription = itemView.findViewById(R.id.quizDescription);
            premiumIcon = itemView.findViewById(R.id.premiumicon);

            // Log pour indiquer que le ViewHolder a été créé
            Log.d(TAG, "QuizViewHolder: ViewHolder créé et vues initialisées");
        }
    }
}
