package metier;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

import controleur.actions.Action;
import vue.Console;

public abstract class Personne {
    private String nom;
    private String mail;
    private String mdp;
    private LocalDate date_creation;
    private final ArrayList<Action> actions = new ArrayList<Action>();

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

    public abstract String getAccueil(Console cons);

    public String[] getMenu(Console cons) {
        ArrayList<Action> actions = getActions();
        String [] nomActions = new String[actions.size()];
        for (int i = 0; i < actions.size(); i++) {
            nomActions[i] = actions.get(i).getNom();
        }
        return nomActions;
    }

    public String getNom() {
        return nom;
    }

    public String getMail() {
        return mail;
    }

    public String getMdp() {
        return mdp;
    }

    public void executerAction(Action action, Console cons) {
        action.executer(cons, this);
    }

    public abstract ArrayList<Action> getActions();

}
