package metier;

import java.util.ArrayList;

import controleur.actions.*;
import vue.Console;

public class Admin extends Personne {
    private int num;
    // liste des actions qu'un admin peut faire
    private final ArrayList<Action> actions = new ArrayList<Action>();

    public Admin(String nom, String mail, String mdp, int num) {
        super(nom, mail, mdp);
        this.num = num;
        // Actions qu'un admin peut faire
        actions.add(new Recherche());
        actions.add(new AjouterMorceau());
        actions.add(new AjouterArtiste());
        actions.add(new Deconnexion());
        actions.add(new Quitter());
    }

    public String getAccueil(Console cons) {
        return "Bienvenue sur la page de gestion, " + getNom() + " !";
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}
