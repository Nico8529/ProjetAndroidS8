package com.example.quiz.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class QuizCreationPagerAdapter extends FragmentStateAdapter {

    public QuizCreationPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Retourne le fragment approprié selon la position
        switch (position) {
            case 0:
                return new QuizInfoFragment();
            case 1:
                return new AddQuestionFragment();
            case 2:
                return new PreviewQuizFragment();
            default:
                throw new IllegalArgumentException("Position invalide : " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Nombre total de pages
    }
}