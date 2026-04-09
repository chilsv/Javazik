package metier;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import controleur.actions.Action;
import vue.Console;

public abstract class Personne implements Serializable {
    private String nom;
    private String mail;
    private String mdp;
    private LocalDate date_creation;
    private final ArrayList<Action> actions = new ArrayList<Action>();

    /**
     * Pour l'instanciation d'un Visiteur
     */
    public Personne() {
        nom = "Visiteur";
        mail = "";
        mdp = "";
        date_creation = LocalDate.now();
    }

    /**
     * Instanciation d'un abonné ou d'un administrateur
     */
    public Personne(String nom, String mail, String mdp) {
        this.nom = nom;
        this.mail = mail;
        this.mdp = mdp;
        date_creation = LocalDate.now();
    }

    /**
     * Message d'accueil personnalisé
     */
    public abstract String getAccueil(Console cons);

    /**
     * Renvoie le menu personnalisé du type d'utilisateur
     */
    public String[] getMenu(Console cons) {
        ArrayList<Action> actions = getActions();
        // On recupère les noms des actions possibles
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

    public LocalDate getDateCreation() {
        return date_creation;
     }

    /**
     * Permet d'exécuter une action choisie par l'utilisateur
     */
    public void executerAction(Action action, Console cons) {
        action.executer(cons, this);
    }

    public void executerAction(Action action, Console cons, Catalogue catalogue) {
        action.executer(cons, this, catalogue);
    }

    /**
     * Renvoie la liste des actions possibles
     */
    public abstract ArrayList<Action> getActions();

}
