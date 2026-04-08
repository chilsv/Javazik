package metier;

import java.util.ArrayList;

import controleur.actions.Action;
import controleur.actions.Recherche;
import vue.Console;

public class Admin extends Personne {
    private int num;
    private final ArrayList<Action> actions = new ArrayList<Action>();

    public Admin(String nom, String mail, String mdp, int num) {
        super(nom, mail, mdp);
        this.num = num;
        // Actions qu'un admin peut faire
        actions.add(new Recherche());
    }

    public String getAccueil(Console cons) {
        return new String("Bienvenue sur la page de gestion, " + getNom() + " !");
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}
