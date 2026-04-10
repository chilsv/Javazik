package controleur.actions;

import metier.Personne;
import metier.Catalogue;
import vue.InterfaceVue;
import static controleur.Main.sauvegarder;

public class Quitter implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        vue.afficherMessage("A bientôt sur Javazic !");
        sauvegarder(catalogue.getMorceaux(), "morceaux.ser");
        sauvegarder(catalogue.getPlaylists(), "playlists.ser");
        sauvegarder(catalogue.getArtistes(), "artistes.ser");
        sauvegarder(catalogue.getAlbums(), "albums.ser");
        System.exit(0);
    }

    @Override
    public String getNom() {
        return "Quitter l'application";
    }
}
