package controleur.actions;

import java.util.ArrayList;

import controleur.Main;
import controleur.exceptions.UtilisateurDejaCreeException;
import controleur.formulaires.InscriptionForm;
import metier.Abonne;
import metier.Admin;
import metier.Visiteur;

public class AjouterUtilisateur implements Action {
    /*
    * @param arguments catalogue, InscriptionForm, abonnes, admins, utilisateur (juste un pointeur où stocker l'utilisateur créé)
    */
    @Override
    public void executer(ActionArguments arguments) throws UtilisateurDejaCreeException {
        InscriptionForm formulaire = arguments.inscriptionForm;
        String mail = formulaire.mail == null ? "" : formulaire.mail.trim();

        if (mailDejaPris(mail, arguments.abonnes, arguments.admins)) {
            throw new UtilisateurDejaCreeException();
        }

        if (formulaire.num == -1) {
            formulaire.num = arguments.abonnes.size() + arguments.admins.size();
        }

        switch (formulaire.type) {
            case "abonne":
                arguments.utilisateur = new Abonne(formulaire.nom, mail, formulaire.mdp, formulaire.num, arguments.catalogue);
                Main.ajouterAbonne(arguments.utilisateur, arguments.abonnes);
                break;
            case "admin":
                arguments.utilisateur = new Admin(formulaire.nom, mail, formulaire.mdp, formulaire.num);
                Main.ajouterAdmin(arguments.utilisateur, arguments.admins);
                break;
            default:
                arguments.utilisateur = new Visiteur();
                break;
        }
        System.out.println("Utilisateur ajouté : " + arguments.utilisateur.getNom());
    }

    public boolean mailDejaPris(String mail, ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {
        for (Abonne abonne : abonnes) {
            if (abonne.getMail().equalsIgnoreCase(mail)) {
                return true;
            }
        }
        for (Admin admin : admins) {
            if (admin.getMail().equalsIgnoreCase(mail)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getNom() {
        return "Ajouter un utilisateur";
    }

}
