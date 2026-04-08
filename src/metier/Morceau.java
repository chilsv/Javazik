package metier;

public class Morceau {
    private int num;
    private String nom;
    private float duree;
    private Artiste artiste;

    public Morceau(String nom, Artiste artiste) {
        this.nom = nom;
        this.artiste = artiste;
    }

    public Morceau(String nom, Artiste artiste, float duree) {
        this.nom = nom;
        this.artiste = artiste;
        this.duree = duree;
    }

    public int getNum() {
        return num;
    }

    public float getDuree() {
        return duree;
    }
}
