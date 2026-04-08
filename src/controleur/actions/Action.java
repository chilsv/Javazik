package controleur.actions;

import metier.Personne;
import vue.Console;

public interface Action {
    public void executer(Console cons, Personne utilisateur);

    public String getNom();
}