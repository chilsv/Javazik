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
                if (!abonne.morceauDejaAime(arguments.morceau, arguments.catalogue)) {
                    arguments.vue.afficherAimer(arguments.morceau.getNom());
                    arguments.catalogue.ajouterMorceauPlaylist(arguments.morceau, abonne.getAimes());
                } else {
                    abonne.retirerMorceauPlaylist(arguments.morceau, arguments.catalogue, 0); // 0 parce qu'on retire des morceaux aimés
                }
            } else if (arguments.playlist != null) {
                if (!abonne.playlistDejaSauvegardee(arguments.playlist.getNum())) {
                    arguments.vue.afficherAimer(arguments.playlist.getNom());
                    abonne.ajouterPlaylist(arguments.playlist.getNum());
                } else {
                    abonne.retirerPlaylist(arguments.playlist.getNum());
                }
            }
        }
    }

    @Override
    public String getNom() {
        return "Aimer un morceau ou une playlist";
    }

}
