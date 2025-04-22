package com.example.quiz;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Login extends AppCompatActivity {

    // Constantes
    private static final String TAG = "Login"; // Pour les logs Logcat
    private static final String FILENAME = "id_data.json";

    // UI Components
    private EditText inputUsername, inputCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Lancement de l'animation du logo
        ImageView logo = findViewById(R.id.logoImage_LLogin);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        logo.startAnimation(rotateAnimation);

        // Récupération des vues
        inputUsername = findViewById(R.id.inputUsername_LLogin);
        inputCode = findViewById(R.id.inputCode_LLogin);
        Button btnLogin = findViewById(R.id.btnLogin_LLogin);
        Button btnRegister = findViewById(R.id.btnRegister_LLogin);

        // Gestion des événements
        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> registerUser());
    }

    /**
     * Connexion de l'utilisateur existant
     */
    private void loginUser() {
        String username = inputUsername.getText().toString().trim();
        String code = inputCode.getText().toString().trim();

        // Vérification des champs
        if (username.isEmpty() || code.length() != 4) {
            toast("Entrer un prénom et un code à 4 chiffres");
            return;
        }

        try {
            JSONArray users = readUsers();
            for (int i = 0; i < Objects.requireNonNull(users).length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("name").equals(username) && user.getString("code").equals(code)) {
                    String id = user.getString("id");
                    toast("Connexion réussie !");
                    Log.d(TAG, "Connexion utilisateur réussie : " + username + ", ID: " + id);
                    navigateToMenu(username, id);
                    return;
                }
            }
            toast("Utilisateur non trouvé.");
            Log.d(TAG, "Échec connexion : Utilisateur non trouvé (" + username + ")");
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la lecture du fichier JSON", e);
            toast("Erreur lors de la lecture du fichier");
        }
    }

    /**
     * Enregistrement d'un nouvel utilisateur
     */
    private void registerUser() {
        String username = inputUsername.getText().toString().trim();
        String code = inputCode.getText().toString().trim();

        // Vérification des champs
        if (username.isEmpty() || code.length() != 4) {
            toast("Entrer un prénom et un code à 4 chiffres");
            return;
        }

        try {
            JSONArray users = readUsers();

            // Vérifie si l'utilisateur existe déjà
            for (int i = 0; i < Objects.requireNonNull(users).length(); i++) {
                if (users.getJSONObject(i).getString("name").equals(username)) {
                    toast("Ce nom est déjà utilisé !");
                    Log.d(TAG, "Échec enregistrement : nom déjà utilisé (" + username + ")");
                    return;
                }
            }

            // Création du nouvel utilisateur
            String id = generateUniqueID(users);
            JSONObject newUser = new JSONObject();
            newUser.put("name", username);
            newUser.put("code", code);
            newUser.put("id", id);

            users.put(newUser);
            writeUsers(users);

            toast("Compte créé ! Bienvenue " + username);
            Log.d(TAG, "Utilisateur enregistré : " + username + ", ID: " + id);
            navigateToMenu(username, id);
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'enregistrement", e);
            toast("Erreur lors de l'enregistrement");
        }
    }

    /**
     * Lecture du fichier JSON des utilisateurs
     */
    private JSONArray readUsers() throws IOException, JSONException {
        Path filePath = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            filePath = Paths.get(getFilesDir().toString(), FILENAME);
        }

        // Vérification si le fichier existe
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Files.exists(filePath)) {
                Log.d(TAG, "Fichier utilisateur inexistant, création d'une nouvelle liste.");
                return new JSONArray(); // Retourne une nouvelle liste si le fichier n'existe pas
            }
        }

        // Utilisation de Files.newBufferedReader() pour lire le fichier de manière moderne
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                StringBuilder builder = new StringBuilder();
                String line;

                // Lecture lignes par ligne du fichier et concaténation
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                // Retourner le contenu du fichier sous forme de JSONArray
                return new JSONArray(builder.toString());
            }
        }
        return null;
    }



    /**
     * Écriture dans le fichier JSON des utilisateurs
     */
    private void writeUsers(JSONArray users) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), FILENAME))) {
            fos.write(users.toString().getBytes());
            Log.d(TAG, "Utilisateurs sauvegardés dans " + FILENAME);
        }
    }

    /**
     * Génère un ID unique à 5 chiffres
     */
    private String generateUniqueID(JSONArray users) throws JSONException {
        Random rand = new Random();
        String id;
        boolean exists;

        do {
            // Utilisation de String avec une locale explicite
            id = String.format(Locale.getDefault(), "%05d", rand.nextInt(100000)); // Ajout de Locale pour éviter l'usage par défaut
            exists = false;
            for (int i = 0; i < users.length(); i++) {
                if (users.getJSONObject(i).getString("id").equals(id)) {
                    exists = true;
                    break;
                }
            }
        } while (exists);

        return id;
    }

    /**
     * Redirection vers l'écran du menu principal
     */
    private void navigateToMenu(String username, String id) {
        Intent intent = new Intent(Login.this, MenuDuJeu.class);
        intent.putExtra("username", username);
        intent.putExtra("user_id", id);
        startActivity(intent);
        finish(); // Fin de l'activité actuelle
        Log.d(TAG, "Navigation vers MenuDuJeu avec utilisateur : " + username);
    }

    /**
     * Affiche un toast court
     */
    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
