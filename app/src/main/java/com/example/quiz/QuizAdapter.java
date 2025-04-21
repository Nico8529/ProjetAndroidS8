package com.example.quiz;

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

    private List<Quiz> quizList;
    private OnQuizClickListener listener;

    public QuizAdapter(List<Quiz> quizList, OnQuizClickListener listener) {
        this.quizList = quizList;
        this.listener = listener;
    }
    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.quizTitle.setText(quiz.getTitle());
        holder.quizDescription.setText(quiz.quizDescription());
        holder.nombreQuestion.setText("Questions : " + quiz.nombreQuestion());
        holder.quizScore.setText("Score Max : " + quiz.getScore());
        if (Objects.equals(quiz.premium(), "yes")) {
            holder.premiumIcon.setVisibility(View.VISIBLE);
        } else {
            holder.premiumIcon.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            listener.onQuizClick(quiz);
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitle;
        TextView quizScore;
        TextView nombreQuestion;
        TextView quizDescription;
        ImageView premiumIcon;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.quizTitle);
            quizScore = itemView.findViewById(R.id.quizscore);
            nombreQuestion = itemView.findViewById(R.id.nombreQuestion);
            quizDescription = itemView.findViewById(R.id.quizDescription);
            premiumIcon = itemView.findViewById(R.id.premiumicon);
        }
    }
}
