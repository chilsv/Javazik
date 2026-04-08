package controleur;

import java.util.ArrayList;
import java.util.Scanner;

import vue.*;
import metier.*;

public class Main {
    private ArrayList<Abonne> abonnes;
    private ArrayList<Admin> admins;

    // Après la connexion d'un utilisateur, on définit une variable de type Personne qui contiendra soit un Abonné, soit un Admin, soit un Visiteur
    // Ca permet ensuite de vérifier les droits de l'utilisateur connecté
    // ex: Personne utilisateur = new Visiteur();
    // ensuite, if (utilisateur instanceof Admin) // faire les trucs réservés aux admins

    public static void main(String[] args) {
        ArrayList<Abonne> abonnes = new ArrayList<>();
        ArrayList<Admin> admins = new ArrayList<>();

        Console cons = new Console();
        menu(cons, abonnes, admins);
        //FenetreMenu fenetre = new FenetreMenu();
        //Evenements.ajouterEvenements(fenetre);
        //fenetre.afficher();
    }

    public static void menu(Console cons, ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {
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
                    menu(cons, abonnes, admins);
                } catch (MdpIncorrectException e) {
                    cons.afficherErreur(e.getMessage());
                    menu(cons, abonnes, admins);
                }
                break;
            case 3:
                try {
                    inscription(cons, abonnes, admins);
                } catch (UtilisateurDejaCreeException e) {
                    cons.afficherErreur(e.getMessage());
                    menu(cons, abonnes, admins);
                }
                break;
            case 4:
                cons.quitter();
                break;
            default:
                cons.choixInvalide();
                menu(cons, abonnes, admins);
        }
    }

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
        utilisateur.visiter(cons);
    }

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
                    abonne.visiter(cons);
                    return;
                } else {
                    throw new MdpIncorrectException();
                }
            }
        }
        for (Admin admin : admins) {
            if (admin.getMail().equals(mail)) {
                if (admin.getMdp().equals(mdp)) {
                    admin.visiter(cons);
                    return;
                } else {
                    throw new MdpIncorrectException();
                }
            }
        }

        throw new UtilisateurIntrouvableException();
    }

    public static void visiter(Personne utilisateur, Console cons) {
        cons.visiter(utilisateur);
    }

    public static void ajouterAbonne(Personne utilisateur, ArrayList<Abonne> abonnes) {
        abonnes.add((Abonne) utilisateur);
    }

    public static void ajouterAdmin(Personne utilisateur, ArrayList<Admin> admins) {
        admins.add((Admin) utilisateur);
    }
}
