package controleur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import controleur.actions.Action;
import vue.*;
import metier.*;

public class Main {
    private static ArrayList<Abonne> abonnes = new ArrayList<Abonne>();
    private static ArrayList<Admin> admins = new ArrayList<Admin>();
    private static ArrayList<Morceau> morceaux = new ArrayList<Morceau>();
    private static ArrayList<Playlist> playlists = new ArrayList<Playlist>();
    private static ArrayList<Artiste> artistes = new ArrayList<Artiste>();
    private static ArrayList<Album> albums = new ArrayList<Album>();
    

    // Après la connexion d'un utilisateur, on définit une variable de type Personne qui contiendra soit un Abonné, soit un Admin, soit un Visiteur
    // Ca permet ensuite de vérifier les droits de l'utilisateur connecté
    // ex: Personne utilisateur = new Visiteur();
    // ensuite, if (utilisateur instanceof Admin) // faire les trucs réservés aux admins

    public static void main(String[] args) {
        charger(abonnes, "abonnes.ser");
        charger(admins, "admins.ser");
        charger(morceaux, "morceaux.ser");
        charger(playlists, "playlists.ser");
        charger(artistes, "artistes.ser");
        charger(albums, "albums.ser");

        Console cons = new Console();
        menu(cons, abonnes, admins);
        //FenetreMenu fenetre = new FenetreMenu();
        //Evenements.ajouterEvenements(fenetre);
        //fenetre.afficher();
    }

    /**
     * Affiche le menu principal et gère les choix de l'utilisateur
     */
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

    public static <T> void charger(ArrayList<T> arrayList, String nomFichier) {
        File fichier = new File("donnees/" + nomFichier);

        if (!fichier.exists()) {
            try (FileOutputStream fos = new FileOutputStream(fichier)) {
                // Crée le fichier vide
            } catch (IOException e) {
                return;
            }
        }

        try (FileInputStream fis = new FileInputStream(fichier);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            ArrayList<T> chargement = (ArrayList<T>) ois.readObject();
            arrayList.addAll(chargement);
        } catch (Exception e) {
        }
    }

    public static void sauvegarder(ArrayList<?> arrayList, String nomFichier) {
        File dossierDonnees = new File("donnees");
        if (!dossierDonnees.exists()) {
            dossierDonnees.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream("donnees/" + nomFichier);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(arrayList);
        } catch (IOException e) {
        }
    }

    public static void ajouterAbonne(Personne utilisateur, ArrayList<Abonne> abonnes) {
        abonnes.add((Abonne) utilisateur);
        sauvegarder(abonnes, "abonnes.ser");
    }

    public static void ajouterAdmin(Personne utilisateur, ArrayList<Admin> admins) {
        admins.add((Admin) utilisateur);
        sauvegarder(admins, "admins.ser");
    }

}