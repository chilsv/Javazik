package controleur.actions;

import metier.Abonne;
import metier.Catalogue;
import metier.Morceau;
import metier.Personne;
import metier.Playlist;
import vue.InterfaceVue;

public class Aimer implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
    }

    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue, Morceau morceau) {
        if (utilisateur instanceof Abonne) {
            Abonne abonne = (Abonne) utilisateur;
            //vue.afficherAimer(morceau.getNom());
            catalogue.ajouterMorceauPlaylist(morceau, abonne.getAimes());
        }
    }

    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue, Playlist playlist) {
        if (utilisateur instanceof Abonne) {
            Abonne abonne = (Abonne) utilisateur;
            //vue.afficherAimer(playlist.getNom());
            abonne.ajouterPlaylist(playlist.getNum());
        }
    }

    @Override
    public String getNom() {
        return "Aimer un morceau ou une playlist";
    }

}
