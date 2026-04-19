package controleur.actions;

import metier.Artiste;

public class SupprimerArtiste implements Action {
    /*
     * @param arguments catalogue, nom
     */
    @Override
    public void executer(ActionArguments arguments) {
        for (Artiste artiste : arguments.catalogue.getArtistes()) {
            if (artiste.getNom().equalsIgnoreCase(arguments.texte)) {
                arguments.catalogue.getArtistes().remove(artiste);
                System.out.println(artiste.getNom() + " supprimé avec succès");
                return;
            }
        }
    }

    @Override
    public String getNom() {
        return "Supprimer un artiste";
    }

}
