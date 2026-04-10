package controleur.actions;

import java.util.ArrayList;

import controleur.MdpIncorrectException;
import controleur.UtilisateurIntrouvableException;
import metier.Abonne;
import metier.Admin;
import metier.Catalogue;
import metier.Personne;
import vue.InterfaceVue;

public class Connexion implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
    }

    public void executer(InterfaceVue vue, ArrayList<Abonne> abonnes, ArrayList<Admin> admins) throws UtilisateurIntrouvableException, MdpIncorrectException {
    }

    @Override
    public String getNom() {
        return "Se connecter";
    }

}
