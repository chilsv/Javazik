package vue;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controleur.EvenementsConnexion;
import controleur.EvenementsInscription;
import controleur.EvenementsMenu;
import controleur.EvenementsVisite;
import controleur.actions.Action;
import controleur.actions.ConsulterLibrairie;
import controleur.actions.ConsulterProfil;
import controleur.formulaires.*;
import metier.*;

public class Fenetre implements InterfaceVue {

    private static final String CARTE_MENU = "menu";
    private static final String CARTE_CONNEXION = "connexion";
    private static final String CARTE_INSCRIPTION = "inscription";
    private static final String CARTE_VISITE = "visite";

    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel cartes;
    private final Map<String, JPanel> cartesParNom;

    public Fenetre() {
        frame = new JFrame(); //Creation
        frame.setSize(1392, 768); //Taille de notre fenetre
        frame.setLocationRelativeTo(null); //mettre au milieu
        frame.setUndecorated(true); //Enlever les bord de base de windows
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cartes = new JPanel(cardLayout);
        cartesParNom = new HashMap<>();

        frame.setContentPane(cartes);
        frame.setVisible(true);
    }

    private void afficherCarte(String nomCarte, JPanel panel) {
        JPanel ancienneCarte = cartesParNom.put(nomCarte, panel);
        if (ancienneCarte != null) {
            cartes.remove(ancienneCarte);
        }
        cartes.add(panel, nomCarte);
        cartes.revalidate();
        cartes.repaint();
        cardLayout.show(cartes, nomCarte);
    }

    private void executerSurEdtEtAttendre(Runnable action) {
        if (SwingUtilities.isEventDispatchThread()) {
            action.run();
            return;
        }
        try {
            SwingUtilities.invokeAndWait(action);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
        executerSurEdtEtAttendre(() -> {
            FenetreMenu fenetreMenu = new FenetreMenu();
            fenetreMenu.setFrame(frame);
            afficherCarte(CARTE_MENU, fenetreMenu.getPanel());

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
        final boolean[] termine = {false};
        final Object verrou = new Object();

        executerSurEdtEtAttendre(() -> {
            FenetreConnexion fenetreConnexion = new FenetreConnexion();
            fenetreConnexion.setFrame(frame);
            afficherCarte(CARTE_CONNEXION, fenetreConnexion.getPanel());

            EvenementsConnexion.ajouterEvenements(fenetreConnexion, choix -> {
                synchronized (verrou) {
                    if (choix == 1) {
                        String mail = fenetreConnexion.getChampMail().getText();
                        String mdp = new String(fenetreConnexion.getChampMdp().getPassword());
                        resultat[0] = new ConnexionForm(mail, mdp);
                        termine[0] = true;
                    } else if (choix == 2) {
                        System.out.println("Retour au menu");
                        termine[0] = true;
                    } else {
                        termine[0] = true;
                    }
                    verrou.notify();
                }
            });
        });

        synchronized (verrou) {
            while (!termine[0]) {
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

        executerSurEdtEtAttendre(() -> {
            FenetreVisite fenetre = new FenetreVisite();
            fenetre.setFrame(frame);
            afficherCarte(CARTE_VISITE, fenetre.getPanel());

            EvenementsVisite.ajouterEvenements(fenetre, choix -> {
                synchronized (verrou) {
                    if (choix == 1) {
                        resultat[0] = new ConsulterProfil();
                    } else if (choix == 2) {
                        resultat[0] = new ConsulterLibrairie();
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

    public InscriptionForm demanderInscription() {
        final InscriptionForm[] resultat = {null};
        final boolean[] termine = {false};
        final Object verrou = new Object();

        executerSurEdtEtAttendre(() -> {
            FenetreInscription fenetre = new FenetreInscription();
            fenetre.setFrame(frame);
            afficherCarte(CARTE_INSCRIPTION, fenetre.getPanel());
            EvenementsInscription.ajouterEvenements(fenetre, choix -> {
            synchronized (verrou) {
                if (choix == 1) {
                    String nom = fenetre.getChampNom().getText();
                    String mail = fenetre.getChampMail().getText();
                    String mdp = new String(fenetre.getChampMdp().getPassword());
                    resultat[0] = new InscriptionForm("abonne", nom, mail, mdp);
                    termine[0] = true;
                } else if (choix == 2) {
                    System.out.println("Retour au menu");
                    termine[0] = true;
                } else {
                    termine[0] = true;
                }
                verrou.notify();
            }
            });
        });

        synchronized (verrou) {
            while (!termine[0]) {
                try {
                    verrou.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

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