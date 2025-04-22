package com.example.quiz;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.*;
import java.io.*;
import java.util.*;

public class Login extends AppCompatActivity {

    private EditText inputUsername, inputCode;
    private Button btnLogin, btnRegister;
    private final String FILENAME = "id_data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        inputUsername = findViewById(R.id.inputUsername);
        inputCode = findViewById(R.id.inputCode);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void loginUser() {
        String username = inputUsername.getText().toString().trim();
        String code = inputCode.getText().toString().trim();

        if (username.isEmpty() || code.length() != 4) {
            toast("Entrer un prénom et un code à 4 chiffres");
            return;
        }

        try {
            JSONArray users = readUsers();
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("name").equals(username) && user.getString("code").equals(code)) {
                    toast("Connexion réussie ! ID: " + user.getString("id"));
                    return;
                }
            }
            toast("Utilisateur non trouvé.");
        } catch (Exception e) {
            e.printStackTrace();
            toast("Erreur lors de la lecture du fichier");
        }
    }

    private void registerUser() {
        String username = inputUsername.getText().toString().trim();
        String code = inputCode.getText().toString().trim();

        if (username.isEmpty() || code.length() != 4) {
            toast("Entrer un prénom et un code à 4 chiffres");
            return;
        }

        try {
            JSONArray users = readUsers();
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("name").equals(username)) {
                    toast("Ce nom est déjà utilisé !");
                    return;
                }
            }

            JSONObject newUser = new JSONObject();
            newUser.put("name", username);
            newUser.put("code", code);
            newUser.put("id", generateUniqueID(users));

            users.put(newUser);
            writeUsers(users);

            toast("Compte créé ! Bienvenue " + username);
        } catch (Exception e) {
            e.printStackTrace();
            toast("Erreur lors de l'enregistrement");
        }
    }

    private JSONArray readUsers() throws IOException, JSONException {
        File file = new File(getFilesDir(), FILENAME);
        if (!file.exists()) return new JSONArray();

        FileInputStream fis = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) builder.append(line);
        reader.close();

        return new JSONArray(builder.toString());
    }

    private void writeUsers(JSONArray users) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), FILENAME));
        fos.write(users.toString().getBytes());
        fos.close();
    }

    private String generateUniqueID(JSONArray users) throws JSONException {
        Random rand = new Random();
        String id;
        boolean exists;

        do {
            id = String.format("%05d", rand.nextInt(100000));
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

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
