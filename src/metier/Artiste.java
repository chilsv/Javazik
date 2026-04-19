package metier;

import java.util.ArrayList;

public abstract class Artiste implements TypeObjets {
    private int num;
    private String nom;
    private float note;
    private int nbEcoutes = 0;
    private String image;
    private ArrayList<Morceau> morceaux = new ArrayList<Morceau>();
    private ArrayList<Album> albums = new ArrayList<Album>();
    private ArrayList<String> genres = new ArrayList<String>();
    private ArrayList<String> zone_geo = new ArrayList<String>();

    public Artiste(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    /**
     * Ajoute un morceau à l'artiste
     */
    public void ajouterMorceau(Morceau morceau) {
        morceaux.add(morceau);
        // on met à jour la note moyenne de l'artiste
        float sommeNotes = 0;
        for (Morceau m : morceaux) {
            sommeNotes += m.getNoteMoy();
        }
        this.note = sommeNotes / morceaux.size();
    }

    /**
     * Vérifie si un morceau est déjà dans la liste de l'artiste
     */
    public boolean contientMorceau(Morceau morceau) {
        for (Morceau m : morceaux) {
            if (m.getNum() == morceau.getNum()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ajoute à l'artiste un album et les morceaux dedans
     */
    public void ajouterAlbum(Album album) {
        albums.add(album);
        for (Morceau morceau : album.getMorceaux()) {
            // On vérifie que le morceau n'est pas déjà dans la liste de l'artiste
            if (!this.contientMorceau(morceau)) {
                morceaux.add(morceau);
            }
        }
    }

    public int getAnnee() {
        return 0;
    }

    public ArrayList<String> getZone_geo() {
        return zone_geo;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public ArrayList<Morceau> getMorceaux() {
        return morceaux;
    }

    public int getNum() {
        return num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getNoteMoy() {
        return note;
    }

    public int getNbEcoutes() {
        return nbEcoutes;
    }

    public void ajouterEcoute() {
        nbEcoutes++;
    }
}
