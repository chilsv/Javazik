package controleur.actions;

import controleur.Main;
import controleur.formulaires.MorceauForm;
import metier.Morceau;

public class AjouterMorceau implements Action {
    /**
     * @param arguments vue, catalogue
     */
    @Override
    public void executer(ActionArguments arguments) {
        MorceauForm formulaire = arguments.vue.demanderMorceau();
        Morceau morceau = new Morceau(formulaire.titre, new metier.Solo(formulaire.artiste), formulaire.duree);
        if (formulaire.album != null && !formulaire.album.isBlank()) {
            morceau.setAlbum(formulaire.album);
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
