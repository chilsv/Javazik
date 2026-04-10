package metier;

import java.util.ArrayList;

public class Album implements TypeObjets {
    private int num;
    private String Titre;
    private Artiste artiste;
    private int annee;
    private ArrayList<Morceau> morceaux;
    private ArrayList<String> genres;
    // ajouter image peut-être

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
}
