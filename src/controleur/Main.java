package controleur;

import java.util.ArrayList;

import vue.FenetreMenu;
import metier.Abonne;
import metier.Admin;
import metier.Visiteur;

public class Main {
    private ArrayList<Abonne> abonnes;
    private ArrayList<Admin> admins;

    public static void main(String[] args) {
        FenetreMenu fenetre = new FenetreMenu();
        Evenements.ajouterEvenements(fenetre);
        fenetre.afficher();
    }

    // Retourne le nombre d'abonnés (pour inscire un visiteur par exemple)
    public int getNombreAbonnes() {
        return abonnes.size();
    }

    // Retourne le nombre d'administrateurs (pour en ajouter un)
    public int getNombreAdmins() {
        return admins.size();
    }

}
