package metier;

import java.util.ArrayList;

public class Morceau implements TypeObjets {
    private int num;
    private String Titre;
    private int duree = 180;
    private ArrayList<Artiste> artistes;
    private int annee;
    private ArrayList<String> genres;

    public Morceau(String titre, Artiste artiste) {
        this.Titre = titre;
        this.artistes = new ArrayList<>();
        this.artistes.add(artiste);
    }

    public Morceau(String titre, ArrayList<Artiste> artistes) {
        this.Titre = titre;
        this.artistes = new ArrayList<>();
        this.artistes.addAll(artistes);
    }

    public Morceau(String titre, Artiste artiste, int duree) {
        this.Titre = titre;
        this.artistes = new ArrayList<>();
        this.artistes.add(artiste);
        this.duree = duree;
    }

    public Morceau(String titre, ArrayList<Artiste> artistes, int duree) {
        this.Titre = titre;
        this.artistes = new ArrayList<>();
        this.artistes.addAll(artistes);
        this.duree = duree;
    }

    public int getNum() {
        return num;
    }

    public int getDuree() {
        return duree;
    }

    public String getNom() {
        return Titre;
    }

    public int getAnnee() {
        return annee;
    }

    public ArrayList<Artiste> getArtistes() {
        return artistes;
    }

    public void setAlbum(String nomAlbum) {
        for (Artiste artiste : artistes) {
            for (Album album : artiste.getAlbums()) {
                // On vérifie que kl'album est pas déjà là
                if (album.getNom().equals(nomAlbum)) {
                    return;
                }
            }
            Album album = new Album(nomAlbum, artiste);
            album.ajouterMorceau(this);
            artiste.ajouterAlbum(album);
        }
    }
}
