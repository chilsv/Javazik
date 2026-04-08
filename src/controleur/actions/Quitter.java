package controleur.actions;

import metier.Personne;
import vue.Console;

public class Quitter implements Action {
    @Override
    public void executer(Console cons, Personne utilisateur) {
        System.exit(0);
    }
    
    @Override
    public String getNom() {
        return "Quitter l'application";
    }
}
