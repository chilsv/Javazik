package vue;

import java.util.ArrayList;

import controleur.actions.Action;
import metier.Abonne;
import metier.Admin;
import metier.Artiste;
import metier.Catalogue;
import metier.Morceau;
import metier.ResultatRecherche;

public interface InterfaceVue {
    String choisirMorceau();
    void afficherLecture(Morceau morceau);
    
    int menuPrincipal();
    Action choisirAction(String accueil, ArrayList<Action> actions);
    void afficherMessage(String message);
    void afficherErreur(String message);
    void afficherProfilAbonne(Abonne abonne, Catalogue catalogue);
    void afficherProfilAdmin(Admin admin);

    ConnexionForm demanderConnexion();
    InscriptionForm demanderInscription();
    RechercheForm demanderRecherche(boolean filtrage);
    void afficherRecherche(ResultatRecherche resultat);

    MorceauForm demanderMorceau();
    ArtisteForm demanderArtiste();
}