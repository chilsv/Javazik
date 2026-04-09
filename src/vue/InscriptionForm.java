package vue;

// Formulaire pour créer un compte
public final class InscriptionForm {
    public final String type;
    public final String nom;
    public final String mail;
    public final String mdp;

    public InscriptionForm(String type, String nom, String mail, String mdp) {
        this.type = type;
        this.nom = nom;
        this.mail = mail;
        this.mdp = mdp;
    }
}