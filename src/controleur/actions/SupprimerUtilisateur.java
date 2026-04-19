package controleur.actions;

import metier.Abonne;

public class SupprimerUtilisateur implements Action {
    /*
    * @param arguments : mail
    */
    @Override
    public void executer(ActionArguments arguments) {
        for (Abonne abonne : arguments.abonnes) {
            if (abonne.getMail().equals(arguments.texte)) {
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
