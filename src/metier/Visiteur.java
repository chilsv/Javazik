package metier;

import java.util.ArrayList;

import controleur.actions.*;
import vue.InterfaceVue;

public class Visiteur extends Personne {
    // Liste des actions qu'un visiteur peut faire
    private final ArrayList<Action> actions = new ArrayList<Action>();

    public Visiteur() {
        super();
        // Actions qu'un visiteur peut faire
        actions.add(new Recherche());
        actions.add(new JouerMorceau());
        actions.add(new Deconnexion());
        actions.add(new Quitter());
    }

    public String getAccueil(InterfaceVue vue) {
        return "Bienvenue sur la page d'accueil !";
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

}
