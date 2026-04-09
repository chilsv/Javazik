package vue;

import java.util.ArrayList;
import java.util.Scanner;

import controleur.actions.Action;
import metier.Abonne;
import metier.Admin;
import metier.Album;
import metier.Artiste;
import metier.Filtre;
import metier.Morceau;
import metier.Playlist;
import metier.ResultatRecherche;
import metier.Solo;

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
    public Action choisirAction(String accueil, ArrayList<Action> actions) {
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
    public void afficherErreur(String message) {
        System.out.println();
        System.out.println(message);
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
                lireTexte("Type (Abonné --> 1 ou Admin --> 2) : "),
                lireTexte("Nom : "),
                lireTexte("Adresse e-mail : "),
                lireTexte("Mot de passe : "));
    }

    @Override
    public RechercheForm demanderRecherche(boolean filtrage) {
        String choix = "";
        if (filtrage) {
            System.out.println("-".repeat(40));
            System.out.println("-------- exemple de filtre :     2AB       --------");
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
        }

        String recherche = lireTexte("Recherche de : ");
        boolean morceau = false;
        boolean artiste = false;
        boolean album = false;
        boolean playlist = false;
        boolean croissant = false;
        int annee = 0;

        for (char c : choix.toUpperCase().toCharArray()) {
            if (c == '1') morceau = true;
            else if (c == '2') artiste = true;
            else if (c == '3') album = true;
            else if (c == '4') playlist = true;
            else if (c == 'A') {
                try {
                    annee = Integer.parseInt(lireTexte("Année : "));
                } catch (NumberFormatException e) {
                    annee = 0;
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

        return new RechercheForm(recherche, morceau, artiste, album, playlist, croissant, annee);
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

    public void afficherProfilAbonne(Abonne abonne) {
        System.out.println("-".repeat(40));
        System.out.println("Profil de "+ abonne.getNom() + " :");
        System.out.println("Compte créé le " + abonne.getDateCreation());
        System.out.println("E-mail : " + abonne.getMail());
        System.out.println("Playlists créées :");
        for (Playlist playlist : abonne.getPlaylists()) {
            System.out.println("- " + playlist.getNom());
        }
    }

    public void afficherProfilAdmin(Admin admin) {
        System.out.println("-".repeat(40));
        System.out.println("Profil de l'administrateur " + admin.getNom()  + " :");
        System.out.println("Compte créé le " + admin.getDateCreation());
    }

}
