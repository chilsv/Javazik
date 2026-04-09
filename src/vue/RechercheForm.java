package vue;

// formulaire pour faire une recherche
public final class RechercheForm {
    public final String recherche;
    public final boolean morceau;
    public final boolean artiste;
    public final boolean album;
    public final boolean playlist;
    public final boolean croissant;
    public final int annee;

    public RechercheForm(String recherche, boolean morceau, boolean artiste, boolean album, boolean playlist, boolean croissant, int annee) {
        this.recherche = recherche;
        this.morceau = morceau;
        this.artiste = artiste;
        this.album = album;
        this.playlist = playlist;
        this.croissant = croissant;
        this.annee = annee;
    }
}