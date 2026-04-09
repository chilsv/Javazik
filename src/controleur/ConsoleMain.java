package controleur;

import java.util.ArrayList;
import java.util.Scanner;

import controleur.actions.Action;
import metier.*;
import vue.Console;

public class ConsoleMain extends Main {
    private static ArrayList<Abonne> abonnes = new ArrayList<Abonne>();
    private static ArrayList<Admin> admins = new ArrayList<Admin>();
    private static ArrayList<Morceau> morceaux = new ArrayList<Morceau>();
    private static ArrayList<Playlist> playlists = new ArrayList<Playlist>();
    private static ArrayList<Artiste> artistes = new ArrayList<Artiste>();
    private static ArrayList<Album> albums = new ArrayList<Album>();

    public ConsoleMain(ArrayList<Abonne> abonnes, ArrayList<Admin> admins, ArrayList<Morceau> morceaux, ArrayList<Playlist> playlists, ArrayList<Artiste> artistes, ArrayList<Album> albums) {
        ConsoleMain.abonnes = abonnes;
        ConsoleMain.admins = admins;
        ConsoleMain.morceaux = morceaux;
        ConsoleMain.playlists = playlists;
        ConsoleMain.artistes = artistes;
        ConsoleMain.albums = albums;
        menu(abonnes, admins);
    }

    /**
     * Affiche le menu principal et gère les choix de l'utilisateur
     */
    public static void menu(ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {
        Console cons = new Console();
        cons.menu();
        int choix;
        Scanner saisie = new Scanner(System.in);
        choix = saisie.nextInt();
        saisie.nextLine();
        switch (choix) {
            case 1:
                Visiteur visiteur = new Visiteur();
                visiter(visiteur, cons);
                break;
            case 2:
                try {
                    connexion(cons, abonnes, admins);
                } catch (UtilisateurIntrouvableException e) {
                    cons.afficherErreur(e.getMessage());
                    menu(abonnes, admins);
                } catch (MdpIncorrectException e) {
                    cons.afficherErreur(e.getMessage());
                    menu(abonnes, admins);
                }
                break;
            case 3:
                try {
                    inscription(cons, abonnes, admins);
                } catch (UtilisateurDejaCreeException e) {
                    cons.afficherErreur(e.getMessage());
                    menu(abonnes, admins);
                }
                break;
            case 4:
                cons.quitter();
                break;
            default:
                cons.choixInvalide();
                menu(abonnes, admins);
        }
    }

    /**
     * Gère l'inscription d'un visiteur
     */
    public static void inscription(Console cons, ArrayList<Abonne> abonnes, ArrayList<Admin> admins) throws UtilisateurDejaCreeException {
        Personne utilisateur;
        Scanner saisie = new Scanner(System.in);

        cons.inscription();
        String type = saisie.nextLine();
        // On vérifie si le type est valide
        if (!"1".equals(type) && !"2".equals(type)) {
                cons.choixInvalide();
                inscription(cons, abonnes, admins);
                return;
        }

        // Entrée du nom
        cons.nom();
        String nom = saisie.nextLine();

        // Entrée du mail
        cons.mail();
        String mail = saisie.nextLine();

        // On vérifie que le mail n'est pas déjà utilisé par un utilisateur
        for (Abonne abonne : abonnes) {
            if (abonne.getMail().equals(mail)) {
                throw new UtilisateurDejaCreeException();
            }
        }
        for (Admin admin : admins) {
            if (admin.getMail().equals(mail)) {
                throw new UtilisateurDejaCreeException();
            }
        }

        // Entrée du mot de passe
        cons.mdp();
        String mdp = saisie.nextLine();

        // Instanciation du type d'utilisateur
        switch (type) {
            case "1":
                utilisateur = new Abonne(nom, mail, mdp, abonnes.size());
                ajouterAbonne(utilisateur, abonnes);
                break;
            case "2":
                utilisateur = new Admin(nom, mail, mdp, admins.size());
                ajouterAdmin(utilisateur, admins);
                break;
            // Ca n'arrive jamais mais au cas où, on instancie un visiteur
            default:
                utilisateur = new Visiteur();
                break;
        }
        // Entrée dans l'application
        visiter(utilisateur, cons);
    }

    /**
     * Gère la connexion d'un utilisateur
     */
    public static void connexion(Console cons, ArrayList<Abonne> abonnes, ArrayList<Admin> admins) throws UtilisateurIntrouvableException, MdpIncorrectException {
        Scanner saisie = new Scanner(System.in);
        cons.connexion();

        cons.mail();
        String mail = saisie.nextLine();

        cons.mdp();
        String mdp = saisie.nextLine();
        
        // On parcourt les abonnés et les admins pour vérifier si les identifiants sont corrects
        for (Abonne abonne : abonnes) {
            if (abonne.getMail().equals(mail)) {
                if (abonne.getMdp().equals(mdp)) {
                    visiter(abonne, cons);
                    return;
                } else {
                    throw new MdpIncorrectException();
                }
            }
        }
        for (Admin admin : admins) {
            if (admin.getMail().equals(mail)) {
                if (admin.getMdp().equals(mdp)) {
                    visiter(admin, cons);
                    return;
                } else {
                    throw new MdpIncorrectException();
                }
            }
        }

        throw new UtilisateurIntrouvableException();
    }

    /**
     * Permet d'utiliser l'application
     */
    public static void visiter(Personne utilisateur, Console cons) {
        Scanner saisie = new Scanner(System.in);
        // Chaque type d'utilisateur a un menu différent
        String[] actions = utilisateur.getMenu(cons);

        cons.visiter(utilisateur.getAccueil(cons), actions);
        int choix;
        try {
            choix = Integer.parseInt(saisie.nextLine());
        } catch (NumberFormatException e) {
            cons.choixInvalide();
            visiter(utilisateur, cons);
            return;
        }

        if (choix < 1 || choix > actions.length) {
            cons.choixInvalide();
            visiter(utilisateur, cons);
            return;
        }

        Action actionChoisie = utilisateur.getActions().get(choix - 1);
        utilisateur.executerAction(actionChoisie, cons, morceaux, artistes);
        visiter(utilisateur, cons);
    }
}
