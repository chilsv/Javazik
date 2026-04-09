package metier;

import java.util.ArrayList;

public class Catalogue {
    private ArrayList<Morceau> morceaux = new ArrayList<Morceau>();
    private ArrayList<Playlist> playlists = new ArrayList<Playlist>();
    private ArrayList<Artiste> artistes = new ArrayList<Artiste>();
    private ArrayList<Album> albums = new ArrayList<Album>();

    public Catalogue(ArrayList<Morceau> morceaux, ArrayList<Playlist> playlists, ArrayList<Artiste> artistes, ArrayList<Album> albums) {
        this.morceaux = morceaux;
        this.playlists = playlists;
        this.artistes = artistes;
        this.albums = albums;
    }

    public ResultatRecherche chercher(Filtre filtre) {
        ResultatRecherche resultat = new ResultatRecherche();
        // Si on cherche un morceau
            if (filtre.morceau) {
                for (Morceau morceau : morceaux) {
                    if (morceau.getNom().toLowerCase().contains(filtre.recherche.toLowerCase())) {
                        resultat.morceaux.add(morceau);
                    }
                }
                resultat.morceaux = filtre.trier(resultat.morceaux);
            // Si on cherche un artiste
            } else if (filtre.artiste) {
                
            // Si on cherche un album
            } else if (filtre.album) {
            
            // Si on cherche une playlist
            } else if (filtre.playlist) {
                
            }
        return resultat;
    }
}
