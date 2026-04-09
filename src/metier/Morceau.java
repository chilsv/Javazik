package metier;

import java.util.ArrayList;

public class Morceau implements TypeObjets {
    private int num;
    private String Titre;
    private float duree;
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

    public Morceau(String titre, Artiste artiste, float duree) {
        this.Titre = titre;
        this.artistes = new ArrayList<>();
        this.artistes.add(artiste);
        this.duree = duree;
    }

    public Morceau(String titre, ArrayList<Artiste> artistes, float duree) {
        this.Titre = titre;
        this.artistes = new ArrayList<>();
        this.artistes.addAll(artistes);
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
