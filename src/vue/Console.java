package vue;

import java.util.ArrayList;
import java.util.Scanner;

import controleur.actions.Action;
import controleur.formulaires.ArtisteForm;
import controleur.formulaires.ConnexionForm;
import controleur.formulaires.InscriptionForm;
import controleur.formulaires.MorceauForm;
import controleur.formulaires.PlaylistForm;
import controleur.formulaires.RechercheForm;
import metier.*;

public class Console implements InterfaceVue {
    public Console() {
        System.out.println("-".repeat(40));
        System.out.println("Bienvenue dans Javazic !");
    }

    private String lireTexte(String invite) {
        Scanner saisie = new Scanner(System.in);
        System.out.print(invite);
        return saisie.nextLine();
    }

    @Override
    public int menuPrincipal() {
        Scanner saisie = new Scanner(System.in);
        System.out.println("-".repeat(40));
        System.out.println("\t1- Visiter l'application");
        System.out.println("\t2- Se connecter");
        System.out.println("\t3- S'inscrire");
        System.out.println("\t4- Quitter");
        System.out.print("--> ");
        int choix = saisie.nextInt();
        saisie.nextLine();
        return choix;
    }

    @Override
    public Action choisirAction(String accueil, Personne utilisateur) {
        ArrayList<Action> actions = utilisateur.getActions();
        Scanner saisie = new Scanner(System.in);
        System.out.println("-".repeat(40));
        System.out.println(accueil);
        for (int i = 0; i < actions.size(); i++) {
            System.out.println((i + 1) + "- " + actions.get(i).getNom());
        }
        System.out.print("--> ");
        String entree = saisie.nextLine();
        try {
            int choix = Integer.parseInt(entree);
            if (choix < 1 || choix > actions.size()) {
                return null;
            }
            return actions.get(choix - 1);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void afficherMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void afficherErreur(Exception e) {
        System.out.println();
        System.out.println(e.getMessage());
        System.out.println();
    }

    @Override
    public ConnexionForm demanderConnexion() {
        System.out.println("-".repeat(40));
        System.out.println("-- Identifiants de connexion --");
        return new ConnexionForm(lireTexte("Adresse e-mail : "), lireTexte("Mot de passe : "));
    }

    @Override
    public InscriptionForm demanderInscription() {
        System.out.println("-".repeat(40));
        System.out.println("Informations nécessaires :");
        return new InscriptionForm(
                "abonne",
                lireTexte("Nom : "),
                lireTexte("Adresse e-mail : "),
                lireTexte("Mot de passe : "));
    }

    public Filtre afficherFiltres() {
        String choix = "";
        System.out.println("-".repeat(40));
        System.out.println("Filtres disponibles (choisir UN chiffre) :");
        System.out.println("1- Morceaux");
        System.out.println("2- Artistes");
        System.out.println("3- Albums");
        System.out.println("4- Playlists");
        System.out.println("A- Une année");
        System.out.println("B- Trier par ordre croissant");
        System.out.println("C- Trier par genre");
        System.out.println("D- Trier par zone géographique");
        choix = lireTexte("--> ");

        boolean morceau = false;
        boolean artiste = false;
        boolean album = false;
        boolean playlist = false;
        boolean croissant = false;
        int[] annee = new int[] {0, 0};

        for (char c : choix.toUpperCase().toCharArray()) {
            if (c == '1') morceau = true;
            else if (c == '2') artiste = true;
            else if (c == '3') album = true;
            else if (c == '4') playlist = true;
            else if (c == 'A') {
                try {
                    int valeur = Integer.parseInt(lireTexte("Année : "));
                    annee = new int[] {valeur, valeur};
                } catch (NumberFormatException e) {
                    annee = new int[] {0, 0};
                }
            } else if (c == 'B') {
                croissant = true;
            }
        }

        if (!morceau && !artiste && !album && !playlist) {
            morceau = true;
            artiste = true;
            album = true;
            playlist = true;
        }

        return new Filtre(morceau, artiste, album, playlist, croissant, annee);
    }

    @Override
    public RechercheForm demanderRecherche(Filtre filtre) {
        String recherche = lireTexte("Recherche de : ");
        return new RechercheForm(recherche, filtre);
    }

    @Override
    public void afficherRecherche(ResultatRecherche resultat) {
        System.out.println("-".repeat(40));
        System.out.println("Résultats de la recherche :");
        System.out.println("-".repeat(40));
        System.out.println("Morceaux :");
        for (Morceau morceau : resultat.morceaux) {
            System.out.println("- " + morceau.getNom());
        }
        System.out.println("-".repeat(40));
        System.out.println("Artistes :");
        for (Artiste artiste : resultat.artistes) {
            System.out.println("- " + artiste.getNom());
        }
        System.out.println("-".repeat(40));
        System.out.println("Albums :");
        for (Album album : resultat.albums) {
            System.out.println("- " + album.getNom());
        }
        System.out.println("-".repeat(40));
        System.out.println("Playlists :");
        for (Playlist playlist : resultat.playlists) {
            System.out.println("- " + playlist.getNom());
        }
    }

    @Override
    public MorceauForm demanderMorceau() {
        System.out.println("-".repeat(40));
        String titre = lireTexte("Titre du morceau : ");
        String artiste = lireTexte("Nom de l'artiste : ");
        String album = lireTexte("Nom de l'album (vide si aucun) : ");
        String dureeTexte = lireTexte("Durée (vide si aucune) : ");
        int duree;
        try {
            duree = dureeTexte == null || dureeTexte.isBlank() ? 180 : Integer.parseInt(dureeTexte);
        } catch (NumberFormatException e) {
            duree = 180;
        }
        return new MorceauForm(titre, artiste, album, duree);
    }

    @Override
    public ArtisteForm demanderArtiste() {
        System.out.println("-".repeat(40));
        return new ArtisteForm(lireTexte("Nom de l'artiste : "));
    }

    @Override
    public String choisirMorceau() {
        return lireTexte("Morceau à jouer : ");
    }

    @Override
    public void afficherLecture(Morceau morceau) {
        final int largeurBarre = 30;
        System.out.println("Lecture : " + morceau.getNom());

        for (int seconde = 0; seconde <= morceau.getDuree(); seconde++) {
            int remplissage = (int) Math.round((seconde / (double) morceau.getDuree()) * largeurBarre);
            String barre = "#".repeat(remplissage) + "-".repeat(largeurBarre - remplissage);
            System.out.print("\r[" + barre + "] " + seconde + "s / " + morceau.getDuree() + "s");
            System.out.flush();

            if (seconde < morceau.getDuree()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\nLecture interrompue.");
                    return;
                }
            }
        }

        System.out.println("\nLecture terminée.");
    }

    @Override
    public void afficherProfilAbonne(Abonne abonne, Catalogue catalogue) {
        System.out.println("-".repeat(40));
        System.out.println("Profil de "+ abonne.getNom() + " :");
        System.out.println("Compte créé le " + abonne.getDateCreation());
        System.out.println("E-mail : " + abonne.getMail());
        System.out.println("Playlists sauvegardées :");
        if (abonne.getPlaylists().isEmpty()) {
            System.out.println("- Aucune playlist");
            return;
        }

        for (int i = 0; i < abonne.getPlaylists().size(); i++) {
            int numPlaylist = abonne.getPlaylists().get(i);
            Playlist playlist = catalogue.getPlaylist(numPlaylist);
            if (playlist != null) {
                System.out.println("- " + playlist.getNom());
            }
        }
    }

    public void afficherProfilAdmin(Admin admin) {
        System.out.println("-".repeat(40));
        System.out.println("Profil de l'administrateur " + admin.getNom()  + " :");
        System.out.println("Compte créé le " + admin.getDateCreation());
    }

    @Override
    public PlaylistForm demanderPlaylist(int numUtilisateur) {
        Scanner saisie = new Scanner(System.in);
        System.out.println("-".repeat(40));
        String nom = lireTexte("Nom de la playlist : ");
        return new PlaylistForm(nom, numUtilisateur);
    }

    @Override
    public void afficherUtilisateurs(ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {
        System.out.println("-".repeat(40));
        if (abonnes == null && admins == null) {
            System.out.println("Aucun utilisateur enregistré.");
            return;
        } else if (abonnes.isEmpty() && admins.isEmpty()) {
            System.out.println("Aucun utilisateur enregistré.");
            return;
        }
        if (abonnes == null) {
            System.out.println("Aucun abonné enregistré.");
        } else if (abonnes.isEmpty()) {
            System.out.println("Aucun abonné enregistré.");
        } else {
            System.out.println("Liste des abonnés :");
            for (Abonne abonne : abonnes) {
                System.out.println(abonne.getNom() + " (@" + abonne.getMail() + ") : " + abonne.getAge() + " jours.");
            }
        }
        if (admins == null) {
            System.out.println("Aucun administrateur enregistré.");
        } else if (admins.isEmpty()) {
            System.out.println("Aucun administrateur enregistré.");
        } else {
            System.out.println("-".repeat(40));
            System.out.println("Liste des admins :");
            for (Admin admin : admins) {
                System.out.println(admin.getNom() + " (@" + admin.getMail() + ") : " + admin.getAge() + " jours.");
            }
        }
        System.out.println();
    }

}
