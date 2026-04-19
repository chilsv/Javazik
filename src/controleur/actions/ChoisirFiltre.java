package controleur.actions;

import controleur.exceptions.ActionException;
import metier.Visiteur;
public class ChoisirFiltre implements Action {
    @Override
    /**
    * @param arguments vue, utilisateur, filtre
    */
    public void executer(ActionArguments arguments) {
        if (arguments.utilisateur instanceof Visiteur) {
            arguments.vue.afficherErreur(new ActionException("Réservé aux abonnés"));
        } else {
            // En GUI, afficherFiltres peut temporairement ne pas etre implemente.
            // On garde alors le filtre courant au lieu de le perdre.
            if (arguments.filtre == null) {
                arguments.filtre = new metier.Filtre(true, true, true, true, false, new int[] {0, 0});
            }
            metier.Filtre nouveauFiltre = arguments.vue.afficherFiltres();
            if (nouveauFiltre != null) {
                arguments.filtre = nouveauFiltre;
            }
        }
    }

    @Override
    public String getNom() {
        return "Filtrer une recherche";
    }

}
































