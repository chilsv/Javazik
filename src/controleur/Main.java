package controleur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import metier.*;
import vue.FenetreMenu;

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

        int mode = 1;

        if (mode == 1) {
            ConsoleMain consoleMain = new ConsoleMain(abonnes, admins, catalogue);
        } else {
            FenetreMenu fenetre = new FenetreMenu();
            Evenements.ajouterEvenements(fenetre);
            fenetre.afficher();
        }
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