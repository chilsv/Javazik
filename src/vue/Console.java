package vue;

import java.util.ArrayList;
import java.util.Scanner;

import controleur.actions.Quitter;
import metier.*;

public class Console {
    public Console() {
        System.out.println("-".repeat(40));
        System.out.println("Bienvenue dans Javazic !");
    }

    public int menu() {
        int choix;
        System.out.println("-".repeat(40));
        System.out.println("\t1- Visiter l'application");
        System.out.println("\t2- Se connecter");
        System.out.println("\t3- S'inscrire");
        System.out.println("\t4- Quitter");
        System.out.print("--> ");
        Scanner saisie = new Scanner(System.in);
        choix = saisie.nextInt();
        saisie.nextLine();
        return choix;
    }

    public void inscription() {
        System.out.println("-".repeat(40));
        System.out.println("Informations nécessaires :");
        System.out.print("Type (Abonné --> 1 ou Admin --> 2) : ");
    }

    public void connexion() {
        System.out.println("-".repeat(40));
        System.out.println("-- Identifiants de connexion --");
    }

    public void nom() {
        System.out.print("Nom : ");
    }

    public void mdp() {
        System.out.print("Mot de passe : ");
    }
    
    public void mail() {
        System.out.print("Adresse e-mail : ");
    }

    public void visiter(String accueil, String[] actions) {
        System.out.println("-".repeat(40));
        System.out.println(accueil);
        for (int i = 0; i < actions.length; i++) {
            System.out.println((i + 1) + "- " + actions[i]);
        }
        System.out.print("--> ");
    }

    public Filtre recherche(boolean filtrage) {
        String recherche;
        String choix = "";
        boolean morceau = false;
        boolean artiste = false;
        boolean album = false;
        boolean playlist = false;
        boolean croissant = false;
        int annee = 0;
        Scanner saisie = new Scanner(System.in);

        // Filtrage pour les abonnés
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
            System.out.print("--> ");
            choix = saisie.nextLine();
        }

        System.out.println("-".repeat(40));
        System.out.print("Recherche de : ");
        recherche = saisie.nextLine();

        // On parcourt la chaine pour déterminer les filtres choisis
        for (char c : choix.toUpperCase().toCharArray()) {
            if (c == '1') morceau = true;
            else if (c == '2') artiste = true;
            else if (c == '3') album = true;
            else if (c == '4') playlist = true;
            else if (c == 'A') {
                System.out.print("Année : ");
                annee = saisie.nextInt();
                saisie.nextLine();
            }
            else if (c == 'B') croissant = true;
        }

        // Sans filtre explicie on recherche partout
        if (!morceau && !artiste && !album && !playlist) {
            morceau = true;
            artiste = true;
            album = true;
            playlist = true;
        }

        Filtre filtre = new Filtre(recherche, morceau, artiste, album, playlist, croissant, annee);
        return filtre;
    }

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

    public Morceau ajouterMorceau() {
        Scanner saisie = new Scanner(System.in);
        System.out.println("-".repeat(40));
        System.out.print("Titre du morceau : ");
        String titre = saisie.nextLine();
        System.out.print("Nom de l'artiste : ");
        String artiste = saisie.nextLine();
        System.out.print("Nom de l'album (vide si aucun) : ");
        String album = saisie.nextLine();
        System.out.print("Durée (vide si aucune) : ");
        String duree = saisie.nextLine();
        Morceau morceau = new Morceau(titre, new Solo(artiste), duree.isEmpty() ? 0 : Integer.parseInt(duree));
        if (!album.isEmpty()) {
            morceau.setAlbum(album);
        }
        return morceau;
    }

    public Artiste ajouterArtiste() {
        Scanner saisie = new Scanner(System.in);
        System.out.println("-".repeat(40));
        System.out.print("Nom de l'artiste : ");
        String nom = saisie.nextLine();
        Solo artiste = new Solo(nom);
        return artiste;
    }
    
    public void quitter(Catalogue catalogue) {
        Quitter quitter = new Quitter();
        quitter.executer(this, null, catalogue);
    }

    public void choixInvalide() {
        System.out.println("Choix invalide, veuillez réessayer.");
    }

    public void afficherErreur(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }
}
