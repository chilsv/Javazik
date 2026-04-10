package controleur.exceptions;

public class UtilisateurDejaCreeException extends ActionException {
    public UtilisateurDejaCreeException() {
        super("Un utilisateur utilise déjà ce mail.");
    }

}
