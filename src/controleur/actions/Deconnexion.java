package controleur.actions;

import metier.Catalogue;
import metier.Personne;
import vue.Console;

public class Deconnexion implements Action {
    @Override
    public void executer(Console cons, Personne utilisateur, Catalogue catalogue) {
        System.out.println("Déconnexion...");
    }

    @Override
    public String getNom() {
        return "Retourner au menu principal";
    }
}