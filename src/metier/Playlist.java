package metier;

import java.util.ArrayList;

public class Playlist {
    private int num;
    private String nom;
    private ArrayList<Morceau> morceaux = new ArrayList<Morceau>();

    public Playlist(String nom) {
        this.nom = nom;
    }

    public Playlist(String nom, ArrayList<Morceau> morceaux) {
        this.nom = nom;
        this.morceaux = morceaux;
    }

    public void ajouterMorceau(Morceau morceau) {
        morceaux.add(morceau);
    }
}
