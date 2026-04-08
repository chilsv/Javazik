package controleur;

import vue.FenetreMenu;

public class Main {

    public static void main(String[] args) {
        FenetreMenu fenetre = new FenetreMenu();
        Evenements.ajouterEvenements(fenetre);
        fenetre.afficher();
    }


}
