package controleur.exceptions;

public class MdpIncorrectException extends ActionException {
    public MdpIncorrectException() {
        super("Mot de passe incorrect.");
    }
}
