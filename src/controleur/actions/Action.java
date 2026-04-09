package controleur.actions;

import java.io.Serializable;

import metier.*;
import vue.InterfaceVue;

public interface Action extends Serializable {
    /**
     * Permet d'exécuter une action choisie par l'utilisateur
     */
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue);

    public String getNom();
}