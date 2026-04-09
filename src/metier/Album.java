package metier;

import java.util.ArrayList;

public class Album implements TypeObjets {
    private int num;
    private String Titre;
    private Artiste artiste;
    private int annee;
    private ArrayList<Morceau> morceaux;
    private ArrayList<String> genres;

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public int getAnnee() {
        return annee;
    }

    public String getNom() {
        return Titre;
    }
}
