package controleur;

import java.util.ArrayList;
import java.util.List;

import metier.Album;
import metier.Artiste;
import metier.Catalogue;
import metier.Morceau;
import metier.Playlist;
import metier.Solo;

public class ScriptPeuplement {
    public static void main(String[] args) {
        ArrayList<Morceau> morceaux = new ArrayList<>();
        ArrayList<Playlist> playlists = new ArrayList<>();
        ArrayList<Artiste> artistes = new ArrayList<>();
        ArrayList<Album> albums = new ArrayList<>();

        Main.charger(morceaux, "morceaux.ser");
        Main.charger(playlists, "playlists.ser");
        Main.charger(artistes, "artistes.ser");
        Main.charger(albums, "albums.ser");

        Catalogue catalogue = new Catalogue(morceaux, playlists, artistes, albums);

        Solo daftPunk = getOrCreateSolo(catalogue, "Daft Punk");
        Solo stromae = getOrCreateSolo(catalogue, "Stromae");
        Solo adele = getOrCreateSolo(catalogue, "Adele");

        Album randomAccess = getOrCreateAlbum(catalogue, daftPunk, "Random Access Memories", 2013);
        Album racineCarree = getOrCreateAlbum(catalogue, stromae, "Racine Carree", 2013);
        Album twentyOne = getOrCreateAlbum(catalogue, adele, "21", 2011);

        Morceau getLucky = getOrCreateMorceau(catalogue, daftPunk, randomAccess, "Get Lucky", 248);
        Morceau instantCrush = getOrCreateMorceau(catalogue, daftPunk, randomAccess, "Instant Crush", 337);
        Morceau formi = getOrCreateMorceau(catalogue, stromae, racineCarree, "Formidable", 214);
        Morceau papaoutai = getOrCreateMorceau(catalogue, stromae, racineCarree, "Papaoutai", 232);
        Morceau rolling = getOrCreateMorceau(catalogue, adele, twentyOne, "Rolling in the Deep", 228);

        getOrCreatePlaylist(catalogue, "Top Electro", 1, List.of(getLucky, instantCrush));
        getOrCreatePlaylist(catalogue, "Top Francophone", 1, List.of(formi, papaoutai));
        getOrCreatePlaylist(catalogue, "Top Mix", 1, List.of(getLucky, papaoutai, rolling));

        Main.sauvegarder(catalogue.getMorceaux(), "morceaux.ser");
        Main.sauvegarder(catalogue.getPlaylists(), "playlists.ser");
        Main.sauvegarder(catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(catalogue.getAlbums(), "albums.ser");

        System.out.println("Peuplement termine.");
        System.out.println("Artistes: " + catalogue.getArtistes().size());
        System.out.println("Albums: " + catalogue.getAlbums().size());
        System.out.println("Morceaux: " + catalogue.getMorceaux().size());
        System.out.println("Playlists: " + catalogue.getPlaylists().size());
    }

    private static Solo getOrCreateSolo(Catalogue catalogue, String nom) {
        for (Artiste artiste : catalogue.getArtistes()) {
            if (artiste.getNom().equalsIgnoreCase(nom) && artiste instanceof Solo) {
                return (Solo) artiste;
            }
        }

        Solo solo = new Solo(nom);
        catalogue.ajouterArtiste(solo);
        return solo;
    }

    private static Album getOrCreateAlbum(Catalogue catalogue, Artiste artiste, String titre, int annee) {
        for (Album album : catalogue.getAlbums()) {
            if (album.getNom().equalsIgnoreCase(titre)
                && album.getArtiste().getNom().equalsIgnoreCase(artiste.getNom())) {
                return album;
            }
        }

        Album album = new Album(titre, artiste, annee);
        artiste.ajouterAlbum(album);
        catalogue.ajouterAlbum(album);
        return album;
    }

    private static Morceau getOrCreateMorceau(
        Catalogue catalogue,
        Artiste artiste,
        Album album,
        String titre,
        int duree
    ) {
        for (Morceau morceau : catalogue.getMorceaux()) {
            if (morceau.getNom().equalsIgnoreCase(titre)) {
                return morceau;
            }
        }

        Morceau morceau = new Morceau(titre, artiste, duree);
        album.ajouterMorceau(morceau);
        artiste.ajouterMorceau(morceau);
        catalogue.ajouterMorceau(morceau);
        return morceau;
    }

    private static Playlist getOrCreatePlaylist(
        Catalogue catalogue,
        String nom,
        int numUtilisateur,
        List<Morceau> morceaux
    ) {
        for (Playlist playlist : catalogue.getPlaylists()) {
            if (playlist.getNom().equalsIgnoreCase(nom)) {
                return playlist;
            }
        }

        Playlist playlist = new Playlist(nom, numUtilisateur, catalogue);
        for (Morceau morceau : morceaux) {
            playlist.ajouterMorceau(morceau);
        }

        try {
            catalogue.ajouterPlaylist(playlist);
        } catch (Exception ignored) {
            // Si deja existante, on renvoie la playlist existante plus haut.
        }

        return playlist;
    }
}
