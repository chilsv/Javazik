package controleur.formulaires;

import metier.Filtre;

// formulaire pour faire une recherche
public final class RechercheForm {
    public final String recherche;
    public final boolean morceau;
    public final boolean artiste;
    public final boolean album;
    public final boolean playlist;
    public final boolean croissant;
    public final int[] annee;

    public RechercheForm(String recherche, boolean morceau, boolean artiste, boolean album, boolean playlist, boolean croissant, int[] annee) {
        this.recherche = recherche;
        this.morceau = morceau;
        this.artiste = artiste;
        this.album = album;
        this.playlist = playlist;
        this.croissant = croissant;
        this.annee = (annee == null || annee.length < 2) ? new int[] {0, 0} : new int[] {annee[0], annee[1]};
    }

    public RechercheForm(String recherche, Filtre filtre) {
        this.recherche = recherche;
        this.morceau = filtre.morceau;
        this.artiste = filtre.artiste;
        this.album = filtre.album;
        this.playlist = filtre.playlist;
        this.croissant = filtre.croissant;
        this.annee = new int[] {filtre.annees[0], filtre.annees[1]};
    }
}