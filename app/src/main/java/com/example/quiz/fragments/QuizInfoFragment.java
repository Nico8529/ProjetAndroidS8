package com.example.quiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quiz.CreationQuiz;
import com.example.quiz.R;
import com.google.android.material.textfield.TextInputEditText;

public class QuizInfoFragment extends Fragment {
    
    private QuizCreationViewModel viewModel;
    private TextInputEditText quizTitleInput;
    private Button nextButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                           ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(QuizCreationViewModel.class);
        
        // Trouver les vues
        quizTitleInput = view.findViewById(R.id.quizTitleInput);
        nextButton = view.findViewById(R.id.nextButton);
        
        // Configurer le bouton suivant
        nextButton.setOnClickListener(v -> {
            String title = quizTitleInput.getText().toString().trim();
            if (!title.isEmpty()) {
                viewModel.setQuizTitle(title);
                if (getActivity() instanceof CreationQuiz) {
                    ((CreationQuiz) getActivity()).navigateToNextPage();
                }
            } else {
                Toast.makeText(getContext(), 
                    "Veuillez entrer un titre pour le quiz", 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
}