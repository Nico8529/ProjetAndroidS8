package com.example.quiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.quiz.CreationQuiz;
import com.example.quiz.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddQuestionFragment extends Fragment {
    private QuizCreationViewModel viewModel;
    private TextInputEditText questionInput;
    private TextInputEditText[] optionInputs = new TextInputEditText[4];
    private RadioGroup correctAnswerGroup;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(QuizCreationViewModel.class);

        // Initialiser les vues
        questionInput = view.findViewById(R.id.questionInput);
        optionInputs[0] = view.findViewById(R.id.option1Input);
        optionInputs[1] = view.findViewById(R.id.option2Input);
        optionInputs[2] = view.findViewById(R.id.option3Input);
        optionInputs[3] = view.findViewById(R.id.option4Input);
        correctAnswerGroup = view.findViewById(R.id.correctAnswerGroup);

        Button nextButton = view.findViewById(R.id.nextButton);
        Button previousButton = view.findViewById(R.id.previousButton);

        nextButton.setOnClickListener(v -> {
            if (validateQuestion()) {
                saveQuestion();
                ((CreationQuiz) requireActivity()).navigateToNextPage();
            }
        });

        previousButton.setOnClickListener(v ->
                ((CreationQuiz) requireActivity()).navigateToPreviousPage()
        );
    }

    private boolean validateQuestion() {
        // Vérifier si la question est remplie
        if (questionInput.getText() == null || questionInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Veuillez entrer une question", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Vérifier si toutes les options sont remplies
        for (int i = 0; i < optionInputs.length; i++) {
            if (optionInputs[i].getText() == null || optionInputs[i].getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Veuillez remplir l'option " + (i + 1), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Vérifier si une réponse correcte est sélectionnée
        if (correctAnswerGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Veuillez sélectionner la réponse correcte", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveQuestion() {
        String questionText = questionInput.getText().toString().trim();
        List<String> options = new ArrayList<>();
        
        // Récupérer toutes les options
        for (TextInputEditText optionInput : optionInputs) {
            options.add(optionInput.getText().toString().trim());
        }

        // Déterminer l'index de la réponse correcte
        int correctAnswerIndex = 0;
        int selectedId = correctAnswerGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.option1Radio) correctAnswerIndex = 0;
        else if (selectedId == R.id.option2Radio) correctAnswerIndex = 1;
        else if (selectedId == R.id.option3Radio) correctAnswerIndex = 2;
        else if (selectedId == R.id.option4Radio) correctAnswerIndex = 3;

        // Créer et sauvegarder la question
        QuizCreationViewModel.Question question = 
            new QuizCreationViewModel.Question(questionText, options, correctAnswerIndex);
        viewModel.addQuestion(question);

        // Réinitialiser les champs
        questionInput.setText("");
        for (TextInputEditText optionInput : optionInputs) {
            optionInput.setText("");
        }
        correctAnswerGroup.clearCheck();

        Toast.makeText(getContext(), "Question ajoutée avec succès", Toast.LENGTH_SHORT).show();
    }
}