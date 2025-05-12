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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

        if (username.isEmpty() || code.length() != 4) {
            toast("Entrer un prénom et un code à 4 chiffres");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        usersRef
                .whereEqualTo("name", username)
                .whereEqualTo("code", code)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String id = doc.getString("id");
                            toast("Connexion réussie !");
                            navigateToMenu(username, id);
                            return;
                        }
                    } else {
                        toast("Utilisateur non trouvé.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors de la recherche Firebase", e);
                    toast("Erreur lors de la connexion");
                });
    }

    /**
     * Enregistrement d'un nouvel utilisateur
     */
    private void registerUser() {
        String username = inputUsername.getText().toString().trim();
        String code = inputCode.getText().toString().trim();

        if (username.isEmpty() || code.length() != 4) {
            toast("Entrer un prénom et un code à 4 chiffres");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        usersRef.whereEqualTo("name", username).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        toast("Ce nom est déjà utilisé !");
                    } else {
                        String id = String.format(Locale.getDefault(), "%05d", new Random().nextInt(100000));

                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("name", username);
                        newUser.put("code", code);
                        newUser.put("id", id);

                        usersRef.add(newUser)
                                .addOnSuccessListener(documentReference -> {
                                    toast("Compte créé ! Bienvenue " + username);
                                    navigateToMenu(username, id);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Erreur lors de l'ajout Firebase", e);
                                    toast("Erreur lors de l'enregistrement");
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors de la vérification de l'existence", e);
                    toast("Erreur lors de l'enregistrement");
                });
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
