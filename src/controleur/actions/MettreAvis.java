package controleur.actions;

import static controleur.Main.ajouterAvis;

import metier.Abonne;
import metier.Avis;

public class MettreAvis implements Action {
    /*
    * @param arguments 
    */
    @Override
    public void executer(ActionArguments arguments) {
        Abonne abonne = (Abonne) arguments.utilisateur;
        Avis nouvelAvis = new Avis(arguments.morceau, abonne, arguments.note, arguments.commentaire);
        arguments.morceau.ajouterAvis(nouvelAvis);
        abonne.ajouterAvis(nouvelAvis);
        ajouterAvis(nouvelAvis);
    }

    @Override
    public String getNom() {
        return "Mettre un avis sur un morceau";
    }

}
