package controleur.exceptions;

public class PlaylistDejaExistanteException extends ActionException {
    public PlaylistDejaExistanteException() {
        super("Une playlist avec ce nom existe déjà");
    }

}
