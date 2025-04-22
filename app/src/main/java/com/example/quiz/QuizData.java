package com.example.quiz;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class QuizData {

    // Tag pour les logs
    private static final String TAG = "QuizData";

    /**
     * Cette méthode charge les données d'un quiz spécifique à partir d'un fichier JSON.
     * @param context Contexte de l'application.
     * @param quizId Identifiant du quiz à charger.
     * @return Un objet JSONObject représentant le quiz ou null si non trouvé.
     */
    public static JSONObject loadQuiz(Context context, int quizId) {
        Log.d(TAG, "loadQuiz: Tentative de chargement du quiz avec l'ID " + quizId);

        try {
            // Charger le fichier JSON depuis les assets
            InputStream inputStream = context.getAssets().open("quiz_data.json");
            int size = inputStream.available();  // Récupérer la taille du fichier
            byte[] buffer = new byte[size];  // Tableau de bytes pour contenir les données

            // Lire le fichier dans le buffer et capturer le nombre de bytes lus
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != size) {
                Log.w(TAG, "loadQuiz: Nombre de bytes lus ne correspond pas à la taille du fichier");
            }
            inputStream.close();  // Fermer le fichier après lecture

            // Convertir le contenu du fichier JSON en String
            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            Log.d(TAG, "loadQuiz: Contenu JSON chargé avec succès");

            // Convertir la chaîne JSON en un objet JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray quizzes = jsonObject.getJSONArray("quizzes");  // Récupérer le tableau de quiz

            // Recherche du quiz avec l'ID correspondant
            for (int i = 0; i < quizzes.length(); i++) {
                JSONObject quiz = quizzes.getJSONObject(i);
                if (quiz.getInt("id") == quizId) {
                    Log.d(TAG, "loadQuiz: Quiz trouvé avec l'ID " + quizId);
                    return quiz;  // Retourner le quiz trouvé
                }
            }

            // Si le quiz n'a pas été trouvé
            Log.d(TAG, "loadQuiz: Aucun quiz trouvé avec l'ID " + quizId);

        } catch (Exception e) {
            // Utilisation de Log.e pour logger l'exception de manière plus robuste
            Log.e(TAG, "Erreur lors du chargement du fichier JSON: ", e);  // Log complet de l'exception
        }

        // Retourner null si aucune correspondance n'a été trouvée
        return null;
    }
}
