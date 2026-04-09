package controleur.actions;

import controleur.Main;
import metier.Catalogue;
import metier.Morceau;
import metier.Personne;
import vue.Console;

public class AjouterMorceau implements Action {
    @Override
    public void executer(Console cons, Personne utilisateur, Catalogue catalogue) {
        Morceau morceau = cons.ajouterMorceau();
        catalogue.ajouterMorceau(morceau);
        Main.sauvegarder(catalogue.getMorceaux(), "morceaux.ser");
        Main.sauvegarder(catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(catalogue.getAlbums(), "albums.ser");
    }

    @Override
    public String getNom() {
        return "Ajouter un morceau dans le catalogue";
    }
}
