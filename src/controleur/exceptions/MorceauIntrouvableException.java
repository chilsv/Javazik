package controleur.exceptions;

public class MorceauIntrouvableException extends ActionException {
    public MorceauIntrouvableException() {
        super("Morceau introuvable dans le catalogue.");
    }
}