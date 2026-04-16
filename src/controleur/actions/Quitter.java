package controleur.actions;

import static controleur.Main.getAvis;
import static controleur.Main.sauvegarder;
import static controleur.Main.sauvegarderAvis;

import java.util.ArrayList;

import metier.Avis;

public class Quitter implements Action {
    /**
     * @param arguments vue, utilisateur, catalogue, avis
     */
    @Override
    public void executer(ActionArguments arguments) {
        arguments.vue.afficherMessage("A bientôt sur Javazic !");
        sauvegarder(arguments.catalogue.getMorceaux(), "morceaux.ser");
        sauvegarder(arguments.catalogue.getPlaylists(), "playlists.ser");
        sauvegarder(arguments.catalogue.getArtistes(), "artistes.ser");
        sauvegarder(arguments.catalogue.getAlbums(), "albums.ser");
        ArrayList<Avis> avisASauvegarder = arguments.avis != null ? arguments.avis : getAvis();
        sauvegarderAvis(avisASauvegarder);
        System.exit(0);
    }

    @Override
    public String getNom() {
        return "Quitter l'application";
    }
}
