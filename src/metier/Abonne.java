package metier;

import java.util.ArrayList;

import controleur.actions.*;
import vue.Console;

public class Abonne extends Personne {
    private int num;
    private final ArrayList<Action> actions = new ArrayList<Action>();

    public Abonne(String nom, String mail, String mdp, int num) {
        super(nom, mail, mdp);
        this.num = num;
        // Actions qu'un abonné peut faire
        actions.add(new Recherche());
    }

    public String getAccueil(Console cons) {
        return new String("Bienvenue sur la page d'accueil, " + getNom() + " !");
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

}
