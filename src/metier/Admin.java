package metier;

import vue.Console;

public class Admin extends Personne {
    private int num;

    public Admin(String nom, String mail, String mdp, int num) {
        super(nom, mail, mdp);
        this.num = num;
    }

    public void visiter(vue.Console cons) {
        cons.visiter(this);
    }

    public String getAccueil(Console cons) {
        return new String("Bienvenue sur la page de gestion, " + getNom() + " !");
    }

    public String getMenu(Console cons) {
        return new String("1- Ajouter un artiste\n--> ");
    }
}
