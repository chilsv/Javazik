package controleur.actions;

import controleur.Main;
import metier.Solo;
import metier.Groupe;

public class AjouterArtiste implements Action {
    /**
     * @param arguments catalogue, artisteform, artistes (juste un pointeur où stocker les artistes mis à jour)
     */
    @Override
    public void executer(ActionArguments arguments) {
        if (arguments.artisteForm.type.equals("solo")) {
            Solo artiste = new Solo(arguments.artisteForm.nom);
            arguments.catalogue.ajouterArtiste(artiste);
            arguments.artistes = artiste;
        } else {
            Groupe artiste = new Groupe(arguments.artisteForm.nom);
            arguments.catalogue.ajouterArtiste(artiste);
            arguments.artistes = artiste;
        }
        Main.sauvegarder(arguments.catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(arguments.catalogue.getAlbums(), "albums.ser");
    }

    @Override
    public String getNom() {
        return "Ajouter un artiste dans le catalogue";
    }
}
