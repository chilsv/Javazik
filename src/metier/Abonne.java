package metier;

import java.util.ArrayList;

import controleur.actions.*;
import controleur.exceptions.PlaylistDejaExistanteException;
import controleur.formulaires.PlaylistForm;
import vue.InterfaceVue;

public class Abonne extends Personne {
    private int playlistDefaut; // Playlist des morceaux aimés créée par défaut
    private ArrayList<Integer> playlists = new ArrayList<Integer>(); // playlists sauvegardées par l'abonné
    private ArrayList<Avis> avis = new ArrayList<Avis>();
    // On liste les actions possibles pour un abonné ici
    private final ArrayList<Action> actions = new ArrayList<Action>();

    public Abonne(String nom, String mail, String mdp, int num, Catalogue catalogue) {
        super(nom, mail, mdp, num);
        // on ajoute à tous une playlist par défaut, générée par l'"utilisateur 0", l'Admin par défaut
        PlaylistForm playlistForm = new PlaylistForm("Morceaux aimés", 0);
        try {
            new AjouterPlaylist().executer(new ActionArguments(this, catalogue, playlistForm));
        } catch (PlaylistDejaExistanteException e) {
        }
        // Actions qu'un abonné peut faire
        actions.add(new Recherche());
        actions.add(new JouerMorceau());
        actions.add(new AjouterPlaylist());
        actions.add(new MettreAvis());
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

    public ArrayList<Integer> getPlaylists() {
        return playlists;
    }

    public int getAimes() {
        return playlistDefaut;
    }

    public void setPlaylistDefaut(int numPlaylist) {
        this.playlistDefaut = numPlaylist;
    }

    public void ajouterPlaylist(int numPlaylist) {
        playlists.add(numPlaylist);
    }

    public boolean retirerMorceauPlaylist(Morceau morceau, Catalogue catalogue, int numPlaylist) {
        if (numPlaylist == playlistDefaut) {
            return false; // on retire pas la playlist par défaut
        }
        catalogue.getPlaylist(numPlaylist).enleverMorceau(morceau);
        return true;
    }

    public void retirerPlaylist(int numPlaylist) {
        if (playlists.contains(numPlaylist)) {
            playlists.remove(Integer.valueOf(numPlaylist));
        }
    }

    public boolean morceauDejaAime(Morceau morceau, Catalogue catalogue) {
        return catalogue.getPlaylist(playlistDefaut).morceauDedans(morceau);
    }

    public boolean playlistDejaSauvegardee(int numPlaylist) {
        return playlists.contains(numPlaylist);
    }

    public void ajouterAvis(Avis avis) {
        this.avis.add(avis);
    }

    public ArrayList<Avis> getAvis() {
        return avis;
    }
}
