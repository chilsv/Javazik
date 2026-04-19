package metier;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controleur.actions.Action;
import vue.InterfaceVue;

public abstract class Personne implements Serializable {
    private String nom;
    private String mail;
    private String mdp;
    private int num;
    private LocalDate date_creation;
    private final ArrayList<Action> actions = new ArrayList<Action>();
    private Map<Morceau, LocalDateTime> historique = new HashMap<Morceau, LocalDateTime>();

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
    public Personne(String nom, String mail, String mdp, int num) {
        this.nom = nom;
        this.mail = mail;
        this.mdp = mdp;
        date_creation = LocalDate.now();
    }

    /**
     * Message d'accueil personnalisé
     */
    public abstract String getAccueil(InterfaceVue vue);

    /**
     * Renvoie le menu personnalisé du type d'utilisateur
     */
    public String[] getMenu(InterfaceVue vue) {
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

    public int getNum() {
        return num;
    }

    public String getMail() {
        return mail;
    }

    public String getMdp() {
        return mdp;
    }

    public Map<Morceau, LocalDateTime> getHistorique() {
        return historique;
    }

    public void ajouterHistorique(Morceau morceau) {
        if (morceau == null) {
            return;
        }
        historique.put(morceau, LocalDateTime.now());
    }

    public int getAge() {
        // trouvé sur google
        return (int) ChronoUnit.DAYS.between(this.getDateCreation(), LocalDate.now());
    }
    
    public LocalDate getDateCreation() {
        return date_creation;
     }

    /**
     * Permet d'exécuter une action choisie par l'utilisateur
     * @params arguments vue, catalogue
     
    public void executerAction(Action action, ActionArguments arguments) throws ActionException {
        try {
            action.executer(arguments);
        } catch (MorceauIntrouvableException e) {
            throw e;
        }
    }*/

    /**
     * Renvoie la liste des actions possibles
     */
    public abstract ArrayList<Action> getActions();

}
