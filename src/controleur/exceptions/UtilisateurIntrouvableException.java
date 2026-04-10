package controleur.exceptions;

public class UtilisateurIntrouvableException extends ActionException {
    public UtilisateurIntrouvableException() {
        super("Utilisateur introuvable.");
    }
}
