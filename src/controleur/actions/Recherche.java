package controleur.actions;

import controleur.formulaires.RechercheForm;
import metier.Abonne;
import metier.Admin;
import metier.Filtre;
import metier.ResultatRecherche;
import metier.Visiteur;

public class Recherche implements Action {
    /**
     * @param arguments vue, utilisateur, catalogue
     */
    @Override
    public void executer(ActionArguments arguments) {
        // Pour chaque dype d'utilisateur, on a des possibilités de recherche différentes
        RechercheForm formulaire = arguments.vue.demanderRecherche(arguments.utilisateur instanceof Abonne);
        Filtre filtre = new Filtre(formulaire.recherche, formulaire.morceau, formulaire.artiste, formulaire.album, formulaire.playlist, formulaire.croissant, formulaire.annee);

        if (arguments.utilisateur instanceof Visiteur) {
            ResultatRecherche resultat = arguments.catalogue.chercher(filtre);
            arguments.vue.afficherRecherche(resultat);
        } else if (arguments.utilisateur instanceof Abonne) {
            ResultatRecherche resultat = arguments.catalogue.chercher(filtre);
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
