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

import controleur.actions.*;
import controleur.formulaires.ConnexionForm;
import controleur.formulaires.InscriptionForm;
import controleur.formulaires.PlaylistForm;
import controleur.exceptions.*;
import metier.*;
import vue.*;

public class Main {
    private static ArrayList<Abonne> abonnes = new ArrayList<Abonne>();
    private static ArrayList<Admin> admins = new ArrayList<Admin>();
    private static ArrayList<Morceau> morceaux = new ArrayList<Morceau>();
    private static ArrayList<Playlist> playlists = new ArrayList<Playlist>();
    private static ArrayList<Artiste> artistes = new ArrayList<Artiste>();
    private static ArrayList<Album> albums = new ArrayList<Album>();
    private static Catalogue catalogue;

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
        catalogue = new Catalogue(morceaux, playlists, artistes, albums);

        // Définition de la vue qu'on utilise
        InterfaceVue vue = new Console();
        //InterfaceVue vue = new Fenetre();

        menu(vue, abonnes, admins, catalogue);
    }

    public static void initialiser() {
        if (admins.size() == 0) {
            InscriptionForm defautForm = new InscriptionForm("admin", "Admin", "defaut", "", 0);
            InscriptionForm gabForm = new InscriptionForm("admin", "Gab", "gabriel.jamet@edu.ece.fr", "gab", 1);
            InscriptionForm ilanForm = new InscriptionForm("admin", "Ilan", "ilan.bide", "", 2);
            try {
                inscription(abonnes, admins, catalogue, defautForm);
                inscription(abonnes, admins, catalogue, gabForm);
                inscription(abonnes, admins, catalogue, ilanForm);
            } catch (UtilisateurDejaCreeException e) {}
        }
    }

    public static <T> void charger(ArrayList<T> arrayList, String nomFichier) {
        File fichier = new File("donnees/" + nomFichier);

        if (!fichier.exists()) {
            try (FileOutputStream fos = new FileOutputStream(fichier)) {
                if ("admins.ser".equals(nomFichier)) {
                    initialiser();
                }
                return;
            } catch (IOException e) {
                initialiser();
                System.err.println("Erreur lors de la création de " + nomFichier);
                return;
            }
        }

        try (FileInputStream fis = new FileInputStream(fichier);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            ArrayList<T> chargement = (ArrayList<T>) ois.readObject();
            arrayList.addAll(chargement);
        } catch (EOFException e) {
            if ("admins.ser".equals(nomFichier)) {
                initialiser();
            }
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
        for (Abonne abonne :abonnes) {
            if (abonne.getMail().equals(utilisateur.getMail())) {
                return; // abonné déjà existant
            }
        }
        abonnes.add((Abonne) utilisateur);
        sauvegarder(abonnes, "abonnes.ser");
    }

    public static void ajouterAdmin(Personne utilisateur, ArrayList<Admin> admins) {
        for (Admin admin :admins) {
            if (admin.getMail().equals(utilisateur.getMail())) {
                return; // admin déjà existant
            }
        }
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
                    connexion(vue, abonnes, admins, catalogue, vue.demanderConnexion());
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
                    Personne utilisateur = inscription(abonnes, admins, catalogue, vue.demanderInscription());
                    visiter(utilisateur, vue, abonnes, admins, catalogue);
                } catch (UtilisateurDejaCreeException e) {
                    vue.afficherErreur(e.getMessage());
                    menu(vue, abonnes, admins, catalogue);
                }
                break;
            case 4: // quitter
                new Quitter().executer(new ActionArguments(vue, null, catalogue));
                break;
            default:
                vue.afficherErreur("Choix invalide.");
                menu(vue, abonnes, admins, catalogue);
        }
    }

    public static Personne inscription(ArrayList<Abonne> abonnes, ArrayList<Admin> admins, Catalogue catalogue, InscriptionForm formulaire) throws UtilisateurDejaCreeException {
        Personne utilisateur;
        String nom = formulaire.nom;
        String mail = formulaire.mail;
        String mdp = formulaire.mdp;
        if (formulaire.num == -1) {
            formulaire.num = abonnes.size() + admins.size();
        }

        switch (formulaire.type) {
            case "abonne":
                // on vérifie que c'est psa déjà pris
                for (Abonne abonne : abonnes) {
                    if (abonne.getMail().equals(mail)) {
                        throw new UtilisateurDejaCreeException();
                    }
                }
                utilisateur = new Abonne(nom, mail, mdp, formulaire.num, catalogue);
                ajouterAbonne(utilisateur, abonnes);
                break;
            case "admin":
                for (Admin admin : admins) {
                    if (admin.getMail().equals(mail)) {
                        throw new UtilisateurDejaCreeException();
                    }
                }
                utilisateur = new Admin(nom, mail, mdp, formulaire.num);
                ajouterAdmin(utilisateur, admins);
                break;
            default:
                utilisateur = new Visiteur();
                break;
        }
        return utilisateur;
    }

    public static void connexion(InterfaceVue vue, ArrayList<Abonne> abonnes, ArrayList<Admin> admins, Catalogue catalogue, ConnexionForm formulaire) throws UtilisateurIntrouvableException, MdpIncorrectException {
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
        } else if (actionChoisie instanceof Deconnexion) {
            menu(vue, abonnes, admins, catalogue);
        } else if (actionChoisie instanceof Quitter) {
            new Quitter().executer(new ActionArguments(vue, utilisateur, catalogue));
        } else if (actionChoisie instanceof AjouterArtiste) {
            new AjouterArtiste().executer(new ActionArguments(catalogue, vue.demanderArtiste()));
        } else if (actionChoisie instanceof AjouterMorceau) {
            new AjouterMorceau().executer(new ActionArguments(catalogue, vue.demanderMorceau()));
        } else if (actionChoisie instanceof AjouterPlaylist) {
            try {
                new AjouterPlaylist().executer(new ActionArguments(utilisateur, catalogue, vue.demanderPlaylist(utilisateur.getNum())));
            } catch (PlaylistDejaExistanteException e) {
                vue.afficherErreur(e.getMessage());
            }
        } else if (actionChoisie instanceof ConsulterProfil) {
        } else if (actionChoisie instanceof Recherche) {
        } else if (actionChoisie instanceof ConsulterUtilisateurs) {
            new ConsulterUtilisateurs().executer(new ActionArguments(vue, utilisateur, catalogue, abonnes, admins));
        } else if (actionChoisie instanceof JouerMorceau) {
            jouerMorceau(vue, catalogue);
        }
        visiter(utilisateur, vue, abonnes, admins, catalogue);
        return;
    }

    public static void supprimerAbonne(Abonne abonne) {
        if (abonnes.contains(abonne)) {
            abonnes.remove(abonne);
            sauvegarder(abonnes, "abonnes.ser");
        }
    }

    public static void jouerMorceau(InterfaceVue vue, Catalogue catalogue) {
        Morceau morceauTrouve = null;
        try {
            new JouerMorceau().executer(new ActionArguments(catalogue, vue.choisirMorceau(), morceauTrouve));
            // oN affiche la lecture du morceau
            vue.afficherLecture(morceauTrouve);
        } catch (MorceauIntrouvableException e) {
            vue.afficherErreur(e.getMessage());
        }
    }
}