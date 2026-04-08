package controleur.actions;

import java.io.Serializable;
import java.util.ArrayList;

import metier.*;
import vue.Console;

public interface Action extends Serializable {
    /**
     * Permet d'exécuter une action choisie par l'utilisateur
     */
    public void executer(Console cons, Personne utilisateur);

    public void executer(Console cons, Personne utilisateur, ArrayList<Morceau> morceaux, ArrayList<Artiste> artistes);

    public String getNom();
}