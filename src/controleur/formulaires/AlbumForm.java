package controleur.formulaires;

import java.util.ArrayList;

import metier.Morceau;

public class AlbumForm {
    public final String titre;
    public final String artiste;
    public final int annee;
    public final ArrayList<Morceau> morceaux;

    public AlbumForm(String titre, String artiste) {
        this.titre = titre;
        this.artiste = artiste;
        this.annee = 0;
        this.morceaux = new ArrayList<>();
    }

    public AlbumForm(String titre, String artiste, int annee) {
        this.titre = titre;
        this.artiste = artiste;
        this.annee = annee;
        this.morceaux = new ArrayList<>();
    }

    public AlbumForm(String titre, String artiste, int annee, ArrayList<Morceau> morceaux) {
        this.titre = titre;
        this.artiste = artiste;
        this.annee = annee;
        this.morceaux = morceaux;
    }
}