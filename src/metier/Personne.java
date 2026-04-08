package metier;

import java.time.LocalDate;

public abstract class Personne {
    private String nom;
    private String mail;
    private String mdp;
    private LocalDate date_creation;

    // Pour l'instanciation d'un Visiteur
    public Personne() {
        nom = "Visiteur";
        mail = "";
        mdp = "";
        date_creation = LocalDate.now();
    }

    // Instanciation d'un abonné ou d'un administrateur
    public Personne(String nom, String mail, String mdp) {
        this.nom = nom;
        this.mail = mail;
        this.mdp = mdp;
        date_creation = LocalDate.now();
    }

}
