package metier;

import java.util.ArrayList;

import controleur.actions.*;
import vue.InterfaceVue;

public class Abonne extends Personne {
    private int num;
    private ArrayList<Morceau> historique = new ArrayList<Morceau>();
    private int playlistDefaut;
    private ArrayList<Integer> playlists = new ArrayList<Integer>(); // playlists sauvegardées par l'abonné
    // On liste les actions possibles pour un abonné ici
    private final ArrayList<Action> actions = new ArrayList<Action>();

    public Abonne(String nom, String mail, String mdp, int num, Catalogue catalogue) {
        super(nom, mail, mdp);
        this.num = num;
        Playlist defaut = new Playlist("Morceaux aimés", 0);
        // on ajoute à tous une playlist par défaut, générée par l'"utilisateur 0", l'Admin par défaut
        catalogue.ajouterPlaylist(defaut);
        // Actions qu'un abonné peut faire
        actions.add(new Recherche());
        actions.add(new JouerMorceau());
        actions.add(new ConsulterProfil());
        actions.add(new Deconnexion());
        actions.add(new Quitter());
    }

    public String getAccueil(InterfaceVue vue) {
        return "Bienvenue sur la page d'accueil, " + getNom() + " !";
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public ArrayList<Morceau> getHistorique() {
        return historique;
    }

    public void ajouterHistorique(Morceau morceau) {
        historique.add(morceau);
    }

    public ArrayList<Integer> getPlaylists() {
        return playlists;
    }

}
