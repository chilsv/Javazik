package controleur.actions;

import controleur.exceptions.MorceauIntrouvableException;
import metier.Filtre;
import metier.Morceau;
import metier.ResultatRecherche;

public class JouerMorceau implements Action {
    /**
     * @param arguments catalogue, nomMorceau, morceauTrouve
     */
    @Override
    public void executer(ActionArguments arguments) throws MorceauIntrouvableException {
        // on cherche le morceau dans le catalogue
        ResultatRecherche resultat = arguments.catalogue.chercher(new Filtre(arguments.nomMorceau, true, false, false, false, false, 0));
        
        for (Morceau morceau : resultat.morceaux) {
            if (morceau.getNom().equalsIgnoreCase(arguments.nomMorceau)) {
                arguments.morceauTrouve = morceau;
                break;
            }
        }

        if (arguments.morceauTrouve == null) {
            throw new MorceauIntrouvableException();
        }
    }

    @Override
    public String getNom() {
        return "Jouer un morceau";
    }

}
