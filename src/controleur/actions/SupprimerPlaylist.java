package controleur.actions;

public class SupprimerPlaylist implements Action {
    /**
     * @param arguments catalogue, numPlaylist
     */
    @Override
    public void executer(ActionArguments arguments) {
        arguments.catalogue.retirerPlaylist(arguments.playlist.getNum());
    }

    @Override
    public String getNom() {
        return "Supprimer une playlist";
    }
}
