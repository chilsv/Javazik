package metier;

import java.util.ArrayList;

import controleur.actions.Action;
import controleur.actions.Quitter;
import controleur.actions.Recherche;
import vue.Console;

public class Visiteur extends Personne {
    // Liste des actions qu'un visiteur peut faire
    private final ArrayList<Action> actions = new ArrayList<Action>();

    public Visiteur() {
        super();
        // Actions qu'un visiteur peut faire
        actions.add(new Recherche());
        actions.add(new Quitter());
    }

    public String getAccueil(Console cons) {
        return "Bienvenue sur la page d'accueil !";
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

}
