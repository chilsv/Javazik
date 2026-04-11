package controleur.actions;

import static controleur.Main.sauvegarder;

public class Quitter implements Action {
    /**
     * @param arguments vue, utilisateur, catalogue
     */
    @Override
    public void executer(ActionArguments arguments) {
        arguments.vue.afficherMessage("A bientôt sur Javazic !");
        sauvegarder(arguments.catalogue.getMorceaux(), "morceaux.ser");
        sauvegarder(arguments.catalogue.getPlaylists(), "playlists.ser");
        sauvegarder(arguments.catalogue.getArtistes(), "artistes.ser");
        sauvegarder(arguments.catalogue.getAlbums(), "albums.ser");
        System.exit(0);
    }

    @Override
    public String getNom() {
        return "Quitter l'application";
    }
}
