package vue;

import java.util.ArrayList;

import controleur.actions.Action;
import controleur.formulaires.*;
import metier.Abonne;
import metier.Admin;
import metier.Catalogue;
import metier.Morceau;
import metier.Personne;
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
    void afficherUtilisateurs(ArrayList<Abonne> abonnes, ArrayList<Admin> admins);
    void afficherAimer(String nom);

    MorceauForm demanderMorceau();
    ArtisteForm demanderArtiste();
    PlaylistForm demanderPlaylist(int numUtilisateur); // pour créer une playlist
}