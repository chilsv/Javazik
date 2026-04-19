package controleur.actions;

import controleur.Main;
import controleur.formulaires.ArtisteForm;
import metier.Album;
import metier.Artiste;

public class AjouterAlbum implements Action {
    /*
    * @param arguments catalogue, AlbumForm
    */
    @Override
    public void executer(ActionArguments arguments) {
        ArtisteForm form = new ArtisteForm(arguments.albumForm.artiste, "solo");

        ActionArguments arg = new ActionArguments(arguments.catalogue, form, null);
        new AjouterArtiste().executer(arg);
        Artiste artiste = arg.artistes;

        Album album = new Album(arguments.albumForm.titre, artiste, arguments.albumForm.annee, arguments.albumForm.morceaux);
        artiste.ajouterAlbum(album);
        Main.sauvegarder(arguments.catalogue.getAlbums(), "albums.ser");
    }

    @Override
    public String getNom() {
        return "Ajouter un album";
    }
}
