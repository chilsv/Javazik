package metier;

import java.util.ArrayList;
import java.util.Arrays;

public class Filtre {
    public boolean morceau;
    public boolean artiste;
    public boolean album;
    public boolean playlist;
    public boolean croissant;
    public int[] annees;

    public Filtre(boolean morceau, boolean artiste, boolean album, boolean playlist, boolean croissant, int[] annees) {
        this.morceau = morceau;
        this.artiste = artiste;
        this.album = album;
        this.playlist = playlist;
        this.croissant = croissant;
        this.annees = normaliserAnnees(annees);
    }

    public <T extends TypeObjets> ArrayList<T> trier(ArrayList<T> liste) {
        // Implémentation du tri en fonction du critère de tri (croissant ou décroissant)
        int debut = annees[0];
        int fin = annees[1];

        // 0,0 => pas de filtre d'annee
        if (!(debut == 0 && fin == 0)) {
            if (debut > fin) {
                int tmp = debut;
                debut = fin;
                fin = tmp;
            }

            if (debut == 0) {
                debut = Integer.MIN_VALUE;
            }
            if (fin == 0) {
                fin = Integer.MAX_VALUE;
            }

            for (int i = 0; i < liste.size(); i++) {
                int anneeObjet = liste.get(i).getAnnee();
                if (anneeObjet < debut || anneeObjet > fin) {
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

    private static int[] normaliserAnnees(int[] annees) {
        if (annees == null || annees.length < 2) {
            return new int[] {0, 0};
        }
        return Arrays.copyOf(annees, 2);
    }
}
