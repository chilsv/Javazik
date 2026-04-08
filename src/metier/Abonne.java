package metier;

public class Abonne extends Personne {
    private int num;

    public Abonne(String nom, String mail, String mdp, int num) {
        super(nom, mail, mdp);
        this.num = num;
    }

    

}
