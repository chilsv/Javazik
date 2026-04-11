package controleur.actions;

import metier.Abonne;
import metier.Admin;
import metier.Filtre;
import metier.ResultatRecherche;
import metier.Visiteur;

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
        if (arguments.utilisateur instanceof Visiteur || arguments.utilisateur instanceof Abonne) {
            ResultatRecherche resultat = arguments.catalogue.chercher(filtre, arguments.rechercheForm.recherche);
            arguments.vue.afficherRecherche(resultat);
        } else if (arguments.utilisateur instanceof Admin) {
            // à implémeneter : peut chercher des abonnés et tout
        }
    }
    
    @Override
    public String getNom() {
        return "Effectuer une recherche";
    }
}
