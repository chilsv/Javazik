package metier;

public class Admin extends Personne {
    private int num;

    public Admin(String nom, String mail, String mdp, int num) {
        super(nom, mail, mdp);
        this.num = num;
    }
}
