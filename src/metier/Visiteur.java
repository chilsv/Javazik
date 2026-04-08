package metier;

import vue.Console;

public class Visiteur extends Personne {
    public Visiteur() {
        super();
    }

    public String getAccueil(Console cons) {
        return new String("Bienvenue sur la page d'accueil !");
    }

    public String getMenu(Console cons) {
        return new String("1- Effectuer une recherche\n--> ");
    }

}
