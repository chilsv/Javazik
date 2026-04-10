package metier;

import java.util.ArrayList;

public class Filtre {
    public String recherche;
    public boolean morceau;
    public boolean artiste;
    public boolean album;
    public boolean playlist;
    public boolean croissant;
    public int annee;

    public Filtre(String recherche, boolean morceau, boolean artiste, boolean album, boolean playlist, boolean croissant, int annee) {
        this.recherche = recherche;
        this.morceau = morceau;
        this.artiste = artiste;
        this.album = album;
        this.playlist = playlist;
        this.croissant = croissant;
        this.annee = annee;
    }

    public <T extends TypeObjets> ArrayList<T> trier(ArrayList<T> liste) {
        // Implémentation du tri en fonction du critère de tri (croissant ou décroissant)
        if (annee != 0) {
            // Tri par année
            for (int i = 0; i < liste.size(); i++) {
                if (liste.get(i).getAnnee() != annee) {
                    liste.remove(i);
                    i--;
                }
            }
        }
        // Tri alpahbétique
        if (croissant) {
            liste.sort((a, b) -> a.getNom().compareToIgnoreCase(b.getNom()));
        } else {
            liste.sort((a, b) -> b.getNom().compareToIgnoreCase(a.getNom()));
        }
        return liste;
    }
}
