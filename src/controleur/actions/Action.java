package controleur.actions;

import java.io.Serializable;

import metier.*;
import vue.Console;

public interface Action extends Serializable {
    /**
     * Permet d'exécuter une action choisie par l'utilisateur
     */
    public void executer(Console cons, Personne utilisateur);

    public void executer(Console cons, Personne utilisateur, Catalogue catalogue);

    public String getNom();
}