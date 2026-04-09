package controleur.actions;

import metier.Catalogue;
import metier.Personne;
import vue.InterfaceVue;

public class Deconnexion implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        vue.afficherMessage("Déconnexion...");
    }

    @Override
    public String getNom() {
        return "Retourner au menu principal";
    }
}