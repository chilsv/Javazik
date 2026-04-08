package controleur.actions;

import java.util.ArrayList;

import metier.Personne;
import metier.Morceau;
import metier.Artiste;
import vue.Console;

public class Quitter implements Action {
    @Override
    public void executer(Console cons, Personne utilisateur) {
        System.exit(0);
    }

    @Override
    public void executer(Console cons, Personne utilisateur, ArrayList<Morceau> morceaux, ArrayList<Artiste> artistes) {
        System.exit(0);
    }

    @Override
    public String getNom() {
        return "Quitter l'application";
    }
}
