package controleur.actions;

import controleur.Main;
import metier.Catalogue;
import metier.Morceau;
import metier.Personne;
import vue.InterfaceVue;
import vue.MorceauForm;

public class AjouterMorceau implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        MorceauForm formulaire = vue.demanderMorceau();
        Morceau morceau = new Morceau(formulaire.titre, new metier.Solo(formulaire.artiste), formulaire.duree);
        if (formulaire.album != null && !formulaire.album.isBlank()) {
            morceau.setAlbum(formulaire.album);
        }
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
