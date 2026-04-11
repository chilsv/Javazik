package vue;

import javax.swing.*;
import java.util.ArrayList;

import controleur.EvenementsConnexion;
import controleur.EvenementsMenu;
import controleur.EvenementsVisite;
import controleur.actions.Action;
import controleur.formulaires.*;
import metier.*;

public class Fenetre implements InterfaceVue {

    /*public void menuPrincipal(){

        FenetreMenu fenetreMenu = new FenetreMenu();

        Evenements.ajouterEvenements(fenetreMenu, choix -> {

            if (choix == 1) {
                System.out.println("Admin");
            }
            else if (choix == 2) {
                System.out.println("Connexion");
            }
            else if (choix == 0) {
                System.out.println("Quitter");
            }

        });
        //return a;
    }*/

    public int menuPrincipal() {
        // Tableau pour capturer le choix depuis le listener asynchrone
        final int[] resultat = {-1};
        final Object verrou = new Object();

        // Lancer Swing sur l'EDT correctement
        SwingUtilities.invokeLater(() -> {
            FenetreMenu fenetreMenu = new FenetreMenu();

            EvenementsMenu.ajouterEvenements(fenetreMenu, choix -> {
                synchronized (verrou) {
                    switch (choix) {
                        case 1:
                            System.out.println("Visiter");
                            break;
                        case 2:
                            System.out.println("Se connecter");
                            break;
                        case 3:
                            System.out.println("S'inscrire");
                            break;
                        case 4:
                            System.out.println("Quitter");
                            break;
                        default:
                            System.out.println("Choix inconnu");
                            break;
                    }
                    resultat[0] = choix;
                    verrou.notify(); // réveille le thread principal
                }
            });
        });

        // Attendre que l'utilisateur fasse un choix
        synchronized (verrou) {
            while (resultat[0] == -1) {
                try {
                    verrou.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        return resultat[0];
    }

    public ConnexionForm demanderConnexion() {
        final ConnexionForm[] resultat = {null};
        final Object verrou = new Object();

        SwingUtilities.invokeLater(() -> {
            FenetreConnexion fenetreConnexion = new FenetreConnexion();

            EvenementsConnexion.ajouterEvenements(fenetreConnexion, choix -> {
                synchronized (verrou) {
                    if (choix == 1) {
                        String mail = fenetreConnexion.getChampMail().getText();
                        String mdp = new String(fenetreConnexion.getChampMdp().getPassword());
                        resultat[0] = new ConnexionForm(mail, mdp);
                    } else if (choix == 2) {
                        System.out.println("Retour au menu");
                    } else {
                        resultat[0] = null;
                    }
                    verrou.notify();
                }
            });
        });

        synchronized (verrou) {
            while (resultat[0] == null) {
                try {
                    verrou.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        return resultat[0];
    }

    //quelle bouton on clique
    public Action choisirAction(String accueil, ArrayList<Action> actions) {
        final Action[] resultat = {null};
        final Object verrou = new Object();

        SwingUtilities.invokeLater(() -> {
            FenetreVisite fenetre = new FenetreVisite();

             EvenementsVisite.ajouterEvenements(fenetre, choix -> {
                synchronized (verrou) {
                    /*
                    if (choix == 1) {
                        resultat[0] = actions.get(0); // Action 1
                    } else if (choix == 2) {
                        resultat[0] = actions.get(1); // Action 2
                    }
                     */
                    verrou.notify();
                }
            });
        });

        return resultat[0];
    }

    public InscriptionForm demanderInscription() {
        final InscriptionForm[] resultat = {null};
        final Object verrou = new Object();

        SwingUtilities.invokeLater(() -> {
            FenetreVisite fenetre = new FenetreVisite();

             EvenementsVisite.ajouterEvenements(fenetre, choix -> {
                synchronized (verrou) {
                    /*
                    if (choix == 1) {
                        resultat[0] = actions.get(0); // Action 1
                    } else if (choix == 2) {
                        resultat[0] = actions.get(1); // Action 2
                    }
                     */
                    verrou.notify();
                }
            });
        });

        return resultat[0];
    }

    //barre de lecture
    public void afficherLecture(Morceau morceau){};

    public void afficherMessage(String message){}; //erreur
    public void afficherErreur(String message){}; //message d'erreur
    public void afficherProfilAbonne(Abonne abonne, Catalogue catalogue){};
    public void afficherProfilAdmin(Admin admin){};

    public RechercheForm demanderRecherche(boolean filtrage){return null;};

    public void afficherRecherche(ResultatRecherche resultat){}; //reuslata rehcerche
    public MorceauForm demanderMorceau(){return null;}; //admin ajouter
    public ArtisteForm demanderArtiste(){return null;}; //admin
    public PlaylistForm demanderPlaylist(int numUtilisateur){return null;}; //abonéé


    public String choisirMorceau(){
        return "0";
    }

    public void afficherUtilisateurs(ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {}

    public void afficherAimer(String nom) {}
}