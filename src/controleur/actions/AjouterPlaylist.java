package controleur.actions;

import controleur.formulaires.PlaylistForm;
import metier.Abonne;
import metier.Catalogue;
import metier.Personne;
import metier.Playlist;
import vue.InterfaceVue;

public class AjouterPlaylist implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        PlaylistForm formulaire = vue.demanderPlaylist();
        Playlist playlist = new Playlist(formulaire.nom, formulaire.morceaux, catalogue);
        if (!catalogue.playlistExiste(playlist.getNum())) {
            if (utilisateur instanceof Abonne) {
                Abonne abonne = (Abonne) utilisateur;
                abonne.ajouterPlaylist(playlist.getNum());
            }
            catalogue.ajouterPlaylist(playlist);
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
