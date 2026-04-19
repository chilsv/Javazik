package metier;

import java.util.ArrayList;

public class Groupe extends Artiste {
    private ArrayList<Solo> membres;

    public Groupe(String nom) {
        super(nom);
    }

    public Groupe(String nom, ArrayList<Solo> membres) {
        super(nom);
        this.membres = membres;
    }

    public ArrayList<Solo> getMembres() {
        return membres;
    }
}



















































































