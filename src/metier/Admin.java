package metier;

public class Admin extends Personne {
    private int num;

    public Admin(String nom, String mail, String mdp) {
        super(nom, mail, mdp);
        num = 0;
    }
}
