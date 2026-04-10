package controleur.actions;

public class Deconnexion implements Action {
    /**
     * @param arguments vue
     */
    @Override
    public void executer(ActionArguments arguments) {
        arguments.vue.afficherMessage("Déconnexion...");
    }

    @Override
    public String getNom() {
        return "Retourner au menu principal";
    }
}