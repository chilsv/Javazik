package metier;

import vue.Console;

public class Abonne extends Personne {
    private int num;

    public Abonne(String nom, String mail, String mdp, int num) {
        super(nom, mail, mdp);
        this.num = num;
    }

    @Override
    public void visiter(vue.Console cons) {
        cons.visiter(this);
    }

    public String getAccueil(Console cons) {
        return new String("Bienvenue sur la page d'accueil, " + getNom() + " !");
    }

    public String getMenu(Console cons) {
        return new String("1- Effectuer une recherche\n--> ");
    }

}
