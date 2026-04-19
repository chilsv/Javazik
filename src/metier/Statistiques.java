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
    public Morceau morceauLePlusJoue;
    public Morceau morceauLeMieuxNote;
    public Artiste artisteLePlusJoue;
    public Artiste artisteLeMieuxNote;

    // stats perso
    public Abonne abonne;
    public Map<Morceau, Integer> ecoutes = new HashMap<Morceau, Integer>();
    public Map<Artiste, Integer> ecoutesArtistes = new HashMap<Artiste, Integer>();
    public int nbMorceauxAimes;
    public int nbPlaylistsAbonne;
    public int nbAvisAbonne;
    public Artiste artistePrefere;
    public Morceau morceauPrefere;
    public int nbMorceauxJoues;
    public int nbMorceauPrefJoues;
    public int nbArtistePrefJoues;
    public int tempsEcouteTotal = 0;

    /* constructeurs stats généralzes */
    public Statistiques(Catalogue catalogue, ArrayList<Abonne> abonnes, ArrayList<Avis> avis) {
        this.nbAbonnes = abonnes.size();
        this.nbMorceaux = catalogue.getMorceaux().size();
        this.nbPlaylists = catalogue.getPlaylists().size();
        this.nbArtistes = catalogue.getArtistes().size();
        this.nbAvis = avis.size();
        getMeilleuresNotes(catalogue);
        this.morceauLePlusJoue = getMorceauPlusJoue(catalogue);
        this.artisteLePlusJoue = getArtistePlusJoue(catalogue);
    }

    /* constructeurs stats perso */
    public Statistiques(Abonne abonne, Catalogue catalogue) {
        this.abonne = abonne;
        getEcoutes(abonne);
        this.artistePrefere = getArtistePrefere();
        this.morceauPrefere = getMorceauPrefere();
        Playlist playlistAimes = catalogue.getPlaylist(abonne.getAimes());
        this.nbMorceauxAimes = (playlistAimes != null && playlistAimes.getMorceaux() != null) ? playlistAimes.getMorceaux().size() : 0;
        this.nbPlaylistsAbonne = abonne.getPlaylists().size();
        this.nbAvisAbonne = abonne.getAvis().size();
    }

    public void getEcoutes(Abonne abonne) {
        Map<Morceau, LocalDateTime> historique = abonne.getHistorique();
        for (Morceau morceau : historique.keySet()) {
            nbMorceauxJoues++;
            tempsEcouteTotal += morceau.getDuree();
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
        nbArtistePrefJoues = maxEcoutes;
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
        nbMorceauPrefJoues = maxEcoutes;
        return morceauPrefere;
    }

    public void getMeilleuresNotes(Catalogue catalogue) {
        float maxNoteMorceau = 0;
        float maxNoteArtiste = 0;
        for (Morceau morceau : catalogue.getMorceaux()) {
            if (morceau.getNoteMoy() > maxNoteMorceau) {
                maxNoteMorceau = morceau.getNoteMoy();
                morceauLeMieuxNote = morceau;
            }
        }
        for (Artiste artiste : catalogue.getArtistes()) {
            if (artiste.getNoteMoy() > maxNoteArtiste) {
                maxNoteArtiste = artiste.getNoteMoy();
                artisteLeMieuxNote = artiste;
            }
        }
    }

    public Morceau getMorceauPlusJoue(Catalogue catalogue) {
        int maxEcoutes = 0;
        for (Morceau morceau : catalogue.getMorceaux()) {
            if (morceau.getNbEcoutes() > maxEcoutes) {
                maxEcoutes = morceau.getNbEcoutes();
                morceauLePlusJoue = morceau;
            }
        }
        return morceauLePlusJoue;
    }

    public Artiste getArtistePlusJoue(Catalogue catalogue) {
        int maxEcoutes = 0;
        for (Artiste artiste : catalogue.getArtistes()) {
            if (artiste.getNbEcoutes() > maxEcoutes) {
                maxEcoutes = artiste.getNbEcoutes();
                artisteLePlusJoue = artiste;
            }
        }
        return artisteLePlusJoue;
    }
}
