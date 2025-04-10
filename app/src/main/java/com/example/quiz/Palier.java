package com.example.quiz;

public class Palier {

    private int niveau;  // Niveau du palier (1 à 15)
    private int montant; // Montant gagné à ce niveau

    // Constructeur
    public Palier(int niveau, int montant) {
        this.niveau = niveau;
        this.montant = montant;
    }

    // Getter et Setter pour niveau
    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    // Getter et Setter pour montant
    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    // Méthode pour vérifier si le joueur a atteint ce palier
    public boolean estAtteint(int montantGagne) {
        return montantGagne >= this.montant;
    }

    // Méthode pour obtenir le montant du palier à afficher
    public String getMontantAffichage() {
        return String.format("%,d €", montant); // Ajout d'un séparateur de milliers
    }

    // Méthode pour obtenir l'affichage de la couleur en fonction du palier atteint
    public String getCouleurPalier(int montantGagne) {
        // Si le joueur a atteint ou dépassé ce palier, couleur verte
        if (estAtteint(montantGagne)) {
            return "#4CAF50";  // Vert
        } else {
            return "#9E9E9E";  // Gris
        }
    }

    @Override
    public String toString() {
        return "Palier {niveau=" + niveau + ", montant=" + montant + "}";
    }

}
