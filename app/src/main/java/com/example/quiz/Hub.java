package com.example.quiz;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class Hub extends AppCompatActivity {

    private LinearLayout playersList;
    private Button btnStartGame, btnEditSettings, btnDeleteHub;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hub);

        // Initialisation des éléments de l'interface
        playersList = findViewById(R.id.playersList);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnEditSettings = findViewById(R.id.btnEditSettings);
        btnDeleteHub = findViewById(R.id.btnDeleteHub);
        btnBack = findViewById(R.id.btnBack);

        // Ajouter des joueurs à la liste
        addPlayerToHub("Joueur1", "id1");
        addPlayerToHub("Joueur2", "id2");

        // Fonction pour retourner à la page précédente
        btnBack.setOnClickListener(v -> onBackPressed());

        // Fonction pour lancer la partie
        btnStartGame.setOnClickListener(v -> {
            if (playersList.getChildCount() >= 2) {
                // Logique pour lancer la partie
                // Peut-être une redirection vers une autre activité ou un message
            }
        });

        // Fonction pour modifier les paramètres
        btnEditSettings.setOnClickListener(v -> {
            // Logique pour modifier les paramètres du quiz
            // Par exemple, ouvrir une nouvelle activité pour modifier les paramètres
        });

        // Fonction pour supprimer le hub
        btnDeleteHub.setOnClickListener(v -> {
            // Logique pour supprimer le hub
            // Cela peut être un prompt de confirmation ou directement une suppression
        });
    }

    // Méthode pour ajouter un joueur à la liste
    private void addPlayerToHub(String playerName, String playerId) {
        @SuppressLint("ResourceType") View playerView = getLayoutInflater().inflate(R.drawable.player_bubble, null);

        // Initialisation des vues pour chaque joueur
        TextView playerNameTextView = playerView.findViewById(R.id.playerName);
        ImageButton btnRemovePlayer = playerView.findViewById(R.id.btnRemovePlayer);

        // Affecter le nom du joueur à la vue
        playerNameTextView.setText(playerName);

        // Logique pour supprimer un joueur
        btnRemovePlayer.setOnClickListener(v -> {
            playersList.removeView(playerView);
            // Optionnel : On peut également gérer la suppression dans une liste de joueurs ou en base de données
        });

        // Ajouter la vue de joueur à la liste
        playersList.addView(playerView);
    }
}
