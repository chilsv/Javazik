package metier;

import java.util.ArrayList;

public class Album implements TypeObjets {
    private int num;
    private String Titre;
    private Artiste artiste;
    private int annee;
    private ArrayList<Morceau> morceaux;
    private ArrayList<String> genres;
    private String image;

    public Album(String titre, Artiste artiste, int annee) {
        this.Titre = titre;
        this.artiste = artiste;
        this.annee = annee;
        this.morceaux = new ArrayList<>();
    }

    public Album(String titre, Artiste artiste) {
        this.Titre = titre;
        this.artiste = artiste;
        this.morceaux = new ArrayList<>();
    }

    public Album(String titre, Artiste artiste, int annee, ArrayList<Morceau> morceaux) {
        this.Titre = titre;
        this.artiste = artiste;
        this.annee = annee;
        this.morceaux = morceaux;
    }

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void ajouterMorceau(Morceau morceau) {
        morceaux.add(morceau);
    }

    public int getAnnee() {
        return annee;
    }

    public String getNom() {
        return Titre;
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public int getNum() {
        return num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
