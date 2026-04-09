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

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public ArrayList<Artiste> getArtistes() {
        return artistes;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void ajouterMorceau(Morceau morceau) {
        morceaux.add(morceau);
        // Permet de mettre à jour le reste
        for (Artiste artisteMorceau : morceau.getArtistes()) {
            boolean artisteExiste = false;
            for (Artiste artiste : artistes) {
                if (artiste.getNom().equalsIgnoreCase(artisteMorceau.getNom())) {
                    artisteExiste = true;
                    break;
                }
            }
            if (!artisteExiste) {
                artistes.add(artisteMorceau);
            }

            for (Album albumMorceau : artisteMorceau.getAlbums()) {
                boolean albumExiste = false;
                for (Album album : albums) {
                    if (album.getNom().equalsIgnoreCase(albumMorceau.getNom())) {
                        albumExiste = true;
                        break;
                    }
                }
                if (!albumExiste) {
                    albums.add(albumMorceau);
                }
            }
        }
    }

    public void ajouterPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    public void ajouterArtiste(Artiste artiste) {
        artistes.add(artiste);
    }

    public void ajouterAlbum(Album album) {
        albums.add(album);
    }

    public ResultatRecherche chercher(Filtre filtre) {
        ResultatRecherche resultat = new ResultatRecherche();
        // on fait une recherche par rapport aux filtres choisis et on retourne le résultat
        String recherche = filtre.recherche == null ? "" : filtre.recherche.toLowerCase();

        //flemme d'expliquer mais c'est un copier-coller
        // pour chaque type (morceau, artiste...), on vérifie si c'est le filtre appliqué
        if (filtre.morceau) {
            for (Morceau morceau : morceaux) {
                if (morceau.getNom().toLowerCase().contains(recherche)) {
                    resultat.morceaux.add(morceau);
                }
            }
            resultat.morceaux = filtre.trier(resultat.morceaux);
        }

        if (filtre.artiste) {
            for (Artiste artiste : artistes) {
                if (artiste.getNom().toLowerCase().contains(recherche)) {
                    resultat.artistes.add(artiste);
                }
            }
            resultat.artistes = filtre.trier(resultat.artistes);
        }

        if (filtre.album) {
            for (Album album : albums) {
                if (album.getNom().toLowerCase().contains(recherche)) {
                    resultat.albums.add(album);
                }
            }
            resultat.albums = filtre.trier(resultat.albums);
        }

        if (filtre.playlist) {
            for (Playlist playlist : playlists) {
                if (playlist.getNom().toLowerCase().contains(recherche)) {
                    resultat.playlists.add(playlist);
                }
            }
            resultat.playlists = filtre.trier(resultat.playlists);
        }

        return resultat;
    }
}
