package controleur.actions;

import metier.Abonne;
import metier.Admin;

public class ConsulterProfil implements Action {
    /**
     * @param arguments vue, utilisateur, catalogue
     */
    @Override
    public void executer(ActionArguments arguments) {
        if (arguments.utilisateur instanceof Abonne) {
            Abonne abonne = (Abonne) arguments.utilisateur;
            arguments.vue.afficherProfilAbonne(abonne, arguments.catalogue);
        } else if (arguments.utilisateur instanceof Admin) {
            Admin admin = (Admin) arguments.utilisateur;
            arguments.vue.afficherProfilAdmin(admin);
        }
    }

    @Override
    public String getNom() {
        return "Consulter le profil";
    }

}
