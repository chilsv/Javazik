package controleur.actions;

import java.util.ArrayList;
import java.util.Scanner;

import metier.*;
import vue.Console;

public class Recherche implements Action {
    public void executer(Console cons, Personne utilisateur) {
    }

    @Override
    public void executer(Console cons, Personne utilisateur, Catalogue catalogue) {
        if (utilisateur instanceof Visiteur) {
            Filtre filtre = cons.recherche();
            ResultatRecherche resultat = catalogue.chercher(filtre);
            cons.afficherRecherche(resultat);

        } else if (utilisateur instanceof Abonne) {
            
        } else if (utilisateur instanceof Admin) {
            System.out.println("Recherche en cours...");
        }
    }
    
    @Override
    public String getNom() {
        return "Effectuer une recherche";
    }
}
