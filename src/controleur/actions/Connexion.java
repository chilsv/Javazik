package controleur.actions;

import controleur.exceptions.MdpIncorrectException;
import controleur.exceptions.UtilisateurIntrouvableException;

public class Connexion implements Action {
    /**
     * @param arguments vue, abonnes, admins
     */
    @Override
    public void executer(ActionArguments arguments) throws UtilisateurIntrouvableException, MdpIncorrectException {
    }

    @Override
    public String getNom() {
        return "Se connecter";
    }
}
