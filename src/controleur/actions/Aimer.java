package controleur.actions;

import metier.Abonne;

public class Aimer implements Action {
    /**
     * @param arguments vue, utilisateur, catalogue, morceau/playlist
     */
    @Override
    public void executer(ActionArguments arguments) {
        if (arguments.utilisateur instanceof Abonne) {
            Abonne abonne = (Abonne) arguments.utilisateur;
            if (arguments.morceau != null) {
                arguments.vue.afficherAimer(arguments.morceau.getNom());
                arguments.catalogue.ajouterMorceauPlaylist(arguments.morceau, abonne.getAimes());
            } else if (arguments.playlist != null) {
                arguments.vue.afficherAimer(arguments.playlist.getNom());
                abonne.ajouterPlaylist(arguments.playlist.getNum());
            }
        }
    }

    @Override
    public String getNom() {
        return "Aimer un morceau ou une playlist";
    }

}
