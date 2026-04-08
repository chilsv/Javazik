package controleur.actions;

import metier.Visiteur;
import metier.Abonne;
import metier.Admin;
import metier.Personne;
import vue.Console;

public class Recherche implements Action {
    @Override
    public void executer(Console cons, Personne utilisateur) {
        if (utilisateur instanceof Visiteur) {
            System.out.println("Recherche en cours...");
        } else if (utilisateur instanceof Abonne) {
            System.out.println("Recherche en cours...");
        } else if (utilisateur instanceof Admin) {
            System.out.println("Recherche en cours...");
        }
    }
    
    @Override
    public String getNom() {
        return "Effectuer une recherche";
    }
}
