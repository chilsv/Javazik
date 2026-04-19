package metier;

import controleur.actions.Action;
import controleur.actions.ActionArguments;

public class SupprimerUtilisateur implements Action {
    /*
    * @param arguments : mail, catalogue
    */
    @Override
    public void executer(ActionArguments arguments) {
        for (Abonne abonne : arguments.abonnes) {
            if (abonne.getMail().equals(arguments.mail)) {
                arguments.abonnes.remove(abonne);
                return;
            }
        }
    }

    @Override
    public String getNom() {
        return "Supprimer un utilisateur";
    }

}
