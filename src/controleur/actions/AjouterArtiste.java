package controleur.actions;

import controleur.Main;
import controleur.formulaires.ArtisteForm;
import metier.Artiste;

public class AjouterArtiste implements Action {
    /**
     * @param arguments vue, catalogue
     */
    @Override
    public void executer(ActionArguments arguments) {
        ArtisteForm formulaire = arguments.vue.demanderArtiste();
        Artiste artiste = new metier.Solo(formulaire.nom);
        arguments.catalogue.ajouterArtiste(artiste);
        Main.sauvegarder(arguments.catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(arguments.catalogue.getAlbums(), "albums.ser");
    }

    @Override
    public String getNom() {
        return "Ajouter un artiste dans le catalogue";
    }
}
