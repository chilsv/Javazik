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
            System.out.print("Recherche de : ");
            Scanner saisie = new Scanner(System.in);
            String recherche = saisie.nextLine();

            ArrayList<Morceau> trouvesMorceaux = new ArrayList<Morceau>();
            ArrayList<Artiste> trouvesArtistes = new ArrayList<Artiste>();
            
            catalogue.chercherMorceaux(recherche, trouvesMorceaux);
            cons.rechercheMorceaux(trouvesMorceaux);

            catalogue.chercherArtistes(recherche, trouvesArtistes);
            cons.rechercheArtistes(trouvesArtistes);

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
