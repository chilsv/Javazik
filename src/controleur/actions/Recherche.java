package controleur.actions;

import java.util.ArrayList;
import java.util.Scanner;

import metier.*;
import vue.Console;

public class Recherche implements Action {
    public void executer(Console cons, Personne utilisateur) {
    }

    @Override
    public void executer(Console cons, Personne utilisateur, ArrayList<Morceau> morceaux, ArrayList<Artiste> artistes) {
        if (utilisateur instanceof Visiteur) {
            System.out.print("Recherche de : ");
            Scanner saisie = new Scanner(System.in);
            String recherche = saisie.nextLine();

            ArrayList<Morceau> trouvesMorceaux = new ArrayList<Morceau>();
            for (Morceau morceau : morceaux) {
                if (morceau.getNom().toLowerCase().contains(recherche.toLowerCase())) {
                    trouvesMorceaux.add(morceau);
                }
            }
            System.out.println("-".repeat(40));
            System.out.println("Morceaux :");
            for (Morceau morceau : trouvesMorceaux) {
                System.out.println("- " + morceau.getNom());
            }

            ArrayList<Artiste> trouvesArtistes = new ArrayList<Artiste>();
            for (Artiste artiste : artistes) {
                if (artiste.getNom().toLowerCase().contains(recherche.toLowerCase())) {
                    trouvesArtistes.add(artiste);
                }
            }
            System.out.println("-".repeat(40));
            System.out.println("Artistes :");
            for (Artiste artiste : trouvesArtistes) {
                System.out.println("- " + artiste.getNom());
            }
            
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
