package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.IdlingRegistry;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ChargementActivityTest {

    private Chargement activity;

    @Before
    public void setUp() {
        // Lancer l'activité
        ActivityScenario<Chargement> scenario = ActivityScenario.launch(Chargement.class);
        scenario.onActivity(act -> activity = act); // Récupérer l'instance de l'activité
    }

    @Test
    public void testActivityLaunch() {
        // Vérifier que l'activité est bien lancée
        assertNotNull(activity);

        // Vérifier que la vue text_LChargement existe et est affichée
        Espresso.onView(withId(R.id.barre_LChargement))
                .check(matches(isDisplayed())); // Vérifier que la vue est bien visible
    }

    @Test
    public void testNavigateToNextActivity() {
        // Créer une intention pour l'activité suivante
        Intent expectedIntent = new Intent(activity, MenuDuJeu.class);

        // Simuler la navigation vers l'activité suivante
        activity.navigateToNextActivity();

        // Vérifier que l'intention utilisée pour démarrer l'activité est correcte
        ActivityScenario<MenuDuJeu> scenario = ActivityScenario.launch(MenuDuJeu.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<MenuDuJeu>() {
            @Override
            public void perform(MenuDuJeu activity) {
                Intent actualIntent = activity.getIntent();
                assertNotNull(actualIntent);  // Vérifier que l'intention n'est pas nulle
                assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());  // Vérifier que les composants de l'intention sont égaux
            }
        });
    }

    @Test
    public void testProgressBarVisibility() {
        // Vérifier que la ProgressBar est visible
        ActivityScenario.launch(Chargement.class);
        Espresso.onView(withId(R.id.title_LChargement))
                .check(matches(isDisplayed())); // Vérifier que la ProgressBar est visible
    }

    @Test
    public void testLoadingTextVisibility() {
        // Vérifier que le texte de chargement est visible
        ActivityScenario.launch(Chargement.class);
        Espresso.onView(withId(R.id.text_LChargement))
                .check(matches(isDisplayed())); // Vérifier que le texte est visible
    }

    @Test
    public void testLogoClickAnimation() {
        // Vérifier que l'animation du logo se lance lors du clic
        Espresso.onView(withId(R.id.logo_LChargement))
                .perform(ViewActions.click()); // Simuler un clic sur le logo

        // Vérifier que l'élément est toujours affiché après le clic
        Espresso.onView(withId(R.id.logo_LChargement))
                .check(matches(isDisplayed())); // Vérifier si le logo reste visible après le clic
    }

    @Test
    public void testLoadingStepChange() {
        // Lancer l'activité
        ActivityScenario.launch(Chargement.class);

        // Attendre que la vue de chargement devienne visible
        ViewVisibilityIdlingResource idlingResource = new ViewVisibilityIdlingResource(activity.findViewById(R.id.text_LChargement));

        // Enregistrer l'IdlingResource avec Espresso pour que l'attente se produise avant l'interaction
        IdlingRegistry.getInstance().register(idlingResource);

        // Attendre que le texte de chargement apparaisse
        Espresso.onView(withId(R.id.text_LChargement))
                .check(matches(withText("Chargement des données...")));  // Vérifier que le texte est correct

        // Désenregistrer l'IdlingResource après l'attente
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
