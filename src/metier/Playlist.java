package metier;

import java.time.LocalDate;
import java.util.ArrayList;

public class Playlist implements TypeObjets {
    private int num;
    private String nom;
    private ArrayList<Morceau> morceaux = new ArrayList<Morceau>();
    private LocalDate dateCreation;

    public Playlist(String nom) {
        this.nom = nom;
        this.dateCreation = LocalDate.now();
    }

    public Playlist(String nom, ArrayList<Morceau> morceaux) {
        this.nom = nom;
        this.morceaux = morceaux;
        this.dateCreation = LocalDate.now();
    }

    public void ajouterMorceau(Morceau morceau) {
        morceaux.add(morceau);
    }

    public int getAnnee() {
        // On ne peut dire qu'une playlist a une année, du coup on renvoie celle du morceau n°1
        return morceaux.get(0).getAnnee();
    }

    public String getNom() {
        return nom;
    }
}
