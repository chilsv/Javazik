package controleur.actions;

import java.io.Serializable;

import metier.Personne;
import vue.Console;

public interface Action extends Serializable {
    /**
     * Permet d'exécuter une action choisie par l'utilisateur
     */
    public void executer(Console cons, Personne utilisateur);

    public String getNom();
}