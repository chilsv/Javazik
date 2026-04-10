package controleur.formulaires;

// Formulaire pour créer un morceau
public final class MorceauForm {
    public final String titre;
    public final String artiste;
    public final String album;
    public final int duree;

    public MorceauForm(String titre, String artiste, String album, int duree) {
        this.titre = titre;
        this.artiste = artiste;
        this.album = album;
        this.duree = duree;
    }
}