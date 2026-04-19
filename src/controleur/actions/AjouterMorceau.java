package controleur.actions;

import controleur.Main;
import metier.Morceau;

public class AjouterMorceau implements Action {
    /**
     * @param arguments catalogue, morceauform
     */
    @Override
    public void executer(ActionArguments arguments) {
        Morceau morceau = new Morceau(arguments.morceauForm.titre, new metier.Solo(arguments.morceauForm.artiste), arguments.morceauForm.duree);
        if (arguments.morceauForm.album != null && !arguments.morceauForm.album.isBlank()) {
            morceau.setAlbum(arguments.morceauForm.album);
        }
        arguments.catalogue.ajouterMorceau(morceau);
        Main.sauvegarder(arguments.catalogue.getMorceaux(), "morceaux.ser");
        Main.sauvegarder(arguments.catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(arguments.catalogue.getAlbums(), "albums.ser");
    }

    @Override
    public String getNom() {
        return "Ajouter un morceau dans le catalogue";
    }
}





