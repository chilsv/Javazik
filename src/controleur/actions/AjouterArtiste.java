package controleur.actions;

import controleur.Main;
import metier.Artiste;
import metier.Catalogue;
import metier.Morceau;
import metier.Personne;
import vue.Console;

public class AjouterArtiste implements Action {
    @Override
    public void executer(Console cons, Personne utilisateur, Catalogue catalogue) {
        Artiste artiste = cons.ajouterArtiste();
        catalogue.ajouterArtiste(artiste);
        Main.sauvegarder(catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(catalogue.getAlbums(), "albums.ser");
    }

    @Override
    public String getNom() {
        return "Ajouter un artiste dans le catalogue";
    }
}
