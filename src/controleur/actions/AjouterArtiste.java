package controleur.actions;

import controleur.Main;
import metier.Artiste;
import metier.Catalogue;
import metier.Personne;
import vue.InterfaceVue;
import vue.ArtisteForm;

public class AjouterArtiste implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        ArtisteForm formulaire = vue.demanderArtiste();
        Artiste artiste = new metier.Solo(formulaire.nom);
        catalogue.ajouterArtiste(artiste);
        Main.sauvegarder(catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(catalogue.getAlbums(), "albums.ser");
    }

    @Override
    public String getNom() {
        return "Ajouter un artiste dans le catalogue";
    }
}
