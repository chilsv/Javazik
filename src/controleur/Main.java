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
import controleur.formulaires.RechercheForm;
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
    private static Filtre filtreRechercheCourant = filtreParDefaut();
    private static ArrayList<Morceau> queue = new ArrayList<Morceau>();

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

        initialiser();
        catalogue = new Catalogue(morceaux, playlists, artistes, albums);

        // Définition de la vue qu'on utilise
        //InterfaceVue vue = new Console();
        InterfaceVue vue = new Fenetre();

        menu(vue, abonnes, admins, catalogue);
    }

    public static void initialiser() {
        if (admins.size() == 0) {
            InscriptionForm defautForm = new InscriptionForm("admin", "Admin", "defaut", "", 0);
            InscriptionForm gabForm = new InscriptionForm("admin", "Gab", "gabriel.jamet@edu.ece.fr", "gab", 1);
            InscriptionForm ilanForm = new InscriptionForm("admin", "Ilan", "ilan.bide", "", 2);
            //InscriptionForm ExAbo = new InscriptionForm("abonne", "Sacha", "a", "a", 3);
            try {
                inscription(abonnes, admins, catalogue, defautForm);
                inscription(abonnes, admins, catalogue, gabForm);
                inscription(abonnes, admins, catalogue, ilanForm);
                //inscription(abonnes, admins, catalogue, ExAbo);
            } catch (UtilisateurDejaCreeException e) {}
        }
    }

    public static <T> void charger(ArrayList<T> arrayList, String nomFichier) {
        File dossierDonnees = new File("donnees");
        if (!dossierDonnees.exists()) {
            dossierDonnees.mkdirs();
        }

        File fichier = new File("donnees/" + nomFichier);

        if (!fichier.exists()) {
            sauvegarder(new ArrayList<>(), nomFichier);
            return;
        }

        try (FileInputStream fis = new FileInputStream(fichier);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            ArrayList<T> chargement = (ArrayList<T>) ois.readObject();
            arrayList.addAll(chargement);
        } catch (EOFException e) {
            sauvegarder(new ArrayList<>(), nomFichier);
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
        for (Admin admin :admins) {
            if (admin.getMail().equals(utilisateur.getMail())) {
                return; // admin déjà existant
            }
        }
        abonnes.add((Abonne) utilisateur);
        sauvegarder(abonnes, "abonnes.ser");
    }

    public static void ajouterAdmin(Personne utilisateur, ArrayList<Admin> admins) {
        for (Abonne abonne :abonnes) {
            if (abonne.getMail().equals(utilisateur.getMail())) {
                return; // abonné déjà existant
            }
        }
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
                    ConnexionForm connexionForm = vue.demanderConnexion();
                    if (connexionForm == null) {
                        menu(vue, abonnes, admins, catalogue);
                        break;
                    }
                    connexion(vue, abonnes, admins, catalogue, connexionForm);
                } catch (UtilisateurIntrouvableException e) {
                    vue.afficherErreur(e);
                    menu(vue, abonnes, admins, catalogue);
                } catch (MdpIncorrectException e) {
                    vue.afficherErreur(e);
                    menu(vue, abonnes, admins, catalogue);
                }
                break;
            case 3: // inscription
                try {
                    InscriptionForm inscriptionForm = vue.demanderInscription();
                    if (inscriptionForm == null) {
                        menu(vue, abonnes, admins, catalogue);
                        break;
                    }
                    Personne utilisateur = inscription(abonnes, admins, catalogue, inscriptionForm);
                    visiter(utilisateur, vue, abonnes, admins, catalogue);
                } catch (UtilisateurDejaCreeException e) {
                    vue.afficherErreur(e);
                    menu(vue, abonnes, admins, catalogue);
                }
                break;
            case 4: // quitter
                new Quitter().executer(new ActionArguments(vue, null, catalogue));
                break;
            default:
                vue.afficherErreur(new ActionException("Choix invalide."));
                menu(vue, abonnes, admins, catalogue);
        }
    }

    public static Personne inscription(ArrayList<Abonne> abonnes, ArrayList<Admin> admins, Catalogue catalogue, InscriptionForm formulaire) throws UtilisateurDejaCreeException {
        Personne utilisateur;
        String nom = formulaire.nom;
        String mail = formulaire.mail == null ? "" : formulaire.mail.trim();
        String mdp = formulaire.mdp;

        if (mailDejaPris(mail, abonnes, admins)) {
            throw new UtilisateurDejaCreeException();
        }

        if (formulaire.num == -1) {
            formulaire.num = abonnes.size() + admins.size();
        }

        switch (formulaire.type) {
            case "abonne":
                utilisateur = new Abonne(nom, mail, mdp, formulaire.num, catalogue);
                ajouterAbonne(utilisateur, abonnes);
                break;
            case "admin":
                utilisateur = new Admin(nom, mail, mdp, formulaire.num);
                ajouterAdmin(utilisateur, admins);
                break;
            default:
                utilisateur = new Visiteur();
                break;
        }
        return utilisateur;
    }

    private static boolean mailDejaPris(String mail, ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {
        for (Abonne abonne : abonnes) {
            if (abonne.getMail().equalsIgnoreCase(mail)) {
                return true;
            }
        }
        for (Admin admin : admins) {
            if (admin.getMail().equalsIgnoreCase(mail)) {
                return true;
            }
        }
        return false;
    }

    public static void connexion(InterfaceVue vue, ArrayList<Abonne> abonnes, ArrayList<Admin> admins, Catalogue catalogue, ConnexionForm formulaire) throws UtilisateurIntrouvableException, MdpIncorrectException {
        String mail = formulaire.mail;
        String mdp = formulaire.mdp;
        if (mdp == null) {
            mdp = "";
        }

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
        if (vue instanceof Fenetre) {
            Fenetre fenetre = (Fenetre) vue;
            if (utilisateur instanceof Abonne) {
                Abonne abonne = (Abonne) utilisateur;
                fenetre.configurerActionsResultats(
                    morceau -> {
                        if (morceau == null) {
                            return;
                        }
                        if (!abonne.morceauDejaAime(morceau, catalogue)) {
                            catalogue.ajouterMorceauPlaylist(morceau, abonne.getAimes());
                        } else {
                            abonne.retirerMorceauPlaylist(morceau, catalogue, abonne.getAimes());
                        }
                    },
                    morceau -> morceau != null && abonne.morceauDejaAime(morceau, catalogue),
                    playlist -> {
                        if (playlist == null) {
                            return;
                        }
                        if (!abonne.playlistDejaSauvegardee(playlist.getNum())) {
                            abonne.ajouterPlaylist(playlist.getNum());
                        } else {
                            abonne.retirerPlaylist(playlist.getNum());
                        }
                    },
                    playlist -> playlist != null && abonne.playlistDejaSauvegardee(playlist.getNum())
                );
            } else {
                fenetre.configurerActionsResultats(
                    morceau -> vue.afficherErreur(new ActionException("Réservé aux abonnés")),
                    morceau -> false,
                    playlist -> vue.afficherErreur(new ActionException("Réservé aux abonnés")),
                    playlist -> false
                );
            }
        }
        // on récupère l'action choisie
        Action actionChoisie = vue.choisirAction(utilisateur.getAccueil(vue), utilisateur);

        if (actionChoisie == null) {
            vue.afficherErreur(new ActionException("Choix invalide pour l'action."));
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
                vue.afficherErreur(e);
            }
        } else if (actionChoisie instanceof ConsulterProfil) {
            consulter_profil(utilisateur, vue, catalogue);
        } else if (actionChoisie instanceof ConsulterLibrairie) {
        } else if (actionChoisie instanceof Recherche) {
            rechercher(vue, utilisateur, catalogue);
        } else if (actionChoisie instanceof ChoisirFiltre) {
            ActionArguments actionArguments = new ActionArguments(vue, utilisateur, catalogue, filtreRechercheCourant);
            new ChoisirFiltre().executer(actionArguments);
            if (actionArguments.filtre != null) {
                filtreRechercheCourant = actionArguments.filtre;
            }
        } else if (actionChoisie instanceof ConsulterUtilisateurs) {
            new ConsulterUtilisateurs().executer(new ActionArguments(vue, utilisateur, catalogue, abonnes, admins));
        } else if (actionChoisie instanceof JouerMorceau) {
            jouerMorceau(vue, catalogue);
        }
        visiter(utilisateur, vue, abonnes, admins, catalogue);
        return;
    }

    public static void rechercher(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        Filtre filtre = vue.afficherFiltres();
        if (filtre == null) {
            filtre = filtreParDefaut();
        }
        RechercheForm formulaire = vue.demanderRecherche(filtre);
        if (formulaire == null) {
            return;
        }
        new Recherche().executer(new ActionArguments(vue, utilisateur, catalogue, formulaire));
    }

    private static Filtre filtreParDefaut() {
        return new Filtre(true, true, true, true, false, new int[] {0, 0});
    }

    public static void consulter_profil(Personne utilisateur, InterfaceVue vue, Catalogue catalogue) {
        if (utilisateur instanceof Abonne) {
            Abonne abonne = (Abonne) utilisateur;
            vue.afficherProfilAbonne(abonne, catalogue);
        } else if (utilisateur instanceof Admin) {
            Admin admin = (Admin) utilisateur;
            vue.afficherProfilAdmin(admin);
        }
    }

    public static void supprimerAbonne(Abonne abonne) {
        if (abonnes.contains(abonne)) {
            abonnes.remove(abonne);
            sauvegarder(abonnes, "abonnes.ser");
        }
    }

    public static void jouerMorceau(InterfaceVue vue, Catalogue catalogue) {
        try {
            ActionArguments arguments = new ActionArguments(catalogue, vue.choisirMorceau(), null);
            new JouerMorceau().executer(arguments);
            // On affiche la lecture du morceau trouve par l'action.
            vue.afficherLecture(arguments.morceauTrouve);
        } catch (MorceauIntrouvableException e) {
            vue.afficherErreur(e);
        }
    }
}