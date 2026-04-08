package vue;

import metier.Personne;
import metier.Abonne;
import metier.Admin;

public class Console {

    public Console() {
        System.out.println("-".repeat(25));
        System.out.println("Bienvenue dans Javazic !");
    }

    public void menu() {
        System.out.println("-".repeat(25));
        System.out.println("\t1- Visiter l'application");
        System.out.println("\t2- Se connecter");
        System.out.println("\t3- S'inscrire");
        System.out.println("\t4- Quitter");
        System.out.print("--> ");
    }

    public void inscription() {
        System.out.println("-".repeat(25));
        System.out.println("Informations nécessaires :");
        System.out.print("Type (Abonné --> 1 ou Admin --> 2) : ");
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

    public void visiter(Personne utilisateur) {
        System.out.println("-".repeat(25));
        System.out.println("Bienvenue sur la page d'accueil !");
    }

    public void visiter(Abonne utilisateur) {
        System.out.println("-".repeat(25));
        System.out.println("Bienvenue sur la page d'accueil, " + utilisateur.getNom() + " !");
    }

    public void visiter(Admin utilisateur) {
        System.out.println("-".repeat(25));
        System.out.println("Bienvenue sur la page de gestion, " + utilisateur.getNom() + " !");
    }

    public void quitter() {
        System.out.println("A bientôt sur Javazic !");
        System.exit(0);
    }

    public void choixInvalide() {
        System.out.println("Choix invalide, veuillez réessayer.");
    }
}
