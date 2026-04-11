package controleur.formulaires;

// Formulaire pour créer un compte
public final class InscriptionForm {
    public final String type;
    public final String nom;
    public final String mail;
    public final String mdp;
    public int num = -1;

    public InscriptionForm(String type, String nom, String mail, String mdp) {
        this.type = type;
        this.nom = nom;
        this.mail = mail;
        this.mdp = mdp;
    }

    public InscriptionForm(String type, String nom, String mail, String mdp, int num) {
        this.type = type;
        this.nom = nom;
        this.mail = mail;
        this.mdp = mdp;
        this.num = num;
    }
}