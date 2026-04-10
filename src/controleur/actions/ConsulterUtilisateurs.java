package controleur.actions;

public class ConsulterUtilisateurs implements Action {
	/**
	 * @param arguments vue, abonnes, admins
	 */
	@Override
	public void executer(ActionArguments arguments) {
		arguments.vue.afficherUtilisateurs(arguments.abonnes, arguments.admins);
	}

	@Override
	public String getNom() {
		return "Consulter la liste des utilisateurs";
	}
}
