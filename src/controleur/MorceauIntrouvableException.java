package controleur;

public class MorceauIntrouvableException extends RuntimeException {
    public MorceauIntrouvableException() {
        super("Morceau introuvable dans le catalogue.");
    }
}