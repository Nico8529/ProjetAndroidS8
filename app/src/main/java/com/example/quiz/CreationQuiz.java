package com.example.quiz;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.quiz.fragments.QuizCreationPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import com.example.quiz.fragments.QuizCreationViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class CreationQuiz extends AppCompatActivity {

    private static final String TAG = "CreationQuiz";
    private ViewPager2 viewPager;
    private QuizCreationViewModel viewModel;
    private TextInputEditText quizNameInput;
    private String quizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_quiz);
        Log.d(TAG, "onCreate: CreationQuiz initialisé");

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this).get(QuizCreationViewModel.class);




        // Setup ViewPager2
        viewPager = findViewById(R.id.viewPager);
        QuizCreationPagerAdapter pagerAdapter = new QuizCreationPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false); // Désactive le swipe

        // Bouton retour
        findViewById(R.id.btnBack_LCreation_Quiz).setOnClickListener(v -> {
            Log.d(TAG, "Retour au menu précédent");
            finish();
        });


    }

    // Navigation methods
    public void navigateToNextPage() {
        if (viewPager != null) {
            int nextItem = viewPager.getCurrentItem() + 1;
            if (nextItem < viewPager.getAdapter().getItemCount()) {
                viewPager.setCurrentItem(nextItem);
                Log.d(TAG, "Navigation vers la page " + nextItem);
            }
        }
    }

    public void navigateToPreviousPage() {
        if (viewPager != null) {
            int previousItem = viewPager.getCurrentItem() - 1;
            if (previousItem >= 0) {
                viewPager.setCurrentItem(previousItem);
                Log.d(TAG, "Navigation vers la page " + previousItem);
            }
        }
    }

    // Method to get the quiz name
    public String getQuizName() {
        return quizName != null ? quizName : "";
    }
}