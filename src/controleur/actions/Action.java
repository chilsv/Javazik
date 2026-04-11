package controleur.actions;

import java.io.Serializable;

import controleur.exceptions.ActionException;
import controleur.exceptions.MdpIncorrectException;
import controleur.exceptions.UtilisateurIntrouvableException;

public interface Action extends Serializable {
    /**
     * Permet d'exécuter une action choisie par l'utilisateur
     * @throws MdpIncorrectException 
     * @throws UtilisateurIntrouvableException 
     */
    public void executer(ActionArguments arguments) throws ActionException;

    public String getNom();
}