package controleur.actions;

import java.util.ArrayList;

import controleur.formulaires.*;
import metier.Abonne;
import metier.Admin;
import metier.Avis;
import metier.Catalogue;
import metier.Morceau;
import metier.Personne;
import metier.Playlist;
import metier.Filtre;
import vue.InterfaceVue;

public class ActionArguments {
    public InterfaceVue vue;
    public Personne utilisateur;
    public Catalogue catalogue;
    public Morceau morceau;
    public Playlist playlist;
    public ArrayList<Abonne> abonnes;
    public ArrayList<Admin> admins;
    public PlaylistForm playlistForm;
    public RechercheForm rechercheForm;
    public InscriptionForm inscriptionForm;
    public ArtisteForm artisteForm;
    public MorceauForm morceauForm;
    public String nomMorceau;
    public Morceau morceauTrouve;
    public Filtre filtre;
    public String commentaire;
    public int note;
    public ArrayList<Avis> avis;
    public String mail;

    public ActionArguments(InterfaceVue vue, Personne utilisateur, Catalogue catalogue) {
        this.vue = vue;
        this.utilisateur = utilisateur;
        this.catalogue = catalogue;
    }

    public ActionArguments(String mail, Catalogue catalogue) {
        this.mail = mail;
        this.catalogue = catalogue;
    }

    public ActionArguments(InscriptionForm formulaire, Catalogue catalogue, ArrayList<Abonne> abonnes, ArrayList<Admin> admins, Personne utilisateur) {
        this.inscriptionForm = formulaire;
        this.catalogue = catalogue;
        this.abonnes = abonnes;
        this.admins = admins;
        this.utilisateur = utilisateur;
    }

    public ActionArguments(InterfaceVue vue, Personne utilisateur, Catalogue catalogue, ArrayList<Avis> avis) {
        this(vue, utilisateur, catalogue);
        this.avis = avis;
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

    public ActionArguments(Personne utilisateur, Catalogue catalogue, PlaylistForm playlistForm) {
        this(utilisateur, catalogue);
        this.playlistForm = playlistForm;
    }

    public ActionArguments(Personne utilisateur, Catalogue catalogue) {
        this.utilisateur = utilisateur;
        this.catalogue = catalogue;
    }

    public ActionArguments(Catalogue catalogue, ArtisteForm artisteForm) {
        this.catalogue = catalogue;
        this.artisteForm = artisteForm;
    }

    public ActionArguments(Catalogue catalogue, MorceauForm morceauForm) {
        this.catalogue = catalogue;
        this.morceauForm = morceauForm;
    }

    public ActionArguments(Catalogue catalogue, String nomMorceau, Morceau morceauTrouve) {
        this.catalogue = catalogue;
        this.nomMorceau = nomMorceau;
        this.morceauTrouve = morceauTrouve;
    }

    public ActionArguments(InterfaceVue vue, Personne utilisateur, Catalogue catalogue, RechercheForm rechercheForm) {
        this(vue, utilisateur, catalogue);
        this.rechercheForm = rechercheForm;
    }

    public ActionArguments(InterfaceVue vue, Personne utilisateur, Catalogue catalogue, Filtre filtre) {
        this(vue, utilisateur, catalogue);
        this.filtre = filtre;
    }

    public ActionArguments(InterfaceVue vue, Personne utilisateur) {
        this.vue = vue;
        this.utilisateur = utilisateur;
    }

    public ActionArguments(Personne utilisateur, Morceau morceau, String commentaire, int note, ArrayList<Avis> listeAvis) {
        this.utilisateur = utilisateur;
        this.morceau = morceau;
        this.commentaire = commentaire;
        this.note = note;
        this.avis = listeAvis;
    }

    public ActionArguments(Personne utilisateur, Morceau morceau, String commentaire, int note) {
        this.utilisateur = utilisateur;
        this.morceau = morceau;
        this.commentaire = commentaire;
        this.note = note;
    }
}