package metier;

import java.util.ArrayList;

public class Morceau implements TypeObjets {
    private int num;
    private String Titre;
    private int duree = 180;
    private ArrayList<Artiste> artistes;
    private int annee;
    private ArrayList<String> genres;
    private String image;
    private String chemin;
    private float noteMoy;
    private int nbEcoutes = 0;
    private ArrayList<Avis> avis = new ArrayList<Avis>();

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

    public Morceau(String titre, Artiste artiste, int duree, String chemin, String image) {
        this.Titre = titre;
        this.artistes = new ArrayList<>();
        this.artistes.add(artiste);
        this.duree = duree;
        this.chemin = chemin;
        this.image = image;
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

    public ArrayList<String> getGenres() {
        return genres;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<Avis> getAvis() {
        return avis;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void ajouterAvis(Avis avis) {
        this.avis.add(avis);
        float somme = 0;
        for (Avis a : this.avis) {
            somme += a.getNote();
        }
        noteMoy = somme / this.avis.size();
    }

    public float getNoteMoy() {
        return noteMoy;
    }

    public int getNbEcoutes() {
        return nbEcoutes;
    }

    public void ajouterEcoute() {
        for (Artiste artiste : artistes) {
            artiste.ajouterEcoute();
        }
        nbEcoutes++;
    }
}
