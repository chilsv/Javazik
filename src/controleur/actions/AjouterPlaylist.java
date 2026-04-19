package controleur.actions;

import controleur.exceptions.PlaylistDejaExistanteException;
import metier.Abonne;
import metier.Playlist;

public class AjouterPlaylist implements Action {
    /**
     * @param arguments utilisateur, catalogue, playlistForm
     */
    @Override
    public void executer(ActionArguments arguments) throws PlaylistDejaExistanteException {   
        Playlist playlist = new Playlist(arguments.playlistForm.nom, arguments.playlistForm.morceaux, arguments.catalogue, arguments.playlistForm.numCreateur);
        if (!arguments.catalogue.playlistExiste(playlist.getNum())) {
            if (arguments.utilisateur instanceof Abonne) {
                Abonne abonne = (Abonne) arguments.utilisateur;
                if (playlist.getNom().equals("Morceaux aimés")) {
                    abonne.setPlaylistDefaut(playlist.getNum());
                }
                abonne.ajouterPlaylist(playlist.getNum());
            }
            try {
                arguments.catalogue.ajouterPlaylist(playlist);
            } catch (PlaylistDejaExistanteException e) {
                throw new PlaylistDejaExistanteException();
            }
        }
    }

    @Override
    public String getNom() {
        return "Ajouter une playlist";
    }

}
