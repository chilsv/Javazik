package vue;

import java.util.ArrayList;

import metier.Morceau;

// Formulaire pour créer un artiste
public final class PlaylistForm {
    public final String nom;
    public ArrayList<Morceau> morceaux = new ArrayList<Morceau>();

    public PlaylistForm(String nom, ArrayList<Morceau> morceaux) {
        this.nom = nom;
        this.morceaux = morceaux;
    }

    public PlaylistForm(String nom) {
        this.nom = nom;
    }
}
