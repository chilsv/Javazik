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
        // Pour chaque dype d'utilisateur, on a des possibilités de recherche différentes
        if (utilisateur instanceof Visiteur) {
            // On définit le filtre
            Filtre filtre = cons.recherche(false);
            // On fait la recherche avec le filtre
            ResultatRecherche resultat = catalogue.chercher(filtre);
            // On affiche le résultat de la recherche
            cons.afficherRecherche(resultat);

        } else if (utilisateur instanceof Abonne) {
            // On définit le filtre
            Filtre filtre = cons.recherche(true);
            // On fait la recherche avec le filtre
            ResultatRecherche resultat = catalogue.chercher(filtre);
            // On affiche le résultat de la recherche
            cons.afficherRecherche(resultat);
            
        } else if (utilisateur instanceof Admin) {
            System.out.println("Recherche en cours...");
        }
    }
    
    @Override
    public String getNom() {
        return "Effectuer une recherche";
    }
}
