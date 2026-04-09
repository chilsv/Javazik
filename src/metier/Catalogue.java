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

    public ArrayList<Morceau> chercherMorceaux(String recherche, ArrayList<Morceau> trouvesMorceaux) {
        for (Morceau morceau : this.morceaux) {
            if (morceau.getNom().toLowerCase().contains(recherche.toLowerCase())) {
                trouvesMorceaux.add(morceau);
            }
        }
        return trouvesMorceaux;
    }

    public ArrayList<Morceau> chercherMorceauxFiltre(String recherche, ArrayList<Morceau> trouvesMorceaux) {
        // Mettre une condition et implémenter les conditions en paramètre
        return trouvesMorceaux;
    }

    public ArrayList<Artiste> chercherArtistes(String recherche, ArrayList<Artiste> trouvesArtistes) {
        for (Artiste artiste : this.artistes) {
                if (artiste.getNom().toLowerCase().contains(recherche.toLowerCase())) {
                    trouvesArtistes.add(artiste);
                }
            }
        return trouvesArtistes;
    }

    public ArrayList<Artiste> chercherArtistesFiltre(String recherche, ArrayList<Artiste> trouvesArtistes) {
        // Mettre une condition et implémenter les conditions en paramètre
        return trouvesArtistes;
    }

}
