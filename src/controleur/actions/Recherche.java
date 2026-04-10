package controleur.actions;

import controleur.formulaires.RechercheForm;
import metier.*;
import vue.InterfaceVue;

public class Recherche implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        // Pour chaque dype d'utilisateur, on a des possibilités de recherche différentes
        RechercheForm formulaire = vue.demanderRecherche(utilisateur instanceof Abonne);
        Filtre filtre = new Filtre(
                formulaire.recherche,
                formulaire.morceau,
                formulaire.artiste,
                formulaire.album,
                formulaire.playlist,
                formulaire.croissant,
                formulaire.annee);

        if (utilisateur instanceof Visiteur) {
            ResultatRecherche resultat = catalogue.chercher(filtre);
            vue.afficherRecherche(resultat);

        } else if (utilisateur instanceof Abonne) {
            ResultatRecherche resultat = catalogue.chercher(filtre);
            vue.afficherRecherche(resultat);
            
        } else if (utilisateur instanceof Admin) {
            // à implémeneter : peut chercher des abonnés et tout
        }
    }
    
    @Override
    public String getNom() {
        return "Effectuer une recherche";
    }
}
