package metier;

public class Morceau implements TypeObjets {
    private int num;
    private String Titre;
    private float duree;
    private Artiste artiste;
    private int annee;

    public Morceau(String titre, Artiste artiste) {
        this.Titre = titre;
        this.artiste = artiste;
    }

    public Morceau(String titre, Artiste artiste, float duree) {
        this.Titre = titre;
        this.artiste = artiste;
        this.duree = duree;
    }

    public int getNum() {
        return num;
    }

    public float getDuree() {
        return duree;
    }

    public String getNom() {
        return Titre;
    }

    public int getAnnee() {
        return annee;
    }
}
