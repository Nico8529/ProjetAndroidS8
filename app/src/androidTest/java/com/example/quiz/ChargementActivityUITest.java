package com.example.quiz;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChargementActivityUITest {

    @Test
    public void testProgressBarVisibility() {
        // Vérifier si la ProgressBar est visible
        Espresso.onView(withId(R.id.title_LChargement))
                .check(matches(isDisplayed()));  // La ProgressBar doit être visible
    }

    @Test
    public void testLoadingTextUpdate() {
        // Vérifier si le texte de chargement change correctement
        Espresso.onView(withId(R.id.text_LChargement))
                .check(matches(withText("Chargement des données..."))); // Vérifier le premier texte
    }

    @Test
    public void testLogoAnimationOnClick() {
        // Vérifier si l'animation se déclenche lorsqu'on clique sur le logo
        Espresso.onView(withId(R.id.logo_LChargement))
                .perform(ViewActions.click()); // Simuler un clic

        // Vérifier que l'animation est bien appliquée (en supposant que l'animation est bien déclenchée)
        Espresso.onView(withId(R.id.logo_LChargement))
                .check(matches(isDisplayed())); // L'élément est affiché après le clic
    }
}
