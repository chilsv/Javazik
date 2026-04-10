package controleur.actions;

import controleur.formulaires.PlaylistForm;
import metier.Abonne;
import metier.Catalogue;
import metier.Playlist;

public class AjouterPlaylist implements Action {
    /**
     * @param arguments vue, utilisateur, catalogue
     */
    @Override
    public void executer(ActionArguments arguments) {
        PlaylistForm formulaire = arguments.vue.demanderPlaylist();
        Playlist playlist = new Playlist(formulaire.nom, formulaire.morceaux, arguments.catalogue);
        if (!arguments.catalogue.playlistExiste(playlist.getNum())) {
            if (arguments.utilisateur instanceof Abonne) {
                Abonne abonne = (Abonne) arguments.utilisateur;
                abonne.ajouterPlaylist(playlist.getNum());
            }
            arguments.catalogue.ajouterPlaylist(playlist);
        }
    }

    public void executerDefaut(Abonne abonne, Catalogue catalogue, Playlist playlist) {
        abonne.ajouterPlaylist(playlist.getNum());
        catalogue.ajouterPlaylist(playlist);
    }

    @Override
    public String getNom() {
        return "Ajouter une playlist";
    }

}
