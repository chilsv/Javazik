package controleur.formulaires;

import java.util.ArrayList;

import metier.Morceau;

// Formulaire pour créer un artiste
public final class PlaylistForm {
    public final String nom;
    public ArrayList<Morceau> morceaux = new ArrayList<Morceau>();
    public int numCreateur;

    public PlaylistForm(String nom, int numCreateur) {
        this.nom = nom;
        this.numCreateur = numCreateur;
    }

    public PlaylistForm(String nom, ArrayList<Morceau> morceaux, int numCreateur) {
        this.nom = nom;
        this.morceaux = morceaux;
        this.numCreateur = numCreateur;
    }
}
