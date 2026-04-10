package controleur.actions;

import java.util.ArrayList;

import metier.Abonne;
import metier.Admin;
import metier.Catalogue;
import metier.Morceau;
import metier.Personne;
import metier.Playlist;
import vue.InterfaceVue;

public class ActionArguments {
    public InterfaceVue vue;
    public Personne utilisateur;
    public Catalogue catalogue;
    public Morceau morceau;
    public Playlist playlist;
    public ArrayList<Abonne> abonnes;
    public ArrayList<Admin> admins;

    public ActionArguments(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        this.vue = vue;
        this.utilisateur = utilisateur;
        this.catalogue = catalogue;
    }

    public ActionArguments(InterfaceVue vue, Personne utilisateur, Catalogue catalogue, Morceau morceau) {
        this(vue, utilisateur, catalogue);
        this.morceau = morceau;
    }

    public ActionArguments(InterfaceVue vue, Personne utilisateur, Catalogue catalogue, Playlist playlist) {
        this(vue, utilisateur, catalogue);
        this.playlist = playlist;
    }

    public ActionArguments(InterfaceVue vue, Personne utilisateur, Catalogue catalogue, ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {
        this(vue, utilisateur, catalogue);
        this.abonnes = abonnes;
        this.admins = admins;
    }
}