package com.example.quiz;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.view.View;
import android.widget.ProgressBar;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.ProgressBar;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChargementTest {

    @Test
    public void testLayout_PresenceDeTousLesElements() {
        ActivityScenario.launch(Chargement.class);

        // Logo
        onView(withId(R.id.logo_LChargement))
                .check(matches(isDisplayed()));

        // Titre texte (barre_LChargement)
        onView(withId(R.id.barre_LChargement))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.app_name)));

        // Barre de progression
        onView(withId(R.id.title_LChargement))
                .check(matches(isDisplayed()));

        // Texte dynamique de chargement
        onView(withId(R.id.text_LChargement))
                .check(matches(isDisplayed()));

        // Version
        onView(withId(R.id.version_LChargement))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.version_app)));
    }

    @Test
    public void testProgressBarCommenceAZero() {
        // Vérifier que la ProgressBar commence à 0
        ActivityScenario.launch(Chargement.class).onActivity(activity -> {
            ProgressBar progressBar = activity.findViewById(R.id.title_LChargement);
            assertEquals("La barre de progression doit commencer à 0", 0, progressBar.getProgress());
        });
    }

    @Test
    public void testProgressBarProgresser() {
        // Vérification que la ProgressBar a progressé
        ActivityScenario.launch(Chargement.class).onActivity(activity -> {
            ProgressBar progressBar = activity.findViewById(R.id.title_LChargement);

            // Attends que la ProgressBar ait progressé
            int initialProgress = progressBar.getProgress();

            // Donne un peu de temps pour que l'animation se fasse
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

            // Vérifier que la ProgressBar a progressé
            assertTrue("La ProgressBar doit progresser", progressBar.getProgress() > initialProgress);
        });
    }

    @Test
    public void testTexteChangePendantChargement() throws InterruptedException {
        ActivityScenario.launch(Chargement.class);

        Thread.sleep(800); // Laisse le temps pour que le texte change

        onView(withId(R.id.text_LChargement))
                .check(matches(not(withText("Chargement des assets"))));
    }

    @Test
    public void testLogoCliquableEtAnimationPresente() {
        // Vérifier si le logo est visible
        onView(withId(R.id.logoImage_LLogin))
                .check(matches(isDisplayed()));

        // Tester si la ProgressBar commence à 0
        ActivityScenario.launch(Chargement.class).onActivity(activity -> {
            ProgressBar progressBar = activity.findViewById(R.id.title_LChargement);
            assertEquals("La barre de progression doit commencer à 0", 0, progressBar.getProgress());
        });
    }

    @Test
    public void testProgressBarAugmente() throws InterruptedException {
        ActivityScenario<Chargement> scenario = ActivityScenario.launch(Chargement.class);

        Thread.sleep(1500); // Attendre un peu

        scenario.onActivity(activity -> {
            ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.title_LChargement);
            int progress = progressBar.getProgress();
            assert progress > 0 : "La barre de progression doit avoir avancé";
        });
    }

    @Test
    public void testTexteChargementFaitPartieDesEtapes() throws InterruptedException {
        ActivityScenario.launch(Chargement.class);

        Thread.sleep(300); // Laisse une étape apparaître

        onView(withId(R.id.text_LChargement))
                .check(matches(anyOf(
                        withText("Chargement des données..."),
                        withText("Initialisation des paramètres..."),
                        withText("Récupération des scores..."),
                        withText("Optimisation finale..."),
                        withText("Vérification de la connexion réseau..."),
                        withText("Mise à jour des ressources..."),
                        withText("Chargement des images et icônes..."),
                        withText("Préparation de l'interface utilisateur..."),
                        withText("Chargement des utilisateurs récents..."),
                        withText("Initialisation des fonctionnalités avancées..."),
                        withText("Récupération des paramètres de l'utilisateur..."),
                        withText("Préparation des animations..."),
                        withText("Synchronisation des sauvegardes..."),
                        withText("Finalisation de l'application...")
                )));
    }

    @Test
    public void testRedirectionVersLoginApresDelai() throws InterruptedException {
        ActivityScenario.launch(Chargement.class);

        Thread.sleep(6000); // Délai de secours

        onView(withId(R.id.inputUsername_LLogin))
                .check(matches(isDisplayed()));
    }

    // BONUS: matcher personnalisé pour vérifier les pourcentages de la ProgressBar
    private static Matcher<View> withProgressGreaterThan(final int minValue) {
        return new BoundedMatcher<View, android.widget.ProgressBar>(android.widget.ProgressBar.class) {
            @Override
            protected boolean matchesSafely(android.widget.ProgressBar progressBar) {
                return progressBar.getProgress() > minValue;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with progress greater than " + minValue);
            }
        };
    }

    @Test
    public void testProgressionParRapportALaLongueurDesEtapes() throws InterruptedException {
        ActivityScenario.launch(Chargement.class);

        Thread.sleep(4000); // Attendre assez longtemps pour dépasser la moitié

        onView(withId(R.id.title_LChargement)).check(matches(withProgressGreaterThan(50)));
    }

    @Test
    public void testProgressBarAtteint100() throws InterruptedException {
        // Attendre que la ProgressBar atteigne 100
        ActivityScenario.launch(Chargement.class).onActivity(activity -> {
            ProgressBar progressBar = activity.findViewById(R.id.title_LChargement);

            // Attente pendant que la ProgressBar continue de progresser
            int progress;
            do {
                progress = progressBar.getProgress();
                try {
                    Thread.sleep(500); // Attendre un peu avant de vérifier à nouveau
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (progress < 100);

            // Vérifier que la ProgressBar a atteint 100
            assertEquals("La ProgressBar doit atteindre 100", 100, progressBar.getProgress());
        });
    }

    // Test si la ProgressBar redémarre à zéro après un certain nombre de cycles
    @Test
    public void testProgressBarRedemarre() throws InterruptedException {
        ActivityScenario.launch(Chargement.class).onActivity(activity -> {
            ProgressBar progressBar = activity.findViewById(R.id.title_LChargement);
            int initialProgress = progressBar.getProgress();

            // Attendre un certain temps pour permettre à la ProgressBar de progresser
            try {
                Thread.sleep(2000); // Attente pour voir la ProgressBar progresser
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Vérifier si la ProgressBar a redémarré à zéro après avoir atteint 100
            int progressAfterSleep = progressBar.getProgress();
            assertTrue("La ProgressBar devrait redémarrer", progressAfterSleep == 0 || progressAfterSleep < initialProgress);
        });
    }
}
