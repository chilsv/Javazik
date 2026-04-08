package metier;

import java.util.ArrayList;

public class Album {
    private int num;
    private String nom;
    private Artiste artiste;
    private int annee;
    private ArrayList<Morceau> morceaux;

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }

}
