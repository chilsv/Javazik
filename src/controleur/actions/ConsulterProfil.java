package controleur.actions;

import metier.Abonne;
import metier.Admin;
import metier.Catalogue;
import metier.Personne;
import vue.InterfaceVue;

public class ConsulterProfil implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        if (utilisateur instanceof Abonne) {
            Abonne abonne = (Abonne) utilisateur;
            vue.afficherProfilAbonne(abonne, catalogue);
        } else if (utilisateur instanceof Admin) {
            Admin admin = (Admin) utilisateur;
            vue.afficherProfilAdmin(admin);
        }
    }

    @Override
    public String getNom() {
        return "Consulter le profil";
    }

}
