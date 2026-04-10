package controleur.actions;

import controleur.exceptions.MorceauIntrouvableException;
import metier.Filtre;
import metier.Morceau;
import metier.ResultatRecherche;

public class JouerMorceau implements Action {
    /**
     * @param arguments vue, catalogue
     */
    @Override
    public void executer(ActionArguments arguments) throws MorceauIntrouvableException {
        // on obtient le morceau à joueur
        String nomMorceau = arguments.vue.choisirMorceau();

        // on cherche le morceau dans le catalogue
        Morceau morceauTrouve = null;
        ResultatRecherche resultat = arguments.catalogue.chercher(new Filtre(nomMorceau, true, false, false, false, false, 0));
        
        for (Morceau morceau : resultat.morceaux) {
            if (morceau.getNom().equalsIgnoreCase(nomMorceau)) {
                morceauTrouve = morceau;
                break;
            }
        }

        if (morceauTrouve == null) {
            throw new MorceauIntrouvableException();
        }

        // oN affiche la lecture du morceau
        arguments.vue.afficherLecture(morceauTrouve);
    }

    @Override
    public String getNom() {
        return "Jouer un morceau";
    }

}
