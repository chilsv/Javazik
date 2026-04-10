package vue;
import controleur.actions.Action;
import metier.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Fenetre implements InterfaceVue {



    public int menuPrincipal(){//ce que j'avais fait

        FenetreMenu fenetreMenu = new FenetreMenu();

        int choix;


        return choix;
    };


    //barre de lecture
    public void afficherLecture(Morceau morceau){};


    public Action choisirAction(String accueil, ArrayList<Action> actions){return null;}; //quelle bouton on clique
    public void afficherMessage(String message){}; //erreur
    public void afficherErreur(String message){}; //message d'erreur
    public void afficherProfilAbonne(Abonne abonne, Catalogue catalogue){};
    public void afficherProfilAdmin(Admin admin){};

    public ConnexionForm demanderConnexion(){return null;};
    public InscriptionForm demanderInscription(){return null;};
    public RechercheForm demanderRecherche(boolean filtrage){return null;};

    public void afficherRecherche(ResultatRecherche resultat){}; //reuslata rehcerche
    public MorceauForm demanderMorceau(){return null;}; //admin ajouter
    public ArtisteForm demanderArtiste(){return null;}; //admin
    public PlaylistForm demanderPlaylist(){return null;}; //abonéé


    public String choisirMorceau(){
        return "0";
    }



}