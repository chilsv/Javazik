package metier;

import java.time.LocalDate;
import java.util.ArrayList;

public class Playlist implements TypeObjets {
    private int num;
    private String nom;
    private int numUtilisateur;
    private ArrayList<Morceau> morceaux = new ArrayList<Morceau>();
    private LocalDate dateCreation;
    private String image;

    public Playlist(String nom, ArrayList<Morceau> morceaux, Catalogue catalogue) {
        this.nom = nom;
        this.morceaux = morceaux;
        this.num = catalogue.getPlaylists().size() + 1;
        this.dateCreation = LocalDate.now();
    }

    public Playlist(String nom, int numUtilisateur, Catalogue catalogue) {
        this.nom = nom;
        this.numUtilisateur = numUtilisateur;
        this.num = catalogue.getPlaylists().size() + 1;
        this.dateCreation = LocalDate.now();
    }

    public Playlist(String nom, ArrayList<Morceau> morceaux, Catalogue catalogue, int numUtilisateur) {
        this.nom = nom;
        this.morceaux = morceaux;
        this.numUtilisateur = numUtilisateur;
        this.num = catalogue.getPlaylists().size() + 1;
        this.dateCreation = LocalDate.now();
    }

    public boolean morceauDedans(Morceau morceau) {
        return morceaux.contains(morceau);
    }

    public void ajouterMorceau(Morceau morceau) {
        morceaux.add(morceau);
    }

    public void enleverMorceau(Morceau morceau) {
        morceaux.remove(morceau);
    }

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }

    public int getAnnee() {
        // On ne peut dire qu'une playlist a une année, du coup on renvoie celle du morceau n°1
        return morceaux.get(0).getAnnee();
    }

    public String getNom() {
        return nom;
    }

    public int getNum() {
        return num;
    }

    public int getNumUtilisateur() {
        return numUtilisateur;
    }

    public LocalDate getCreation() {
        return dateCreation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
