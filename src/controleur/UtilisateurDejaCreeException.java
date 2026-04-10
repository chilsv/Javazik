package controleur;

public class UtilisateurDejaCreeException extends Exception {
    public UtilisateurDejaCreeException() {
        super("Un utilisateur utilise déjà ce mail.");
    }

}
