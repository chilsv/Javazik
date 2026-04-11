package controleur.actions;

import controleur.Main;
import metier.Solo;
import metier.Groupe;

public class AjouterArtiste implements Action {
    /**
     * @param arguments catalogue, artisteform
     */
    @Override
    public void executer(ActionArguments arguments) {
        if (arguments.artisteForm.type.equals("solo")) {
            Solo artiste = new Solo(arguments.artisteForm.nom);
            arguments.catalogue.ajouterArtiste(artiste);
        } else {
            Groupe artiste = new Groupe(arguments.artisteForm.nom);
            arguments.catalogue.ajouterArtiste(artiste);
        }
        Main.sauvegarder(arguments.catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(arguments.catalogue.getAlbums(), "albums.ser");
    }

    @Override
    public String getNom() {
        return "Ajouter un artiste dans le catalogue";
    }
}
