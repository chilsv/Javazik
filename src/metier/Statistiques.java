package metier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Statistiques {
    // stats générales
    public int nbAbonnes;
    public int nbMorceaux;
    public int nbArtistes;
    public int nbPlaylists;
    public int nbAvis;

    // stats perso
    public Abonne abonne;
    public Map<Morceau, Integer> ecoutes = new HashMap<>();
    public Map<Artiste, Integer> ecoutesArtistes = new HashMap<>();
    public int nbMorceauxAimes;
    public int nbPlaylistsAbonne;
    public int nbAvisAbonne;
    public Artiste artistePrefere;
    public Morceau morceauPrefere;
    public int nbMorceauxJoues;
    public int nbMorceauPrefJoues;
    public int nbArtistePrefJoues;

    /* constructeurs stats généralzes */
    public Statistiques(Catalogue catalogue, ArrayList<Abonne> abonnes, ArrayList<Avis> avis) {
        this.nbAbonnes = abonnes.size();
        this.nbMorceaux = catalogue.getMorceaux().size();
        this.nbPlaylists = catalogue.getPlaylists().size();
        this.nbArtistes = catalogue.getArtistes().size();
        this.nbAvis = avis.size();
    }

    /* constructeurs stats perso */
    public Statistiques(Abonne abonne, Catalogue catalogue) {
        this.abonne = abonne;
        getEcoutes(abonne);
        this.artistePrefere = getArtistePrefere();
        this.morceauPrefere = getMorceauPrefere();
        this.nbMorceauxAimes = catalogue.getPlaylist(abonne.getAimes()).getMorceaux().size();
        this.nbPlaylistsAbonne = abonne.getPlaylists().size();
        this.nbAvisAbonne = abonne.getAvis().size();
        //this.nbMorceauxJoues = abonne.getNbMorceauxJoues();
        //this.nbMorceauPrefJoues = abonne.getNbMorceauPrefJoues();
        //this.nbArtistePrefJoues = abonne.getNbArtistePrefJoues();
    }

    public void getEcoutes(Abonne abonne) {
        Map<Morceau, LocalDateTime> historique = abonne.getHistorique();
        for (Morceau morceau : historique.keySet()) {
            ArrayList<Artiste> artistes = morceau.getArtistes();
            // on ajoute une écoute pour le morceau
            if (ecoutes.containsKey(morceau)) {
                ecoutes.put(morceau, ecoutes.get(morceau) + 1);
            } else {
                ecoutes.put(morceau, 1);
            }
            // on parcourt les artistes
            for (Artiste artiste : artistes) {
                // on ajoute une écoute pour l'artiste
                if (ecoutesArtistes.containsKey(artiste)) {
                    ecoutesArtistes.put(artiste, ecoutesArtistes.get(artiste) + 1);
                } else {
                    ecoutesArtistes.put(artiste, 1);
                }
            }
        }
    }

    public Artiste getArtistePrefere() {
        Artiste artistePrefere = null;
        int maxEcoutes = 0;
        for (Artiste artiste : ecoutesArtistes.keySet()) {
            if (ecoutesArtistes.get(artiste) > maxEcoutes) {
                maxEcoutes = ecoutesArtistes.get(artiste);
                artistePrefere = artiste;
            }
        }
        return artistePrefere;
    }

    public Morceau getMorceauPrefere() {
        Morceau morceauPrefere = null;
        int maxEcoutes = 0;
        for (Morceau morceau : ecoutes.keySet()) {
            if (ecoutes.get(morceau) > maxEcoutes) {
                maxEcoutes = ecoutes.get(morceau);
                morceauPrefere = morceau;
            }
        }
        return morceauPrefere;
    }
}
