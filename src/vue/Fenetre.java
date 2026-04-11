package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controleur.EvenementsConnexion;
import controleur.EvenementsInscription;
import controleur.EvenementsMenu;
import controleur.EvenementsVisite;
import controleur.actions.Action;
import controleur.actions.ChoisirFiltre;
import controleur.actions.ConsulterLibrairie;
import controleur.actions.ConsulterProfil;
import controleur.actions.Deconnexion;
import controleur.actions.Recherche;
import controleur.exceptions.ActionException;
import controleur.formulaires.*;
import metier.*;

public class Fenetre implements InterfaceVue {
    private static final String CARTE_MENU = "menu";
    private static final String CARTE_CONNEXION = "connexion";
    private static final String CARTE_INSCRIPTION = "inscription";
    private static final String CARTE_VISITE = "visite";
    private static final int DUREE_TOAST_MS = 2800;

    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel cartes;
    private final Map<String, JPanel> cartesParNom;
    private FenetreVisite visiteCourante;
    private JWindow toastFenetre;
    private Timer toastTimer;

    public Fenetre() {
        frame = new JFrame("Javazic"); //Creation
        frame.setSize(1392, 768); //Taille de notre fenetre
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("assets/logo.png"));
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
    public Action choisirAction(String accueil, Personne utilisateur) {
        final Action[] resultat = {null};
        final Object verrou = new Object();

        executerSurEdtEtAttendre(() -> {
            FenetreVisite fenetre = new FenetreVisite();
            fenetre.setFrame(frame);
            visiteCourante = fenetre;
            afficherCarte(CARTE_VISITE, fenetre.getPanel());

            boolean filtreVisible = !(utilisateur instanceof Visiteur);
            EvenementsVisite.ajouterEvenements(fenetre, choix -> {
                synchronized (verrou) {
                    if (choix == 1) {
                        resultat[0] = new ConsulterProfil();
                    } else if (choix == 2) {
                        resultat[0] = new ConsulterLibrairie();
                    } else if (choix == 3) {
                        resultat[0] = new Deconnexion();
                    } else if (choix == 4) {
                        resultat[0] = new ChoisirFiltre();
                    } else if (choix == 5) {
                        resultat[0] = new Recherche();
                    } else {
                        resultat[0] = null;
                    }
                    verrou.notify();
                }
            }, filtreVisible);
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

    public void afficherMessage(String message) {
    }

    public void afficherErreur(Exception e) {
        if (e == null) {
            return;
        }
        String message = e.getMessage();
        afficherMessage(e.getMessage());
        String texte = (message == null || message.isBlank()) ? "Une erreur est survenue." : message;

        if (toastTimer != null && toastTimer.isRunning()) {
            toastTimer.stop();
        }
        if (toastFenetre != null) {
            toastFenetre.dispose();
        }

        toastFenetre = new JWindow(frame);
        toastFenetre.setBackground(new Color(0, 0, 0, 0));

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBorder(new EmptyBorder(10, 14, 10, 14));
        contenu.setBackground(new Color(210, 20, 20, 185));

        JLabel label = new JLabel(texte, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        contenu.add(label, BorderLayout.CENTER);

        toastFenetre.setContentPane(contenu);
        toastFenetre.pack();

        Point posFenetre = frame.getLocationOnScreen();
        int x = posFenetre.x + (frame.getWidth() - toastFenetre.getWidth()) / 2;
        int y = posFenetre.y + 28;
        toastFenetre.setLocation(x, y);
        toastFenetre.setAlwaysOnTop(true);
        toastFenetre.setVisible(true);

        toastTimer = new Timer(DUREE_TOAST_MS, evt -> {
            if (toastFenetre != null) {
                toastFenetre.dispose();
                toastFenetre = null;
            }
        });
        toastTimer.setRepeats(false);
        toastTimer.start();
    }
   
    public void afficherProfilAbonne(Abonne abonne, Catalogue catalogue){};
    public void afficherProfilAdmin(Admin admin){};

    public RechercheForm demanderRecherche(Filtre filtre) {
        if (visiteCourante == null) {
            return null;
        }
        return new RechercheForm(visiteCourante.getBarreRecherche().getText(), filtre);
    }

    public Filtre afficherFiltres() {
        if (visiteCourante == null) {
            return null;
        }
        return visiteCourante.getFiltreSelectionne();
    }

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