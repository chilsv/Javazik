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
}
