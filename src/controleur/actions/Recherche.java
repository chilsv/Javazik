package controleur.actions;

import metier.Filtre;
import metier.ResultatRecherche;

public class Recherche implements Action {
    /**
     * @param arguments vue, utilisateur, catalogue, rechercheForm
     */
    @Override
    public void executer(ActionArguments arguments) {
        if (arguments.rechercheForm == null) {
            return;
        }
        Filtre filtre = new Filtre(arguments.rechercheForm.morceau, arguments.rechercheForm.artiste, arguments.rechercheForm.album, arguments.rechercheForm.playlist, arguments.rechercheForm.croissant, arguments.rechercheForm.annee);
        ResultatRecherche resultat = arguments.catalogue.chercher(filtre, arguments.rechercheForm.recherche);
        arguments.vue.afficherRecherche(resultat);
    }
    
    @Override
    public String getNom() {
        return "Effectuer une recherche";
    }
}
