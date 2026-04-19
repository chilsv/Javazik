package controleur.actions;

import metier.Morceau;

public class SupprimerMorceau implements Action {
    /*
     * @param arguments : catalogue, morceauForm
     */
    @Override
    public void executer(ActionArguments arguments) {
        for (Morceau morceau : arguments.catalogue.getMorceaux()) {
            if (morceau.getNom().equals(arguments.morceauForm.titre)) {
                /*for (Artiste artiste : arguments.morceauForm.artistes) {
                    if (!morceau.getArtistes().contains(artiste)) {
                        return;
                    }
                }*/
                if (morceau.getArtistes().get(0).getNom().equals(arguments.morceauForm.artiste)) {
                    arguments.catalogue.retirerMorceau(morceau);
                    return;
                }
            }
        }
    }

    @Override
    public String getNom() {
        return "Supprimer un morceau";
    }

}
