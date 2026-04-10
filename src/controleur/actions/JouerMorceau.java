package controleur.actions;

import controleur.MorceauIntrouvableException;
import metier.Catalogue;
import metier.Filtre;
import metier.Morceau;
import metier.Personne;
import metier.ResultatRecherche;
import vue.InterfaceVue;

public class JouerMorceau implements Action {
    @Override
    public void executer(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) throws MorceauIntrouvableException {
        // on obtient le morceau à joueur
        String nomMorceau = vue.choisirMorceau();

        // on cherche le morceau dans le catalogue
        Morceau morceauTrouve = null;
        ResultatRecherche resultat = catalogue.chercher(new Filtre(nomMorceau, true, false, false, false, false, 0));
        
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
        vue.afficherLecture(morceauTrouve);
    }

    @Override
    public String getNom() {
        return "Jouer un morceau";
    }

}
