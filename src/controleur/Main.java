package controleur;

import java.io.File;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import controleur.actions.Action;
import controleur.actions.Deconnexion;
import controleur.actions.Quitter;
import metier.*;
import vue.*;

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
        Catalogue catalogue = new Catalogue(morceaux, playlists, artistes, albums);

        // Définition de la vue qu'on utilise
        InterfaceVue vue = new Console();
        //InterfaceVue vue = new Fenetre();

        menu(vue, abonnes, admins, catalogue);
    }
    
    public static <T> void charger(ArrayList<T> arrayList, String nomFichier) {
        File fichier = new File("donnees/" + nomFichier);

        if (!fichier.exists()) {
            try (FileOutputStream fos = new FileOutputStream(fichier)) {
            } catch (IOException e) {
                return;
            }
        }

        try (FileInputStream fis = new FileInputStream(fichier);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            ArrayList<T> chargement = (ArrayList<T>) ois.readObject();
            arrayList.addAll(chargement);
        } catch (EOFException e) {
        } catch (InvalidClassException e) {
            // si la classe a chanté entre temps, on réinitialise le fichier
            sauvegarder(new ArrayList<>(), nomFichier);
        } catch (Exception e) {
            System.err.println("Erreur lors du changement de " + nomFichier);
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
            System.err.println("Erreur lors de la sauvegarde de " + nomFichier + " : " + e.getMessage());
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

    public static void menu(InterfaceVue vue, ArrayList<Abonne> abonnes, ArrayList<Admin> admins, Catalogue catalogue) {
        // obtention du choix
        int choix = vue.menuPrincipal();
        switch (choix) {
            case 1: // visiteur
                visiter(new Visiteur(), vue, abonnes, admins, catalogue);
                break;
            case 2: // connexion
                try {
                    connexion(vue, abonnes, admins, catalogue);
                } catch (UtilisateurIntrouvableException e) {
                    vue.afficherErreur(e.getMessage());
                    menu(vue, abonnes, admins, catalogue);
                } catch (MdpIncorrectException e) {
                    vue.afficherErreur(e.getMessage());
                    menu(vue, abonnes, admins, catalogue);
                }
                break;
            case 3: // inscription
                try {
                    inscription(vue, abonnes, admins, catalogue);
                } catch (UtilisateurDejaCreeException e) {
                    vue.afficherErreur(e.getMessage());
                    menu(vue, abonnes, admins, catalogue);
                }
                break;
            case 4: // quitter
                new Quitter().executer(vue, null, catalogue);
                break;
            default:
                vue.afficherErreur("Choix invalide.");
                menu(vue, abonnes, admins, catalogue);
        }
    }

    public static void inscription(InterfaceVue vue, ArrayList<Abonne> abonnes, ArrayList<Admin> admins, Catalogue catalogue) throws UtilisateurDejaCreeException {
        Personne utilisateur;
        InscriptionForm formulaire = vue.demanderInscription(); // prend les infos
        String type = formulaire.type; // type d'utilisateur (abonné ou admin)
        if (!"1".equals(type) && !"2".equals(type)) {
            vue.afficherErreur("Choix invalide pour le type d'utilisateur.");
            inscription(vue, abonnes, admins, catalogue);
            return;
        }

        String nom = formulaire.nom;
        String mail = formulaire.mail;

        // on vérifie que c'est psa déjà pris
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

        String mdp = formulaire.mdp;

        switch (type) {
            case "1":
                utilisateur = new Abonne(nom, mail, mdp, abonnes.size());
                ajouterAbonne(utilisateur, abonnes);
                break;
            case "2":
                utilisateur = new Admin(nom, mail, mdp, admins.size());
                ajouterAdmin(utilisateur, admins);
                break;
            default:
                utilisateur = new Visiteur();
                break;
        }

        // On lance l'appli
        visiter(utilisateur, vue, abonnes, admins, catalogue);
    }

    public static void connexion(InterfaceVue vue, ArrayList<Abonne> abonnes, ArrayList<Admin> admins, Catalogue catalogue) throws UtilisateurIntrouvableException, MdpIncorrectException {
        ConnexionForm formulaire = vue.demanderConnexion(); // obitent les infos
        String mail = formulaire.mail;
        String mdp = formulaire.mdp;

        // vérifie mdp
        for (Abonne abonne : abonnes) {
            if (abonne.getMail().equals(mail)) {
                if (abonne.getMdp().equals(mdp)) {
                    visiter(abonne, vue, abonnes, admins, catalogue);
                    return;
                }
                throw new MdpIncorrectException();
            }
        }
        for (Admin admin : admins) {
            if (admin.getMail().equals(mail)) {
                if (admin.getMdp().equals(mdp)) {
                    visiter(admin, vue, abonnes, admins, catalogue);
                    return;
                }
                throw new MdpIncorrectException();
            }
        }

        throw new UtilisateurIntrouvableException();
    }

    public static void visiter(Personne utilisateur, InterfaceVue vue, ArrayList<Abonne> abonnes, ArrayList<Admin> admins, Catalogue catalogue) {
        // On prend les actions possibles pour l'utilisateur pour lui afficher la bonne fenêtre
        ArrayList<Action> actionsPossibles = utilisateur.getActions();
        // on récupère l'action choisie
        Action actionChoisie = vue.choisirAction(utilisateur.getAccueil(vue), actionsPossibles);
        if (actionChoisie == null) {
            vue.afficherErreur("Choix invalide pour l'action.");
            visiter(utilisateur, vue, abonnes, admins, catalogue);
            return;
        }
        try {
            // on fait l'action choisie
            utilisateur.executerAction(actionChoisie, vue, catalogue);
        } catch (MorceauIntrouvableException e) {
            vue.afficherErreur(e.getMessage());
            visiter(utilisateur, vue, abonnes, admins, catalogue);
            return;
        }

        if (actionChoisie instanceof Deconnexion) {
            menu(vue, abonnes, admins, catalogue);
            return;
        }

        visiter(utilisateur, vue, abonnes, admins, catalogue);
    }

}