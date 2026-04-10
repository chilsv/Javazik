package controleur.formulaires;

// Formulaire pour se connecter
public final class ConnexionForm {
    public final String mail;
    public final String mdp;

    public ConnexionForm(String mail, String mdp) {
        this.mail = mail;
        this.mdp = mdp;
    }
}