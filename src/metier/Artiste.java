package metier;

import java.util.ArrayList;

public abstract class Artiste {
    private int num;
    private String nom;
    private ArrayList<Morceau> morceaux;
    private ArrayList<Album> albums;

    public Artiste(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void ajouterMorceau(Morceau morceau) {
        morceaux.add(morceau);
    }

    public boolean contientMorceau(Morceau morceau) {
        for (Morceau m : morceaux) {
            if (m.getNum() == morceau.getNum()) {
                return true;
            }
        }
        return false;
    }

    public void ajouterAlbum(Album album) {
        albums.add(album);
        for (Morceau morceau : album.getMorceaux()) {
            // On vérifie que le morceau n'est pas déjà dans la liste de l'artiste
            if (!this.contientMorceau(morceau)) {
                morceaux.add(morceau);
            }
        }
    }
}
