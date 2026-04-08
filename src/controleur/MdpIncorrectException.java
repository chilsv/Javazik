package controleur;

public class MdpIncorrectException extends Exception{
    public MdpIncorrectException() {
        super("Mot de passe incorrect.");
    }
}
