package controleur.actions;

import java.util.Scanner;

import metier.Catalogue;
import metier.Personne;
import vue.Console;

public class JouerMorceau implements Action {
    @Override
    public void executer(Console cons, Personne utilisateur, Catalogue catalogue) {
        System.out.println("Lecture du morceau...");
    }

    @Override
    public String getNom() {
        return "Jouer un morceau";
    }

}
