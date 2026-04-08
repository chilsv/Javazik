package metier;

import java.util.ArrayList;

import controleur.actions.Action;
import controleur.actions.Recherche;
import vue.Console;

public class Visiteur extends Personne {
    private final ArrayList<Action> actions = new ArrayList<Action>();

    public Visiteur() {
        super();
        // Actions qu'un visiteur peut faire
        actions.add(new Recherche());
    }

    public String getAccueil(Console cons) {
        return new String("Bienvenue sur la page d'accueil !");
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

}
