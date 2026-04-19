package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;

import controleur.EvenementsConnexion;
import controleur.EvenementsInscription;
import controleur.EvenementsMenu;
import controleur.EvenementsVisite;
import controleur.Main;
import controleur.actions.Action;
import controleur.actions.ChoisirFiltre;
import controleur.actions.ConsulterProfil;
import controleur.actions.Deconnexion;
import controleur.actions.Recherche;
import controleur.exceptions.ActionException;
import controleur.formulaires.*;
import metier.*;

public class Fenetre implements InterfaceVue {
    private static final int DUREE_NOTIF = 1500;

    private final JFrame frame; // fenêtre de l'appli
    private final CardLayout cardLayout;
    private final JPanel pages; // le panel princiapal qui va contenir toutes les pages
    private final Map<String, JPanel> pagesMap; // map pour trouver le panel grâcec à son nom
    private FenetreVisite fenetreVisite;
    private Personne utilisateur;
    private Catalogue catalogue;
    private boolean lectureEnPause = false; // pause et play
    private Morceau morceauLecture; // morceau en cours
    private int progressionSecondes = 0; // le temps depuis qu'on a lancé le morceau
    private Timer timerLecture;
    private JProgressBar barreProgression;
    private JLabel labelTempsLecture;
    private boolean dejaAjouteHistorique = false; // pour éviter d'ajouter plusieurs fois le même morceau à l'historique
    private final java.util.Deque<JComponent> historique_fenetre = new java.util.ArrayDeque<>(); //historiique de PAGE

    // couleur meme que les autres jpanel
    final Color BG_PRINCIPAL = new Color(26, 26, 26);
    final Color BG_CARTE = new Color(34, 34, 34);
    final Color BORDER = new Color(50, 50, 50);
    final Color TEXT_BLANC = new Color(255, 255, 255);
    final Color TEXT_GRIS = new Color(160, 160, 160);
    final Color ACCENT = new Color(80, 130, 220);

    // notification pour faire les erreurs "résevré aux abonnés"
    private JWindow notif;
    private Timer tempsNotif;

    // cstes pour les listeners (définies dans EvenementsVisite)
    public static final int CHOIX_PROFIL = 1;
    public static final int CHOIX_PLAYLISTS = 2;
    public static final int CHOIX_RETOUR = 3;
    public static final int CHOIX_FILTRE = 4;
    public static final int CHOIX_LOUPE = 5;
    public static final int CHOIX_ARTISTES = 6;
    public static final int CHOIX_ALBUMS = 7;
    public static final int CHOIX_MORCEAUX = 8;
    public static final int CHOIX_PARCOURIR = 9;
    public static final int CHOIX_POUR_VOUS = 10;
    public static final int CHOIX_POPULAIRE= 11;
    public static final int CHOIX_RADIO = 12;
    public static final int CHOIX_PODCASTS = 13;
    public static final int CHOIX_ADMIN = 14;

    public Fenetre() {
        frame = new JFrame("Javazic"); //Creation
        frame.setSize(1392, 768); //Taille de notre fenetre
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("assets/logo.png"));
        frame.setLocationRelativeTo(null); //mettre au milieu
        frame.setUndecorated(true); //Enlever les bord de base de windows
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        pages = new JPanel(cardLayout);
        pagesMap = new HashMap<>();

        frame.setContentPane(pages);
        frame.setVisible(true);


    }

    /*Pour changer la page qui est affichée */
    private void afficherPanel(String nom, JPanel panel) {
        JPanel ancienPanel = pagesMap.put(nom, panel);
        if (ancienPanel != null) {
            pages.remove(ancienPanel);
        }
        pages.add(panel, nom);
        pages.revalidate(); // d'après la doc
        pages.repaint(); // ça rafraichit l'affichage du panel
        cardLayout.show(pages, nom);
    }

    /*attend que l'utilisateur fasse une action */
    private void executerEtAttendre(Runnable action) {
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
        executerEtAttendre(() -> {
            FenetreMenu fenetreMenu = new FenetreMenu();
            fenetreMenu.setFrame(frame);
            afficherPanel("menu", fenetreMenu.getPanel());

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

        executerEtAttendre(() -> {
            FenetreConnexion fenetreConnexion = new FenetreConnexion();
            fenetreConnexion.setFrame(frame);
            afficherPanel("connexion", fenetreConnexion.getPanel());

            EvenementsConnexion.ajouterEvenements(fenetreConnexion, choix -> {
                synchronized (verrou) {
                    if (choix == 1) {
                        String mail = fenetreConnexion.getChampMail().getText();
                        String mdp = new String(fenetreConnexion.getChampMdp().getPassword());
                        resultat[0] = new ConnexionForm(mail, mdp);
                        termine[0] = true;
                    } else if (choix == 2) {
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

    /*quelle bouton on clique*/
    public Action choisirAction(String accueil, Personne utilisateur) {
        final Action[] resultat = {null};
        final Object verrou = new Object();

        executerEtAttendre(() -> {
            this.utilisateur = utilisateur;
            if (fenetreVisite == null) {
                fenetreVisite = new FenetreVisite(this);
                fenetreVisite.setFrame(frame);
            }
            // On affiche la page de visite et on réinitialise tous les événeemnts
            afficherPanel("visite", fenetreVisite.getPanel());
            fenetreVisite.reinitialiserEvenements();

            historique_fenetre.clear(); // on vide l'historique à chaque nouvelle session
            mettreAJourBoutonRetour();

            JLabel btnAdmin = fenetreVisite.getBtnAdmin();
            if (btnAdmin != null) {
                btnAdmin.setVisible(utilisateur instanceof Admin);
            }

            JLabel btnRetour2 = fenetreVisite.getBtnRetour2();
            if (btnRetour2 != null) {
                btnRetour2.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        if (!historique_fenetre.isEmpty()) {
                            fenetreVisite.setPanelCentral(historique_fenetre.pop());
                            mettreAJourBoutonRetour();
                        }
                    }
                });
            }




            boolean filtreVisible = !(utilisateur instanceof Visiteur); // ça dépend de si c'est un visiteur ou pas
            EvenementsVisite.ajouterEvenements(fenetreVisite, choix -> {
                synchronized (verrou) {
                    if (choix == CHOIX_PROFIL) {
                        if (utilisateur instanceof Visiteur){
                            fenetreVisite.afficherErreur(new ActionException("Vous n'avez pas de compte"));
                        }
                        else {
                            fenetreVisite.viderPanelCentral();
                            if (utilisateur instanceof Abonne||utilisateur instanceof Admin) {
                                naviguerVers(afficherProfilAbonne(utilisateur));// on ne anotifie pas le verrou, on reste sur la page, important, on ne sort pas de choisirAction
                            }
                            return;
                        }
                    } else if (choix == CHOIX_PLAYLISTS) {
                        resultat[0] = new Recherche(new Filtre(false, false, false, true, false, new int[] {0, 0}));
                    } else if (choix == CHOIX_ARTISTES) {
                        resultat[0] = new Recherche(new Filtre(false, true, false, false, false, new int[] {0, 0}));
                    } else if (choix == CHOIX_ALBUMS) {
                        resultat[0] = new Recherche(new Filtre(false, false, true, false, false, new int[] {0, 0}));
                    } else if (choix == CHOIX_MORCEAUX) {
                        resultat[0] = new Recherche(new Filtre(true, false, false, false, false, new int[] {0, 0}));
                    } else if (choix == CHOIX_RETOUR) {
                        fenetreVisite.viderPanelCentral();
                        resultat[0] = new Deconnexion();
                    } else if (choix == CHOIX_FILTRE) {
                        resultat[0] = new ChoisirFiltre();
                    } else if (choix == CHOIX_LOUPE) {
                        resultat[0] = new Recherche();
                    } else if (choix == CHOIX_ADMIN) {
                        if (utilisateur instanceof Admin) {
                            afficherProfilAdmin((Admin) utilisateur);
                        }
                        return; // on reste dans choisirAction, pas de verrou.notify()
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

        executerEtAttendre(() -> {
            FenetreInscription fenetre = new FenetreInscription();
            fenetre.setFrame(frame);
            afficherPanel("inscription", fenetre.getPanel());

            EvenementsInscription.ajouterEvenements(fenetre, choix -> {
            synchronized (verrou) {
                if (choix == 1) {
                    String nom = fenetre.getChampNom().getText();
                    String mail = fenetre.getChampMail().getText();
                    String mdp = new String(fenetre.getChampMdp().getPassword());
                    resultat[0] = new InscriptionForm("abonne", nom, mail, mdp);
                    termine[0] = true;
                } else if (choix == 2) {
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

    /* barre de lecture à faire */
    public void afficherLecture(Morceau morceau) {
        if (fenetreVisite == null) {
            return;
        }

        if (morceauLecture != morceau) {
            progressionSecondes = 0;
            morceauLecture = morceau;
            dejaAjouteHistorique = false; // on remet à false
        }

        // rafraichitr la barre
        executerEtAttendre(() -> {
            JPanel barreLecture = fenetreVisite.getLecture();
            barreLecture.removeAll();
            barreLecture.setLayout(new BorderLayout(24, 0));
            barreLecture.setBorder(new EmptyBorder(10, 16, 10, 16));
            barreLecture.add(lectureGauche(morceau), BorderLayout.WEST);
            barreLecture.add(lectureCentre(morceau), BorderLayout.CENTER);
            barreLecture.add(lectureDroite(morceau), BorderLayout.EAST);
            barreLecture.setVisible(true);
            barreLecture.revalidate();
            barreLecture.repaint();

            mettreAJourAffichageProgression();
            if (lectureEnPause) {
                arreterTimerLecture();
            } else {
                demarrerTimerLecture();
            }

        });
    }

    private JPanel lectureGauche(Morceau morceau) {
        JPanel bloc = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bloc.setOpaque(false);

        JLabel image = new JLabel();
        image.setPreferredSize(new Dimension(48, 48));
        ImageIcon icon = new ImageIcon(morceau.getImage());
        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Image imageRedimensionnee = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            image.setIcon(new ImageIcon(imageRedimensionnee));
        } else {
            image.setIcon(new ImageIcon("assets/logo.png"));
        }
        bloc.add(image);

        JPanel texte = new JPanel();
        texte.setOpaque(false);
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel(morceau.getNom());
        titre.setForeground(TEXT_BLANC);
        titre.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel artistes = new JLabel(formatterArtistes(morceau.getArtistes()));
        artistes.setForeground(TEXT_GRIS);
        artistes.setFont(new Font("SansSerif", Font.PLAIN, 12));

        texte.add(titre);
        texte.add(artistes);
        bloc.add(texte);
        return bloc;
    }

    private JPanel lectureCentre(Morceau morceau) {
        JPanel bloc = new JPanel();
        bloc.setLayout(new BoxLayout(bloc, BoxLayout.Y_AXIS));
        bloc.setOpaque(false);
        bloc.setBorder(new EmptyBorder(0, 10, 0, 10));

        // boutons pause et suivant
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        controles.setOpaque(false);
        controles.add(boutonPause(morceau));
        controles.add(boutonSuivant(morceau));

        barreProgression = new JProgressBar(0, Math.max(1, morceau.getDuree()));
        barreProgression.setStringPainted(false);
        barreProgression.setBorderPainted(false);
        barreProgression.setBackground(new Color(60, 60, 60));
        barreProgression.setForeground(ACCENT);
        barreProgression.setPreferredSize(new Dimension(320, 6));
        barreProgression.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        barreProgression.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel temps = new JPanel(new BorderLayout());
        temps.setOpaque(false);
        labelTempsLecture = new JLabel("0:00");
        labelTempsLecture.setForeground(TEXT_GRIS);
        labelTempsLecture.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JLabel fin = new JLabel(formatterDuree(morceau.getDuree()));
        fin.setForeground(TEXT_GRIS);
        fin.setFont(new Font("SansSerif", Font.PLAIN, 11));
        temps.add(labelTempsLecture, BorderLayout.WEST);
        temps.add(fin, BorderLayout.EAST);

        bloc.add(controles);
        bloc.add(Box.createVerticalStrut(6));
        bloc.add(barreProgression);
        bloc.add(Box.createVerticalStrut(4));
        bloc.add(temps);

        return bloc;
    }

    private JPanel lectureDroite(Morceau morceau) {
        JPanel bloc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bloc.setOpaque(false);
        bloc.setPreferredSize(new Dimension(220, 48));

        ArrayList<Morceau> fileAttente = Main.getFileAttente();
        bloc.add(fileAttenteLabel(fileAttente));
        return bloc;
    }

    private JButton boutonPause(Morceau morceauCourant) {
        JButton bouton = creerBouton(
            lectureEnPause ? "assets/play.png" : "assets/pause.png",
            lectureEnPause ? "Reprendre" : "Pause",
            lectureEnPause ? "Play" : "Pause"
        );
        bouton.setPreferredSize(new Dimension(20, 20));
        bouton.addActionListener(evt -> {
            lectureEnPause = !lectureEnPause;
            afficherLecture(morceauCourant);
        });
        return bouton;
    }

    private void demarrerTimerLecture() {
        if (morceauLecture == null) {
            return;
        }
        if (timerLecture == null) {
            timerLecture = new Timer(1000, evt -> {
                if (lectureEnPause || morceauLecture == null) {
                    return;
                }
                int duree = Math.max(1, morceauLecture.getDuree());
                if (progressionSecondes < duree) {
                    progressionSecondes++;
                    mettreAJourAffichageProgression();
                    ajouterHistorique();
                    return;
                }

                Morceau suivant = Main.retirerFileAttente();
                if (suivant != null) {
                    lectureEnPause = false;
                    afficherLecture(suivant);
                } else {
                    lectureEnPause = true;
                    arreterTimerLecture();
                }
            });
        }
        if (!timerLecture.isRunning()) {
            timerLecture.start();
        }
    }

    /* pour la pause */
    private void arreterTimerLecture() {
        if (timerLecture != null && timerLecture.isRunning()) {
            timerLecture.stop();
        }
    }

    private float mettreAJourAffichageProgression() {
        if (morceauLecture == null) {
            return 0;
        }
        int duree = Math.max(1, morceauLecture.getDuree());
        int valeur = Math.min(progressionSecondes, duree);
        if (barreProgression != null) {
            barreProgression.setMaximum(duree);
            barreProgression.setValue(valeur);
        }
        if (labelTempsLecture != null) {
            labelTempsLecture.setText(formatterDuree(valeur));
        }
        return (float)valeur / (float)duree; // on utilise le temps de lecture pour ajouter à l'historique
    }

    private void ajouterHistorique() {
        if (morceauLecture == null || utilisateur == null || dejaAjouteHistorique) {
            return;
        }
        if (progressionSecondes < 10) {
            return;
        }

        utilisateur.ajouterHistorique(morceauLecture);
        morceauLecture.ajouterEcoute();
        dejaAjouteHistorique = true;
        System.out.println(morceauLecture.getNom() + " ajouté à l'historique");
    }

    private JButton boutonSuivant(Morceau morceauCourant) {
        JButton bouton = creerBouton("assets/suivant.png", "Lire le morceau suivant", ">>");
        bouton.setPreferredSize(new Dimension(20, 20));
        bouton.addActionListener(evt -> {
            Morceau suivant = Main.retirerFileAttente();
            if (suivant != null) {
                lectureEnPause = false;
                afficherLecture(suivant);
            } else {
                afficherMessage("La file d'attente est vide.");
                afficherLecture(morceauCourant);
            }
        });
        return bouton;
    }

    private JButton creerBouton(String chemin, String texte, String texteSecours) {
        JButton bouton = new JButton();
        ImageIcon icon = new ImageIcon(chemin);
        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Image imageRedimensionnee = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            bouton.setIcon(new ImageIcon(imageRedimensionnee));
            bouton.setText(null);
        } else {
            bouton.setIcon(null);
            bouton.setText(texteSecours);
            bouton.setForeground(TEXT_GRIS);
            bouton.setFont(new Font("SansSerif", Font.BOLD, 11));
        }
        bouton.setToolTipText(texte);
        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setContentAreaFilled(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return bouton;
    }

    /* crée le bouton file d'attente mais j'ai pas encore mis la gestion de file d'attente */
    private JLabel fileAttenteLabel(ArrayList<Morceau> fileAttente) {
        ImageIcon icon = new ImageIcon("assets/file_attente.png");
        JLabel label;
        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Image imageRedimensionnee = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(imageRedimensionnee));
        } else {
            label = new JLabel("Queue");
            label.setForeground(TEXT_GRIS);
            label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    public void afficherMessage(String message) {
    }

    /* affiche une notif rouge */
    public void afficherErreur(Exception e) {
        if (e == null) {
            return;
        }
        String message = e.getMessage();
        if (message == null || message.isBlank()) {
            message = ""; // pour éviter ede me retrouver avec un null
        }

        if (tempsNotif != null && tempsNotif.isRunning()) {
            tempsNotif.stop();
        }
        if (notif != null) {
            notif.dispose();
        }

        // ça je te laisse gérer le style
        notif = new JWindow(frame);
        notif.setBackground(new Color(0, 0, 0, 0));

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBorder(new EmptyBorder(10, 14, 10, 14));
        contenu.setBackground(new Color(210, 20, 20, 185));

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        contenu.add(label, BorderLayout.CENTER);

        notif.setContentPane(contenu);
        notif.pack();

        // on affiche la notif en haut
        Point fenetrePosition = frame.getLocationOnScreen(); // ça retourne un point, j'imagine que c'est des coordonénes
        int x = fenetrePosition.x + (frame.getWidth() - notif.getWidth()) / 2;
        int y = fenetrePosition.y + 28;
        notif.setLocation(x, y);
        notif.setAlwaysOnTop(true);
        notif.setVisible(true);

        tempsNotif = new Timer(DUREE_NOTIF, evt -> {
            if (notif != null) {
                notif.dispose();
                notif = null;
            }
        });
        tempsNotif.setRepeats(false);
        tempsNotif.start();
    }
   
    /* peut-être que  */
    public void afficherProfilAbonne(Abonne abonne, Catalogue catalogue){};



    public RechercheForm demanderRecherche(Filtre filtre) {
        return new RechercheForm(fenetreVisite.getBarreRecherche().getText(), filtre);
    }

    public Filtre afficherFiltres() {
        return fenetreVisite.getFiltres();
    }

    public void configurer(Personne utilisateurActuel, Catalogue catalogue) {
        this.utilisateur = utilisateurActuel;
        this.catalogue = catalogue;
    }

    /* true si le morceau est aimé par abonné */
    private boolean estAime(Morceau morceau) {
        if (!(utilisateur instanceof Abonne) || morceau == null) {
            return false;
        }
        Abonne abonne = (Abonne) utilisateur;
        return abonne.morceauDejaAime(morceau, catalogue);
    }

    private void basculerAime(Morceau morceau) {
        if (morceau == null || catalogue == null) {
            return;
        }
        if (!(utilisateur instanceof Abonne)) {
            afficherErreur(new ActionException("Réservé aux abonnés"));
            return;
        }
        Abonne abonne = (Abonne) utilisateur;
        if (!abonne.morceauDejaAime(morceau, catalogue)) {
            
            catalogue.ajouterMorceauPlaylist(morceau, abonne.getAimes());
        } else {
            abonne.retirerMorceauPlaylist(morceau, catalogue, abonne.getAimes());
        }
    }

    private boolean estAimee(Playlist playlist) {
        if (!(utilisateur instanceof Abonne) || playlist == null) {
            return false;
        }
        Abonne abonne = (Abonne) utilisateur;
        return abonne.playlistDejaSauvegardee(playlist.getNum());
    }

    private void basculerAime(Playlist playlist) {
        if (playlist == null) {
            return;
        }
        if (!(utilisateur instanceof Abonne)) {
            afficherErreur(new ActionException("Réservé aux abonnés"));
            return;
        }
        Abonne abonne = (Abonne) utilisateur;
        if (!abonne.playlistDejaSauvegardee(playlist.getNum())) {
            abonne.ajouterPlaylist(playlist.getNum());
        } else {
            abonne.retirerPlaylist(playlist.getNum());
        }
    }

    private static class LigneResultat<TypeObjets> {
        private final TypeObjets objet; // morceau, artiste, album ou playlist
        private final String titre;
        private final String detail;
        private final String duree;
        private final boolean peutAimer;

        private LigneResultat(TypeObjets objet, String titre, String detail, String duree, boolean peutAimer) {
            this.objet = objet;
            if (titre == null) {
                this.titre = "";
            } else {
                this.titre = titre;
            }
            
            if (detail == null) {
                this.detail = "";
            } else {
                this.detail = detail;
            }

             if (duree == null) {
                this.duree = "";
            } else {
                this.duree = duree;
            }

            this.peutAimer = peutAimer;
        }
    }

    /* pose l'image cliquable pour jouer, mettre en file d'attente et aimer */
    private JLabel creerLabelImage(String chemin, String texte, Runnable action, int largeur, int hauteur) {
        ImageIcon icon = new ImageIcon(chemin);
        JLabel label;

        // pour rétrécir l'image
        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Image imageRedimensionnee = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(imageRedimensionnee));
        } else {
            // si ya un pb avec l'image on affiche le texte
            label = new JLabel(texte);
        }

        // quand on passe la souris dessus ça affiche le texte et ça change le curseur
        label.setToolTipText(texte);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Clic sur " + texte);
                action.run();
            }
        });
        return label;
    }

    private JLabel creerVignetteObjet(TypeObjets objet) {
        String chemin = objet.getImage();
        ImageIcon icon = new ImageIcon(chemin);
        JLabel label;
        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Image imageRedimensionnee = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(imageRedimensionnee));
        } else {
            label = new JLabel("img");
        }
        return label;
    }

    /* pour changer l'image coeur quand on clique sur aimer */
    private void mettreIconeAimer(JLabel label, boolean aime) {
        String chemin;
        if (aime) {
            chemin = "assets/aime_true.png";
        } else {
            chemin = "assets/aime_false.png";
        }
        ImageIcon icon = new ImageIcon(chemin);

        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Image imageRedimensionnee = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imageRedimensionnee));
            label.setText(null);
        } else {
            label.setIcon(null);
            label.setText("aimer");
        }
    }

    /* crée visuellement la ligne avec les boutons et infos */
    private <T extends TypeObjets> JPanel creerLigne(LigneResultat<T> ligne, Runnable actionDetails, Runnable actionAimer, boolean afficherAimer, boolean aimeActuel) {
        JPanel lignePanel = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        lignePanel.setOpaque(false);                              // false obligatoire pour que l'arrondi soit visible
        lignePanel.setBackground(new Color(55, 55, 55));          // gris clair dark  ← était (248, 251, 255)
        lignePanel.setBorder(new EmptyBorder(8, 10, 8, 10));      // un peu plus de padding vertical pour centrer
        lignePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        lignePanel.setPreferredSize(new Dimension(100, 76));
        lignePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // bouton play
        JLabel play = creerLabelImage("assets/play.png", "play", () -> jouer(ligne.objet), 22, 22);
        JLabel vignette = creerVignetteObjet(ligne.objet);
        //play.setAlignmentY(Component.CENTER_ALIGNMENT);
        vignette.setAlignmentY(Component.CENTER_ALIGNMENT);
        play.setBounds(20, 20, play.getIcon().getIconWidth(), play.getIcon().getIconHeight());

        JPanel gauche = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        gauche.setOpaque(false);
        gauche.setAlignmentY(Component.CENTER_ALIGNMENT);
        gauche.add(play);
        gauche.add(vignette);

        JPanel texte = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 20));
        texte.setOpaque(false);
        texte.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel principal = new JLabel(ligne.titre);
        principal.setFont(new Font("SansSerif", Font.BOLD, 14));
        principal.setForeground(new Color(230, 230, 230));
        texte.add(principal);

        if (!ligne.detail.isBlank()) {
            JLabel secondaire = new JLabel(ligne.detail);
            secondaire.setFont(new Font("SansSerif", Font.PLAIN, 12));
            secondaire.setForeground(new Color(150, 150, 150));
            texte.add(secondaire);
        }

        if (!ligne.duree.isBlank()) {
            JLabel duree = new JLabel(ligne.duree);
            duree.setFont(new Font("SansSerif", Font.PLAIN, 12));
            duree.setForeground(new Color(130, 130, 130));
            texte.add(duree);
        }

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 18));
        actions.setOpaque(false);
        actions.setAlignmentY(Component.CENTER_ALIGNMENT);
        if (ligne.objet instanceof Morceau) {
            actions.add(creerLabelImage("assets/file_attente.png", "ajouter à la file d'attente", () -> {
                Main.ajouterFileAttente((Morceau) ligne.objet);
                afficherMessage("Ajouté à la file d'attente");
            }, 22, 22));
        }
        if (afficherAimer) {
            final JLabel[] aimerLabelRef = new JLabel[1];
            String chemin;
            if (aimeActuel) {
                chemin = "assets/aime_true.png";
            } else {
                chemin = "assets/aime_false.png";
            }
            aimerLabelRef[0] = creerLabelImage(chemin, "aimer", () -> {
                actionAimer.run();
                if (ligne.objet instanceof Morceau) {
                    mettreIconeAimer(aimerLabelRef[0], estAime((Morceau) ligne.objet));
                } else if (ligne.objet instanceof Playlist) {
                    mettreIconeAimer(aimerLabelRef[0], estAimee((Playlist) ligne.objet));
                }
            }, 22, 22);
            actions.add(aimerLabelRef[0]);
        }
        actions.add(creerLabelImage("assets/details.png", "details", actionDetails, 22, 22));

        lignePanel.add(gauche, BorderLayout.WEST);
        lignePanel.add(texte, BorderLayout.CENTER);
        lignePanel.add(actions, BorderLayout.EAST);


        return lignePanel;
    }

    /* pour bien afficher la durée */
    private String formatterDuree(int secondes) {
        int minutes = Math.max(0, secondes) / 60;
        int reste = Math.max(0, secondes) % 60;
        return String.format("%d:%02d", minutes, reste);
    }

    private Morceau premierMorceau(Album album) {
        if (album == null || album.getMorceaux() == null || album.getMorceaux().isEmpty()) {
            return null;
        }
        return album.getMorceaux().get(0);
    }

    private Morceau premierMorceau(Playlist playlist) {
        if (playlist == null || playlist.getMorceaux() == null || playlist.getMorceaux().isEmpty()) {
            return null;
        }
        return playlist.getMorceaux().get(0);
    }

    private Morceau premierMorceau(Artiste artiste) {
        if (artiste == null || artiste.getAlbums() == null) {
            return null;
        }

        for (Album album : artiste.getAlbums()) {
            Morceau morceau = premierMorceau(album);
            if (morceau != null) {
                return morceau;
            }
        }
        return null;
    }

    private void jouer(TypeObjets objet) {
        if (utilisateur instanceof Visiteur && utilisateur.getHistorique().size() >= 5) {
            fenetreVisite.afficherErreur(new ActionException("Abonnez-vous pour écouter plus"));
            return;
        }

        Morceau morceau = null;
        if (objet instanceof Morceau) {
            morceau = (Morceau) objet;
        } else if (objet instanceof Album) {
            morceau = premierMorceau((Album) objet);
        } else if (objet instanceof Playlist) {
            morceau = premierMorceau((Playlist) objet);
        } else if (objet instanceof Artiste) {
            morceau = premierMorceau((Artiste) objet);
        }

        if (morceau != null) {
            afficherLecture(morceau);
        }
    }

    private void afficherDetails(TypeObjets objet) {
        if (objet == null || fenetreVisite == null) return;
        executerEtAttendre(() -> {
            if (objet instanceof Morceau) {
                naviguerVers(afficherMorceau((Morceau) objet));
            } else if (objet instanceof Album) {
                naviguerVers(afficherAlbum((Album) objet));
            } else if (objet instanceof Playlist) {
                naviguerVers(afficherPlaylist((Playlist) objet));
            } else if (objet instanceof Artiste) {
                naviguerVers(afficherArtiste((Artiste) objet));
            }
        });
    }


    // nouvelle méthode interne qui retourne le panel
    private JComponent afficherProfilAbonne(Personne personne) {
        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(BG_PRINCIPAL);
        contenu.setBorder(new EmptyBorder(20, 24, 24, 24));

        JPanel carte = new JPanel(new BorderLayout(20, 0));
        carte.setBackground(BG_CARTE);
        carte.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel avatar = new JLabel();
        avatar.setPreferredSize(new Dimension(100, 100));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setVerticalAlignment(SwingConstants.CENTER);
        ImageIcon icon = new ImageIcon("assets/profil.png");
        if (icon.getIconWidth() > 0) {
            avatar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        } else {
            avatar.setText("👤");
            avatar.setFont(new Font("SansSerif", Font.PLAIN, 48));
            avatar.setForeground(TEXT_GRIS);
        }

        JPanel infos = new JPanel();
        infos.setOpaque(false);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));

        JLabel nom = new JLabel(personne.getNom());
        nom.setFont(new Font("SansSerif", Font.BOLD, 26));
        nom.setForeground(TEXT_BLANC);
        nom.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mail = new JLabel("✉  " + personne.getMail());
        mail.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mail.setForeground(TEXT_GRIS);
        mail.setBorder(new EmptyBorder(10, 0, 0, 0));
        mail.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mdp = new JLabel("🔒  ••••••••");
        mdp.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mdp.setForeground(TEXT_GRIS);
        mdp.setBorder(new EmptyBorder(6, 0, 0, 0));
        mdp.setAlignmentX(Component.LEFT_ALIGNMENT);

        infos.add(nom);
        infos.add(mail);
        infos.add(mdp);

        // infos spécifiques selon le type
        if (personne instanceof Abonne) {
            Abonne abonne = (Abonne) personne;
            int nbPlaylists = abonne.getPlaylists() != null ? abonne.getPlaylists().size() : 0;
            int nbAvis = abonne.getAvis() != null ? abonne.getAvis().size() : 0;

            JLabel stats = new JLabel(nbPlaylists + " playlist(s)  ·  " + nbAvis + " avis rédigé(s)");
            stats.setFont(new Font("SansSerif", Font.PLAIN, 13));
            stats.setForeground(TEXT_GRIS);
            stats.setBorder(new EmptyBorder(10, 0, 0, 0));
            stats.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel typeLabel = new JLabel("Abonné");
            typeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            typeLabel.setForeground(ACCENT);
            typeLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
            typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            infos.add(stats);
            infos.add(typeLabel);

        } else if (personne instanceof Admin) {
            JLabel typeLabel = new JLabel("Administrateur");
            typeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            typeLabel.setForeground(ACCENT);
            typeLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
            typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            infos.add(typeLabel);
        }

        carte.add(avatar, BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);
        contenu.add(carte, BorderLayout.NORTH);
        return contenu;
    }

    //afficher album dans la recerche
    public JComponent afficherAlbum(Album album) {
        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(BG_PRINCIPAL);
        contenu.setBorder(new EmptyBorder(20, 24, 24, 24));

        // panel pour le texte, o,n mettra dedans le nom le nombre et tt
        JPanel carte = new JPanel(new BorderLayout(20, 0));
        carte.setBackground(BG_CARTE);
        carte.setBorder(new EmptyBorder(18, 18, 18, 18));

        // Image
        JLabel image = new JLabel();
        image.setPreferredSize(new Dimension(160, 160));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setForeground(TEXT_GRIS);
        ImageIcon icon = new ImageIcon(album.getImage() != null ? album.getImage() : "");
        if (icon.getIconWidth() > 0) {
            image.setIcon(new ImageIcon(icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
        } else {
            image.setText("Aucune image");
        }

        // Infos texte
        JPanel infos = new JPanel();
        infos.setOpaque(false);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));

        //reprednre la meme typos
        JLabel nom = new JLabel(album.getNom());
        nom.setFont(new Font("SansSerif", Font.BOLD, 26));
        nom.setForeground(TEXT_BLANC);
        nom.setAlignmentX(Component.LEFT_ALIGNMENT);

        //nom artiste
        String nomArtiste = album.getArtiste() != null ? album.getArtiste().getNom() : "Inconnu";
        JLabel artiste = new JLabel("Artiste : " + nomArtiste);
        artiste.setFont(new Font("SansSerif", Font.PLAIN, 14));
        artiste.setForeground(TEXT_GRIS);
        artiste.setBorder(new EmptyBorder(10, 0, 0, 0));
        artiste.setAlignmentX(Component.LEFT_ALIGNMENT);

        //annee
        String anneeAffichee = album.getAnnee() > 0 ? String.valueOf(album.getAnnee()) : "Inconnue";
        JLabel annee = new JLabel("Année : " + anneeAffichee);
        annee.setFont(new Font("SansSerif", Font.PLAIN, 14));
        annee.setForeground(TEXT_GRIS);
        annee.setBorder(new EmptyBorder(6, 0, 0, 0));
        annee.setAlignmentX(Component.LEFT_ALIGNMENT);

        //utiliser la arraylist pour avoir afficher la liste des albums ou dedans y'a la liste des morceaux des albums
        ArrayList<Morceau> morceaux = album.getMorceaux() != null ? album.getMorceaux() : new ArrayList<>();
        int dureeTotal = 0;
        for (Morceau m : morceaux) dureeTotal += m.getDuree();

        //creer les contener
        JLabel nbMorceaux = new JLabel(morceaux.size() + " morceau(x)  ·  " + formatterDuree(dureeTotal));
        nbMorceaux.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nbMorceaux.setForeground(TEXT_GRIS);
        nbMorceaux.setBorder(new EmptyBorder(6, 0, 0, 0));
        nbMorceaux.setAlignmentX(Component.LEFT_ALIGNMENT);

        //on met tout forme
        infos.add(nom);
        infos.add(artiste);
        infos.add(annee);
        infos.add(nbMorceaux);
        carte.add(image, BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);

        // liste des morceaux
        JLabel titreListe = new JLabel("Morceaux");
        titreListe.setFont(new Font("SansSerif", Font.BOLD, 18));
        titreListe.setForeground(TEXT_BLANC);
        titreListe.setBorder(new EmptyBorder(20, 0, 10, 0));
        JComponent listeMorceaux = creerVueMorceaux(morceaux);
        JPanel bas = new JPanel(new BorderLayout());
        bas.setOpaque(false);
        bas.add(titreListe, BorderLayout.NORTH);
        bas.add(listeMorceaux, BorderLayout.CENTER);
        contenu.add(carte, BorderLayout.NORTH);
        contenu.add(bas, BorderLayout.CENTER);
        return contenu;
    }



    //afficher les playlist
    public JComponent afficherPlaylist(Playlist playlist) {
        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(BG_PRINCIPAL);
        contenu.setBorder(new EmptyBorder(20, 24, 24, 24));

        //meme systeme que precedement
        JPanel carte = new JPanel(new BorderLayout(20, 0));
        carte.setBackground(BG_CARTE);
        carte.setBorder(new EmptyBorder(18, 18, 18, 18));

        //iamge
        JLabel image = new JLabel();
        image.setPreferredSize(new Dimension(160, 160));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setForeground(TEXT_GRIS);
        ImageIcon icon = new ImageIcon(playlist.getImage() != null ? playlist.getImage() : "");
        if (icon.getIconWidth() > 0) {
            image.setIcon(new ImageIcon(icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
        } else {
            image.setText("Aucune image");
        }

        // Infos texte
        JPanel infos = new JPanel();
        infos.setOpaque(false);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        JLabel nom = new JLabel(playlist.getNom());
        nom.setFont(new Font("SansSerif", Font.BOLD, 26));
        nom.setForeground(TEXT_BLANC);
        nom.setAlignmentX(Component.LEFT_ALIGNMENT);

        //utiliser l'arraylist poru recup les morceau des playlist (meme focntionement que albums)
        ArrayList<Morceau> morceaux = playlist.getMorceaux() != null ? playlist.getMorceaux() : new ArrayList<>();
        int dureeTotal = 0;
        for (Morceau m : morceaux) dureeTotal += m.getDuree();

        //meme typos
        JLabel nbMorceaux = new JLabel(morceaux.size() + " morceau(x)  ·  " + formatterDuree(dureeTotal));
        nbMorceaux.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nbMorceaux.setForeground(TEXT_GRIS);
        nbMorceaux.setBorder(new EmptyBorder(10, 0, 0, 0));
        nbMorceaux.setAlignmentX(Component.LEFT_ALIGNMENT);
        String dateStr = playlist.getCreation() != null ? formatterDateAvis(playlist.getCreation()) : "Inconnue";
        JLabel dateCreation = new JLabel("Créée le : " + dateStr);
        dateCreation.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateCreation.setForeground(TEXT_GRIS);
        dateCreation.setBorder(new EmptyBorder(6, 0, 0, 0));
        dateCreation.setAlignmentX(Component.LEFT_ALIGNMENT);

        //assemblage
        infos.add(nom);
        infos.add(nbMorceaux);
        infos.add(dateCreation);
        carte.add(image, BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);

        //liste des morceaux
        JLabel titreListe = new JLabel("Morceaux");
        titreListe.setFont(new Font("SansSerif", Font.BOLD, 18));
        titreListe.setForeground(TEXT_BLANC);
        titreListe.setBorder(new EmptyBorder(20, 0, 10, 0));
        JComponent listeMorceaux = creerVueMorceaux(morceaux);
        JPanel bas = new JPanel(new BorderLayout());
        bas.setOpaque(false);
        bas.add(titreListe, BorderLayout.NORTH);
        bas.add(listeMorceaux, BorderLayout.CENTER);
        contenu.add(carte, BorderLayout.NORTH);
        contenu.add(bas, BorderLayout.CENTER);
        return contenu;
    }


    //afficher Artiste
    public JComponent afficherArtiste(Artiste artiste) {
        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(BG_PRINCIPAL);
        contenu.setBorder(new EmptyBorder(20, 24, 24, 24));

        // texte
        JPanel carte = new JPanel(new BorderLayout(20, 0));
        carte.setBackground(BG_CARTE);
        carte.setBorder(new EmptyBorder(18, 18, 18, 18));

        // photo de l'artiste (phot de base)
        JLabel image = new JLabel();
        image.setPreferredSize(new Dimension(160, 160));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setForeground(TEXT_GRIS);
        ImageIcon icon = new ImageIcon(artiste.getImage() != null ? artiste.getImage() : "");
        if (icon.getIconWidth() > 0) {
            image.setIcon(new ImageIcon(icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
        } else {
            image.setText("Aucune image");
        }

        // Infos texte
        JPanel infos = new JPanel();
        infos.setOpaque(false);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        JLabel nom = new JLabel(artiste.getNom());
        nom.setFont(new Font("SansSerif", Font.BOLD, 26));
        nom.setForeground(TEXT_BLANC);
        nom.setAlignmentX(Component.LEFT_ALIGNMENT);

        //meme utilsiation de l'arraylist
        ArrayList<Morceau> tousLesMorceaux = artiste.getMorceaux() != null ? artiste.getMorceaux() : new ArrayList<>();
        ArrayList<Album> albums = artiste.getAlbums() != null ? artiste.getAlbums() : new ArrayList<>();
        int dureeTotal = 0;
        for (Morceau m : tousLesMorceaux) dureeTotal += m.getDuree();

        //affichage du nombre de morceaux et tt
        JLabel nbMorceaux = new JLabel(tousLesMorceaux.size() + " morceau(x)  ·  " + albums.size() + " album(s)  ·  " + formatterDuree(dureeTotal));
        nbMorceaux.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nbMorceaux.setForeground(TEXT_GRIS);
        nbMorceaux.setBorder(new EmptyBorder(10, 0, 0, 0));
        nbMorceaux.setAlignmentX(Component.LEFT_ALIGNMENT);

        //assembalge
        infos.add(nom);
        infos.add(nbMorceaux);
        carte.add(image, BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);
        CardLayout ongletLayout = new CardLayout();
        JPanel ongletPanel = new JPanel(ongletLayout);
        ongletPanel.setBackground(BG_PRINCIPAL);

        //lien entre les afficher
        JButton btnAlbums = creerBoutonCategorie("Albums (" + albums.size() + ")", ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
        JButton btnMorceaux =creerBoutonCategorie("Morceaux (" + tousLesMorceaux.size() + ")", ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
        JPanel barreOnglets= new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barreOnglets.setOpaque(false);
        barreOnglets.setBorder(new EmptyBorder(16, 0, 10, 0));
        barreOnglets.add(btnAlbums);
        barreOnglets.add(btnMorceaux);

        // Vue albums de l'artiste (réutilise creerVueAlbums)
        JComponent vueAlbums = creerVueAlbums(albums);
        JComponent vueMorceaux = creerVueMorceaux(tousLesMorceaux);
        ongletPanel.add(vueAlbums,"albums");
        ongletPanel.add(vueMorceaux,"morceaux");
        JButton[] btns   = { btnAlbums, btnMorceaux };
        String[]  cartes = { "albums",  "morceaux"  };
        for (int i = 0; i < btns.length; i++) {
            final int idx = i;
            btns[i].addActionListener(evt -> {
                ongletLayout.show(ongletPanel, cartes[idx]);
                for (int j = 0; j < btns.length; j++) {appliquerStyleCategorieActive(btns[j], j == idx, ACCENT, BG_CARTE, TEXT_GRIS, BORDER);}
            });
        }
        // Onglet Albums actif par défaut
        ongletLayout.show(ongletPanel, "albums");
        appliquerStyleCategorieActive(btnAlbums,true, ACCENT,BG_CARTE,TEXT_GRIS,BORDER);
        appliquerStyleCategorieActive(btnMorceaux, false,ACCENT,BG_CARTE,TEXT_GRIS,BORDER);

        JPanel bas = new JPanel(new BorderLayout());
        bas.setOpaque(false);
        bas.add(barreOnglets,BorderLayout.NORTH);
        bas.add(ongletPanel,BorderLayout.CENTER);

        contenu.add(carte,BorderLayout.NORTH);
        contenu.add(bas,BorderLayout.CENTER);
        return contenu;
    }

    public JComponent afficherMorceau(Morceau morceau) {
        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(BG_PRINCIPAL);
        contenu.setBorder(new EmptyBorder(20, 24, 24, 24));

        JPanel carte = new JPanel(new BorderLayout(20, 0));
        carte.setBackground(BG_CARTE);
        carte.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel image = new JLabel();
        image.setPreferredSize(new Dimension(180, 180));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setVerticalAlignment(SwingConstants.CENTER);
        image.setForeground(TEXT_GRIS);

        ImageIcon icon = new ImageIcon(morceau.getImage());
        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Image imageRedimensionnee = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            image.setIcon(new ImageIcon(imageRedimensionnee));
            image.setText(null);
        } else {
            image.setText("Aucune image");
        }

        JPanel infos = new JPanel();
        infos.setOpaque(false);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));

        JLabel nom = new JLabel(morceau.getNom());
        nom.setFont(new Font("SansSerif", Font.BOLD, 28));
        nom.setForeground(TEXT_BLANC);
        nom.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel artistes = new JLabel("Artistes : " + formatterArtistes(morceau.getArtistes()));
        artistes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        artistes.setForeground(TEXT_GRIS);
        artistes.setBorder(new EmptyBorder(10, 0, 0, 0));
        artistes.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel duree = new JLabel("Durée : " + formatterDuree(morceau.getDuree()));
        duree.setFont(new Font("SansSerif", Font.PLAIN, 14));
        duree.setForeground(TEXT_GRIS);
        duree.setBorder(new EmptyBorder(6, 0, 0, 0));
        duree.setAlignmentX(Component.LEFT_ALIGNMENT);

        String anneeAffichee;
        if (morceau.getAnnee() > 0) {
            anneeAffichee = Integer.toString(morceau.getAnnee());
        } else {
            anneeAffichee = "Inconnue";
        }
        JLabel annee = new JLabel("Année : " + anneeAffichee);
        annee.setFont(new Font("SansSerif", Font.PLAIN, 14));
        annee.setForeground(TEXT_GRIS);
        annee.setBorder(new EmptyBorder(6, 0, 0, 0));
        annee.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel jouer = new JLabel(new ImageIcon("assets/play.png"));
        ImageIcon playIcon = new ImageIcon("assets/play.png");
        Image jouerRedimensionnee = playIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        jouer.setIcon(new ImageIcon(jouerRedimensionnee));
        jouer.setFont(new Font("SansSerif", Font.BOLD, 13));
        jouer.setForeground(Color.WHITE);
        jouer.setBackground(ACCENT);
        jouer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jouer.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 14));
        jouer.setAlignmentX(Component.LEFT_ALIGNMENT);

        infos.add(nom);
        infos.add(artistes);
        infos.add(duree);
        infos.add(annee);
        infos.add(Box.createVerticalStrut(10));
        infos.add(jouer);

        carte.add(image, BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);

        contenu.add(carte, BorderLayout.CENTER);
        contenu.add(sectionAvis(morceau), BorderLayout.SOUTH);
        return contenu;
    }


    // faire la section avis
    private JComponent sectionAvis(Morceau morceau) {
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);
        section.setBorder(new EmptyBorder(16, 0, 0, 0));

        JPanel enTeteAvis = new JPanel(new BorderLayout());
        enTeteAvis.setOpaque(false);

        JLabel titreAvis = new JLabel("Avis des abonnés");
        titreAvis.setFont(new Font("SansSerif", Font.BOLD, 18));
        titreAvis.setForeground(TEXT_BLANC);
        titreAvis.setBorder(new EmptyBorder(0, 0, 10, 0));
        enTeteAvis.add(titreAvis, BorderLayout.WEST);

        // formulaire de commentaire, recuperer clavier
        JPanel formulaire = new JPanel(new BorderLayout(8, 0));
        formulaire.setOpaque(false);
        formulaire.setBorder(new EmptyBorder(0, 0, 12, 0));

        JTextField champCommentaire = new JTextField();
        champCommentaire.setBackground(new Color(45, 45, 45));
        champCommentaire.setForeground(TEXT_BLANC);
        champCommentaire.setCaretColor(TEXT_BLANC);
        champCommentaire.setFont(new Font("SansSerif", Font.PLAIN, 13));
        champCommentaire.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        champCommentaire.putClientProperty("JTextField.placeholderText", "Votre commentaire...");

        // Sélecteur de note 1-5
        JPanel panelNote = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        panelNote.setOpaque(false);
        JLabel labelNote = new JLabel("Note :");
        labelNote.setForeground(TEXT_GRIS);
        labelNote.setFont(new Font("SansSerif", Font.PLAIN, 13));
        SpinnerNumberModel modeleNote = new SpinnerNumberModel(5, 1, 5, 1);
        JSpinner spinnerNote = new JSpinner(modeleNote);
        spinnerNote.setBackground(new Color(45, 45, 45));
        spinnerNote.setForeground(TEXT_BLANC);
        spinnerNote.setFont(new Font("SansSerif", Font.PLAIN, 13));
        spinnerNote.setPreferredSize(new Dimension(55, 28));
        ((JSpinner.DefaultEditor) spinnerNote.getEditor()).getTextField().setBackground(new Color(45, 45, 45));
        ((JSpinner.DefaultEditor) spinnerNote.getEditor()).getTextField().setForeground(TEXT_BLANC);
        panelNote.add(labelNote);
        panelNote.add(spinnerNote);

        JButton boutonEnvoyer = new JButton("Envoyer");
        boutonEnvoyer.setFont(new Font("SansSerif", Font.BOLD, 12));
        boutonEnvoyer.setForeground(TEXT_BLANC);
        boutonEnvoyer.setBackground(ACCENT);
        boutonEnvoyer.setFocusPainted(false);
        boutonEnvoyer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonEnvoyer.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));

        JPanel droiteFormulaire = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        droiteFormulaire.setOpaque(false);
        droiteFormulaire.add(panelNote);
        droiteFormulaire.add(boutonEnvoyer);

        formulaire.add(champCommentaire, BorderLayout.CENTER);
        formulaire.add(droiteFormulaire, BorderLayout.EAST);

        // liste avis
        JPanel listeAvis = new JPanel();
        listeAvis.setLayout(new BoxLayout(listeAvis, BoxLayout.Y_AXIS));
        listeAvis.setBackground(BG_CARTE);
        listeAvis.setBorder(new EmptyBorder(10, 10, 10, 10));

        // méthode locale pour rafraîchir la liste
        Runnable rafraichirListe = () -> {
            listeAvis.removeAll();
            ArrayList<Avis> avisMorceau = morceau.getAvis();
            if (avisMorceau == null || avisMorceau.isEmpty()) {
                JLabel vide = new JLabel("Soyez le premier à commenter !");
                vide.setForeground(TEXT_GRIS);
                vide.setFont(new Font("SansSerif", Font.PLAIN, 13));
                vide.setAlignmentX(Component.LEFT_ALIGNMENT);
                listeAvis.add(vide);
            } else {
                for (Avis avis : avisMorceau) {
                    JPanel carteAvis = new JPanel();
                    carteAvis.setLayout(new BoxLayout(carteAvis, BoxLayout.Y_AXIS));
                    carteAvis.setAlignmentX(Component.LEFT_ALIGNMENT);
                    carteAvis.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
                    carteAvis.setBackground(new Color(45, 45, 45));
                    carteAvis.setBorder(new EmptyBorder(10, 10, 10, 10));

                    JLabel lblUtilisateur = new JLabel(avis.getAbonne().getNom());
                    lblUtilisateur.setForeground(TEXT_BLANC);
                    lblUtilisateur.setFont(new Font("SansSerif", Font.BOLD, 13));
                    lblUtilisateur.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel lblDate = new JLabel("ajouté le " + formatterDateAvis(avis.getDate()));
                    lblDate.setForeground(TEXT_GRIS);
                    lblDate.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    lblDate.setBorder(new EmptyBorder(4, 0, 0, 0));
                    lblDate.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel lblCommentaire = new JLabel("\" " + avis.getCommentaire() + " \"");
                    lblCommentaire.setForeground(TEXT_BLANC);
                    lblCommentaire.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    lblCommentaire.setBorder(new EmptyBorder(6, 0, 0, 0));
                    lblCommentaire.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel lblNote = new JLabel("Note : " + avis.getNote() + "/5");
                    lblNote.setForeground(ACCENT);
                    lblNote.setFont(new Font("SansSerif", Font.BOLD, 13));
                    lblNote.setBorder(new EmptyBorder(6, 0, 0, 0));
                    lblNote.setAlignmentX(Component.LEFT_ALIGNMENT);

                    carteAvis.add(lblUtilisateur);
                    carteAvis.add(lblDate);
                    carteAvis.add(lblCommentaire);
                    carteAvis.add(lblNote);
                    listeAvis.add(carteAvis);
                    listeAvis.add(Box.createVerticalStrut(8));
                }
            }
            listeAvis.revalidate();
            listeAvis.repaint();
        };

        // Remplissage initial
        rafraichirListe.run();

        // Listener du bouton envoyer
        boutonEnvoyer.addActionListener(evt -> {
            if (!(utilisateur instanceof Abonne)) {
                afficherErreur(new ActionException("Réservé aux abonnés"));
                return;
            }
            String texte = champCommentaire.getText().trim();
            if (texte.isEmpty()) {
                afficherErreur(new ActionException("Le commentaire ne peut pas être vide"));
                return;
            }
            int note = (int) spinnerNote.getValue();
            Avis nouvelAvis = new Avis(morceau, (Abonne) utilisateur, note, texte);
            morceau.getAvis().add(nouvelAvis);
            ((Abonne) utilisateur).ajouterAvis(nouvelAvis);
            champCommentaire.setText("");
            spinnerNote.setValue(5);
            rafraichirListe.run();
        });

        JScrollPane scrollAvis = new JScrollPane(listeAvis);
        scrollAvis.setBorder(BorderFactory.createEmptyBorder());
        scrollAvis.getViewport().setBackground(BG_CARTE);
        scrollAvis.setPreferredSize(new Dimension(100, 200));

        section.add(enTeteAvis, BorderLayout.NORTH);
        section.add(formulaire, BorderLayout.CENTER);
        section.add(scrollAvis, BorderLayout.SOUTH);
        return section;
    }

    //afficher le fond de base de admin qui permet de supprimer/ajouter des morceaux/artiste/abonne/playslist

    public void afficherProfilAdmin(Admin admin) {
        if (fenetreVisite == null) return;
        executerEtAttendre(() -> {
            JPanel contenu = new JPanel(new BorderLayout());
            contenu.setBackground(BG_PRINCIPAL);
            contenu.setBorder(new EmptyBorder(20, 24, 24, 24));

            // scindée en deux l'ecran pour gestion utilisateur a gaiche et gestion morceau/artiste/Playlist a dorite
            JPanel gestion = new JPanel(new GridLayout(1, 2, 16, 0));
            gestion.setOpaque(false);
            gestion.setBorder(new EmptyBorder(20, 0, 0, 0));

            // colonne gauche gestion des abonnés
            JPanel colonneAbonnes = new JPanel();
            colonneAbonnes.setLayout(new BoxLayout(colonneAbonnes, BoxLayout.Y_AXIS));
            colonneAbonnes.setBackground(BG_CARTE);
            colonneAbonnes.setBorder(new EmptyBorder(16, 16, 16, 16));
            JLabel titreAbonnes = new JLabel("Gestion des abonnés");
            titreAbonnes.setFont(new Font("SansSerif", Font.BOLD, 16));
            titreAbonnes.setForeground(TEXT_BLANC);
            titreAbonnes.setAlignmentX(Component.LEFT_ALIGNMENT);
            titreAbonnes.setBorder(new EmptyBorder(0, 0, 14, 0));

            // Parti champs ajouter abonné
            JLabel labelAjoutAbonne = new JLabel("Ajouter un abonné");
            labelAjoutAbonne.setFont(new Font("SansSerif", Font.BOLD, 13));
            labelAjoutAbonne.setForeground(ACCENT);
            labelAjoutAbonne.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelAjoutAbonne.setBorder(new EmptyBorder(0, 0, 6, 0));
            JTextField champNomAjout = creerChampTexte("Nom");
            ajouterPlaceholder(champNomAjout, "Nom");
            JTextField champMailAjout = creerChampTexte("Mail");
            ajouterPlaceholder(champMailAjout, "Mail");
            JTextField champMdpAjout = creerChampTexte("Password");
            ajouterPlaceholder(champMdpAjout, "Mot de passe");


            JButton btnAjouterAbonne = creerBoutonAdmin("Ajouter", ACCENT);
            btnAjouterAbonne.addActionListener(evt -> {
                String nomVal = champNomAjout.getText().trim();
                String mailVal = champMailAjout.getText().trim();
                String mdpVal = new String(champMdpAjout.getText()).trim();
                if (nomVal.isEmpty() || mailVal.isEmpty() || mdpVal.isEmpty()) {
                    fenetreVisite.afficherErreur(new ActionException("Tous les champs sont requis"));
                    return;
                }
                try {
                    InscriptionForm form = new InscriptionForm("abonne", nomVal, mailVal, mdpVal);
                    //new AjouterAbonne().executer(new ActionArguments(this, utilisateur, catalogueActuel, form));
                    champNomAjout.setText("");
                    champMailAjout.setText("");
                    champMdpAjout.setText("");
                    fenetreVisite.afficherErreur(new ActionException("Abonné ajouter avec succés"));
                } catch (Exception ex) {
                    afficherErreur(ex);
                }
            });

            // Séparateur
            JSeparator sep1 = new JSeparator();
            sep1.setForeground(BORDER);
            sep1.setBackground(BORDER);
            sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            sep1.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Champs supprimer abonné
            JLabel labelSupprAbonne = new JLabel("Supprimer un abonné");
            labelSupprAbonne.setFont(new Font("SansSerif", Font.BOLD, 13));
            labelSupprAbonne.setForeground(new Color(200, 60, 60));
            labelSupprAbonne.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelSupprAbonne.setBorder(new EmptyBorder(10, 0, 6, 0));
            JTextField champMailSuppr = creerChampTexte("Mail");
            ajouterPlaceholder(champMailSuppr, "Mail");
            JButton btnSupprimerAbonne = creerBoutonAdmin("Supprimer", new Color(180, 40, 40));
            btnSupprimerAbonne.addActionListener(evt -> {
                String mailVal = champMailSuppr.getText().trim();
                if (mailVal.isEmpty()) {
                    fenetreVisite.afficherErreur(new ActionException("Mail requis"));
                    return;
                }
                try {
                    //new SupprimerAbonne().executer(new ActionArguments(this, utilisateur, catalogueActuel,nomVal, mailVal));
                    champMailSuppr.setText("");
                    fenetreVisite.afficherErreur(new ActionException("Abonné supprimé avec succés"));
                } catch (Exception ex) {
                    afficherErreur(ex);
                }
            });

            colonneAbonnes.add(titreAbonnes);
            colonneAbonnes.add(labelAjoutAbonne);
            colonneAbonnes.add(champNomAjout);
            colonneAbonnes.add(Box.createVerticalStrut(6));
            colonneAbonnes.add(champMailAjout);
            colonneAbonnes.add(Box.createVerticalStrut(6));
            colonneAbonnes.add(champMdpAjout);
            colonneAbonnes.add(Box.createVerticalStrut(8));
            colonneAbonnes.add(btnAjouterAbonne);
            colonneAbonnes.add(Box.createVerticalStrut(14));
            colonneAbonnes.add(sep1);
            colonneAbonnes.add(labelSupprAbonne);
            colonneAbonnes.add(Box.createVerticalStrut(6));
            colonneAbonnes.add(champMailSuppr);
            colonneAbonnes.add(Box.createVerticalStrut(8));
            colonneAbonnes.add(btnSupprimerAbonne);
            colonneAbonnes.add(Box.createVerticalGlue());

            // colonne droite gestion du catalogue
            JPanel colonneCatalogue = new JPanel();
            colonneCatalogue.setLayout(new BoxLayout(colonneCatalogue, BoxLayout.Y_AXIS));
            colonneCatalogue.setBackground(BG_CARTE);
            colonneCatalogue.setBorder(new EmptyBorder(16, 16, 16, 16));
            JLabel titreCatalogue = new JLabel("Gestion du catalogue");
            titreCatalogue.setFont(new Font("SansSerif", Font.BOLD, 16));
            titreCatalogue.setForeground(TEXT_BLANC);
            titreCatalogue.setAlignmentX(Component.LEFT_ALIGNMENT);
            titreCatalogue.setBorder(new EmptyBorder(0, 0, 14, 0));

            // Onglets Morceau/artist/playimist
            CardLayout ongletCatalogueLayout = new CardLayout();
            JPanel ongletCatalogue = new JPanel(ongletCatalogueLayout);
            ongletCatalogue.setOpaque(false);
            ongletCatalogue.setAlignmentX(Component.LEFT_ALIGNMENT);

            JButton btnOngletMorceau  = creerBoutonCategorie("Morceau",  ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
            JButton btnOngletArtiste  = creerBoutonCategorie("Artiste",  ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
            JButton btnOngletPlaylist  = creerBoutonCategorie("Playlist",  ACCENT, BG_CARTE, TEXT_GRIS, BORDER);

            JPanel barreCatalogue = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            barreCatalogue.setOpaque(false);
            barreCatalogue.setAlignmentX(Component.LEFT_ALIGNMENT);
            barreCatalogue.add(btnOngletMorceau);
            barreCatalogue.add(btnOngletArtiste);
            barreCatalogue.add(btnOngletPlaylist);
            barreCatalogue.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            // Vue pour ajouter morceau
            JPanel vueMorceau = new JPanel();
            vueMorceau.setLayout(new BoxLayout(vueMorceau, BoxLayout.Y_AXIS));
            vueMorceau.setOpaque(false);

            JLabel labelAjoutMorceau = new JLabel("Ajouter un morceau");
            labelAjoutMorceau.setFont(new Font("SansSerif", Font.BOLD, 13));
            labelAjoutMorceau.setForeground(ACCENT);
            labelAjoutMorceau.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelAjoutMorceau.setBorder(new EmptyBorder(8, 0, 6, 0));

            JTextField champTitreMorceau  = creerChampTexte("Titre");
            ajouterPlaceholder(champTitreMorceau, "Titre");
            JTextField champArtisteMorceau = creerChampTexte("Artiste");
            ajouterPlaceholder(champArtisteMorceau, "Artiste");
            JTextField champAlbumMorceau  = creerChampTexte("Album (optionnel)");
            ajouterPlaceholder(champAlbumMorceau, "Album (optionnel)");
            JTextField champDureeMorceau  = creerChampTexte("Durée (secondes)");
            ajouterPlaceholder(champDureeMorceau, "Durée (secondes)");

            JButton btnAjouterMorceau = creerBoutonAdmin("Ajouter", ACCENT);
            btnAjouterMorceau.addActionListener(evt -> {
                String titre  = champTitreMorceau.getText().trim();
                String artiste = champArtisteMorceau.getText().trim();
                String album  = champAlbumMorceau.getText().trim();
                String dureeStr = champDureeMorceau.getText().trim();
                if (titre.isEmpty() || artiste.isEmpty() || dureeStr.isEmpty()) {
                    fenetreVisite.afficherErreur(new ActionException("Titre, artiste et durée requis"));
                    return;
                }
                try {
                    int duree = Integer.parseInt(dureeStr);
                    MorceauForm form = new MorceauForm(titre, artiste, album.isEmpty() ? null : album, duree);
                    //new AjouterMorceau().executer(new ActionArguments(catalogueActuel, form));
                    champTitreMorceau.setText(""); champArtisteMorceau.setText("");
                    champAlbumMorceau.setText(""); champDureeMorceau.setText("");
                    fenetreVisite.afficherErreur(new ActionException("Morceau ajouté avec succés"));
                } catch (NumberFormatException ex) {
                    fenetreVisite.afficherErreur(new ActionException("Durée invalide"));
                } catch (Exception ex) {
                    afficherErreur(ex);
                }
            });

            JSeparator sep2 = new JSeparator();
            sep2.setForeground(BORDER);
            sep2.setBackground(BORDER);
            sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            sep2.setAlignmentX(Component.LEFT_ALIGNMENT);

            //supprimé
            JLabel labelSupprMorceau = new JLabel("Supprimer un morceau");
            labelSupprMorceau.setFont(new Font("SansSerif", Font.BOLD, 13));
            labelSupprMorceau.setForeground(new Color(200, 60, 60));
            labelSupprMorceau.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelSupprMorceau.setBorder(new EmptyBorder(10, 0, 6, 0));

            JTextField champSupprMorceauTitre = creerChampTexte("Nom du morceau");
            ajouterPlaceholder(champSupprMorceauTitre, "Nom du morceau");
            JTextField champSupprMorceauArtiste = creerChampTexte("Nom de l'artiste");
            ajouterPlaceholder(champSupprMorceauArtiste, "Nom de l'artiste");
            JButton btnSupprimerMorceau = creerBoutonAdmin("Supprimer", new Color(180, 40, 40));
            btnSupprimerMorceau.addActionListener(evt -> {
                String nomValTitre = champSupprMorceauTitre.getText().trim();
                String nomValArttiste = champSupprMorceauArtiste.getText().trim();
                if (nomValTitre.isEmpty()||nomValArttiste.isEmpty()) {
                    fenetreVisite.afficherErreur(new ActionException("Nom et artiste requis"));
                    return;
                }
                try {
                    //new SupprimerMorceau().executer(new ActionArguments(catalogueActuel, nomVal, null));
                    champSupprMorceauTitre.setText("");
                    fenetreVisite.afficherErreur(new ActionException("Morceau supprimé avec succés"));
                } catch (Exception ex) {
                    afficherErreur(ex);
                }
            });

            vueMorceau.add(labelAjoutMorceau);
            vueMorceau.add(champTitreMorceau);
            vueMorceau.add(Box.createVerticalStrut(6));
            vueMorceau.add(champArtisteMorceau);
            vueMorceau.add(Box.createVerticalStrut(6));
            vueMorceau.add(champAlbumMorceau);
            vueMorceau.add(Box.createVerticalStrut(6));
            vueMorceau.add(champDureeMorceau);
            vueMorceau.add(Box.createVerticalStrut(8));
            vueMorceau.add(btnAjouterMorceau);
            vueMorceau.add(Box.createVerticalStrut(14));
            vueMorceau.add(sep2);
            vueMorceau.add(labelSupprMorceau);
            vueMorceau.add(champSupprMorceauTitre);
            vueMorceau.add(Box.createVerticalStrut(6));
            vueMorceau.add(champSupprMorceauArtiste);
            vueMorceau.add(Box.createVerticalStrut(8));
            vueMorceau.add(btnSupprimerMorceau);
            vueMorceau.add(Box.createVerticalGlue());

            // sous partie vue artiste
            JPanel vueArtiste = new JPanel();
            vueArtiste.setLayout(new BoxLayout(vueArtiste, BoxLayout.Y_AXIS));
            vueArtiste.setOpaque(false);

            JLabel labelAjoutArtiste = new JLabel("Ajouter un artiste");
            labelAjoutArtiste.setFont(new Font("SansSerif", Font.BOLD, 13));
            labelAjoutArtiste.setForeground(ACCENT);
            labelAjoutArtiste.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelAjoutArtiste.setBorder(new EmptyBorder(8, 0, 6, 0));

            JTextField champNomArtiste = creerChampTexte("Nom de l'artiste");
            ajouterPlaceholder(champNomArtiste, "Nom de l'artiste");
            JButton btnAjouterArtiste = creerBoutonAdmin("Ajouter", ACCENT);
            btnAjouterArtiste.addActionListener(evt -> {
                String nomVal = champNomArtiste.getText().trim();
                if (nomVal.isEmpty()) {
                    fenetreVisite.afficherErreur(new ActionException("Nom de l'artiste requis"));
                    return;
                }
                try {
                    ArtisteForm form = new ArtisteForm(nomVal);
                    //new AjouterArtiste().executer(new ActionArguments(catalogueActuel, form));
                    champNomArtiste.setText("");
                    fenetreVisite.afficherErreur(new ActionException("Artiste ajouter avec succés"));
                } catch (Exception ex) {
                    afficherErreur(ex);
                }
            });

            JSeparator sep3 = new JSeparator();
            sep3.setForeground(BORDER);
            sep3.setBackground(BORDER);
            sep3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            sep3.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel labelSupprArtiste = new JLabel("Supprimer un artiste");
            labelSupprArtiste.setFont(new Font("SansSerif", Font.BOLD, 13));
            labelSupprArtiste.setForeground(new Color(200, 60, 60));
            labelSupprArtiste.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelSupprArtiste.setBorder(new EmptyBorder(10, 0, 6, 0));

            JTextField champSupprArtiste = creerChampTexte("Nom de l'artiste");
            ajouterPlaceholder(champSupprArtiste, "Nom de l'artiste");
            JButton btnSupprimerArtiste = creerBoutonAdmin("Supprimer", new Color(180, 40, 40));
            btnSupprimerArtiste.addActionListener(evt -> {
                String nomVal = champSupprArtiste.getText().trim();
                if (nomVal.isEmpty()) {
                    fenetreVisite.afficherErreur(new ActionException("Nom de l'artiste requis"));
                    return;
                }
                try {
                    ArtisteForm form = new ArtisteForm(nomVal);
                    //new SupprimerArtiste().executer(new ActionArguments(catalogueActuel, form));
                    champSupprArtiste.setText("");
                    fenetreVisite.afficherErreur(new ActionException("Artiste supprimé avec succés"));
                } catch (Exception ex) {
                    afficherErreur(ex);
                }
            });

            vueArtiste.add(labelAjoutArtiste);
            vueArtiste.add(champNomArtiste);
            vueArtiste.add(Box.createVerticalStrut(8));
            vueArtiste.add(btnAjouterArtiste);
            vueArtiste.add(Box.createVerticalStrut(14));
            vueArtiste.add(sep3);
            vueArtiste.add(labelSupprArtiste);
            vueArtiste.add(champSupprArtiste);
            vueArtiste.add(Box.createVerticalStrut(8));
            vueArtiste.add(btnSupprimerArtiste);
            vueArtiste.add(Box.createVerticalGlue());


            // sous partie vue playlist
            JPanel vuePlaylist = new JPanel();
            vuePlaylist.setLayout(new BoxLayout(vuePlaylist, BoxLayout.Y_AXIS));
            vuePlaylist.setOpaque(false);

            JLabel labelAjoutPlaylist = new JLabel("Ajouter une playlist");
            labelAjoutPlaylist.setFont(new Font("SansSerif", Font.BOLD, 13));
            labelAjoutPlaylist.setForeground(ACCENT);
            labelAjoutPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelAjoutPlaylist.setBorder(new EmptyBorder(8, 0, 6, 0));

            JTextField champNomPlaylist = creerChampTexte("Nom de la playlist");
            ajouterPlaceholder(champNomPlaylist, "Nom de la playlist");
            JButton btnAjouterPlaylist = creerBoutonAdmin("Ajouter", ACCENT);
            btnAjouterPlaylist.addActionListener(evt -> {
                String nomVal = champNomPlaylist.getText().trim();
                if (nomVal.isEmpty()) {
                    fenetreVisite.afficherErreur(new ActionException("Nom de la playlist requis"));
                    return;
                }
                try {
                    //PlaylistForm form = new PlaylistForm(nomVal);
                    //new AjouterArtiste().executer(new ActionArguments(catalogueActuel, form));
                    champNomPlaylist.setText("");
                    fenetreVisite.afficherErreur(new ActionException("Playlist ajouté avec succés"));
                } catch (Exception ex) {
                    afficherErreur(ex);
                }
            });

            JSeparator sep4 = new JSeparator();
            sep4.setForeground(BORDER);
            sep4.setBackground(BORDER);
            sep4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            sep4.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel labelSupprPlaylist = new JLabel("Supprimer un artiste");
            labelSupprPlaylist.setFont(new Font("SansSerif", Font.BOLD, 13));
            labelSupprPlaylist.setForeground(new Color(200, 60, 60));
            labelSupprPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelSupprPlaylist.setBorder(new EmptyBorder(10, 0, 6, 0));

            JTextField champSupprPlaylist= creerChampTexte("Nom de la playlist");
            ajouterPlaceholder(champSupprPlaylist, "Nom de la playlist");
            JButton btnSupprimerPlaylist = creerBoutonAdmin("Supprimer", new Color(180, 40, 40));
            btnSupprimerPlaylist.addActionListener(evt -> {
                String nomVal = champSupprPlaylist.getText().trim();
                if (nomVal.isEmpty()) {
                    fenetreVisite.afficherErreur(new ActionException("Nom de la playlist requis"));
                    return;
                }
                try {
                    //PLaylistForm form = new PLaylistForm(nomVal);
                    //new SupprimerArtiste().executer(new ActionArguments(catalogueActuel, form));
                    champSupprPlaylist.setText("");
                    fenetreVisite.afficherErreur(new ActionException("Playlist supprimé avec succés"));
                } catch (Exception ex) {
                    afficherErreur(ex);
                }
            });

            vuePlaylist.add(labelAjoutPlaylist);
            vuePlaylist.add(champNomPlaylist);
            vuePlaylist.add(Box.createVerticalStrut(8));
            vuePlaylist.add(btnAjouterPlaylist);
            vuePlaylist.add(Box.createVerticalStrut(14));
            vuePlaylist.add(sep3);
            vuePlaylist.add(labelSupprPlaylist);
            vuePlaylist.add(champSupprPlaylist);
            vuePlaylist.add(Box.createVerticalStrut(8));
            vuePlaylist.add(btnSupprimerPlaylist);
            vuePlaylist.add(Box.createVerticalGlue());

            ongletCatalogue.add(vueMorceau,"morceau");
            ongletCatalogue.add(vueArtiste,"artiste");
            ongletCatalogue.add(vuePlaylist,"playlist");

            // Listeners onglets catalogue
            JButton[] btnsCatalogue  = { btnOngletMorceau, btnOngletArtiste,  btnOngletPlaylist};
            String[]  cartesCatalogue = { "morceau", "artiste", "playlist"};
            for (int i = 0; i < btnsCatalogue.length; i++) {
                final int idx = i;
                btnsCatalogue[i].addActionListener(evt -> {
                    ongletCatalogueLayout.show(ongletCatalogue, cartesCatalogue[idx]);
                    for (int j = 0; j < btnsCatalogue.length; j++) {
                        appliquerStyleCategorieActive(btnsCatalogue[j], j == idx, ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
                    }
                });
            }
            ongletCatalogueLayout.show(ongletCatalogue, "morceau");
            appliquerStyleCategorieActive(btnOngletMorceau, true,  ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
            appliquerStyleCategorieActive(btnOngletArtiste, false, ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
            appliquerStyleCategorieActive(btnOngletPlaylist, false, ACCENT, BG_CARTE, TEXT_GRIS, BORDER);

            colonneCatalogue.add(titreCatalogue);
            colonneCatalogue.add(barreCatalogue);
            colonneCatalogue.add(Box.createVerticalStrut(8));
            colonneCatalogue.add(ongletCatalogue);

            gestion.add(colonneAbonnes);
            gestion.add(colonneCatalogue);

            contenu.add(gestion, BorderLayout.NORTH);

            naviguerVers(contenu);
        });
    }

    //Helpers
    private JTextField creerChampTexte(String placeholder) {
        JTextField champ = new JTextField();
        styliserChamp(champ, placeholder);
        return champ;
    }

    private void styliserChamp(JTextField champ, String placeholder) {
        champ.setBackground(new Color(45, 45, 45));
        champ.setForeground(TEXT_BLANC);
        champ.setCaretColor(TEXT_BLANC);
        champ.setFont(new Font("SansSerif", Font.PLAIN, 13));
        champ.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        champ.putClientProperty("JTextField.placeholderText", placeholder);
        champ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        champ.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton creerBoutonAdmin(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(TEXT_BLANC);
        btn.setBackground(couleur);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return btn;
    }

    private String formatterDateAvis(LocalDate dateAvis) {
        if (dateAvis == null) {
            return "Inconnue";
        }
        return dateAvis.toString();
    }

    private String formatterArtistes(ArrayList<Artiste> artistes) {
        if (artistes == null || artistes.isEmpty()) {
            return "Inconnu";
        }

        StringBuilder noms = new StringBuilder();
        for (int i = 0; i < artistes.size(); i++) {
            if (i > 0) {
                noms.append(", ");
            }
            noms.append(artistes.get(i).getNom());
        }
        return noms.toString();
    }

    public void afficherRecherche(ResultatRecherche resultat) {
        if (fenetreVisite == null) return;

        executerEtAttendre(() -> {


            historique_fenetre.clear(); //clear historique
            mettreAJourBoutonRetour();


            //comptage
            int nbMorceaux = (resultat == null || resultat.morceaux == null) ? 0 : resultat.morceaux.size();
            int nbArtistes = (resultat == null || resultat.artistes == null) ? 0 : resultat.artistes.size();
            int nbAlbums = (resultat == null || resultat.albums == null) ? 0 : resultat.albums.size();
            int nbPlaylists = (resultat == null || resultat.playlists == null) ? 0 : resultat.playlists.size();
            int total = nbMorceaux + nbArtistes + nbAlbums + nbPlaylists;


            ArrayList<Morceau> morceauxResultat = (resultat != null && resultat.morceaux != null) ? resultat.morceaux  : new ArrayList<>();
            ArrayList<Artiste> artistesResultat = (resultat != null && resultat.artistes != null) ? resultat.artistes : new ArrayList<>();
            ArrayList<Album>  albumsResultat = (resultat != null && resultat.albums != null) ? resultat.albums  : new ArrayList<>();
            ArrayList<Playlist> playlistsResultat = (resultat != null && resultat.playlists != null) ? resultat.playlists : new ArrayList<>();

            // cnteneur racine
            JPanel contenu = new JPanel(new BorderLayout());
            contenu.setBackground(BG_PRINCIPAL);
            contenu.setBorder(new EmptyBorder(20, 24, 24, 24));

            // en-tête
            JPanel haut = new JPanel();
            haut.setLayout(new BoxLayout(haut, BoxLayout.Y_AXIS));
            haut.setOpaque(false);

            JLabel titre = new JLabel("Résultats de recherche");
            titre.setFont(new Font("SansSerif", Font.BOLD, 22));
            titre.setForeground(TEXT_BLANC);
            titre.setBorder(new EmptyBorder(0, 0, 4, 0));
            titre.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel resume = new JLabel(total == 0 ? "Aucun résultat" : total + " résultat" + (total > 1 ? "s" : ""));
            resume.setFont(new Font("SansSerif", Font.PLAIN, 13));
            resume.setForeground(TEXT_GRIS);
            resume.setBorder(new EmptyBorder(0, 0, 16, 0));
            resume.setAlignmentX(Component.LEFT_ALIGNMENT);

            haut.add(titre);
            haut.add(resume);

            // Barre de catégories
            CardLayout cartesCategoriesLayout = new CardLayout();
            JPanel cartesCategories = new JPanel(cartesCategoriesLayout);
            cartesCategories.setBackground(BG_PRINCIPAL);

            // Boutons de catégorie stylisés dark
            JButton btnMorceaux = creerBoutonCategorie("Morceaux ("+ nbMorceaux  + ")", ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
            JButton btnArtistes = creerBoutonCategorie("Artistes (" + nbArtistes  + ")", ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
            JButton btnAlbums = creerBoutonCategorie("Albums (" + nbAlbums + ")", ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
            JButton btnPlaylists= creerBoutonCategorie("Playlists (" + nbPlaylists + ")", ACCENT, BG_CARTE, TEXT_GRIS, BORDER);

            JPanel barreCategories = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            barreCategories.setOpaque(false);
            barreCategories.add(btnMorceaux);
            barreCategories.add(btnArtistes);
            barreCategories.add(btnAlbums);
            barreCategories.add(btnPlaylists);
            barreCategories.setBorder(new EmptyBorder(0, 0, 14, 0));
            barreCategories.setAlignmentX(Component.LEFT_ALIGNMENT);

            haut.add(barreCategories);

                // Vues par catégorie (version simple, sans lambda)
                cartesCategories.add(creerVueMorceaux(morceauxResultat), "morceaux");
                cartesCategories.add(creerVueArtistes(artistesResultat), "artistes");
                cartesCategories.add(creerVueAlbums(albumsResultat), "albums");
                cartesCategories.add(creerVuePlaylists(playlistsResultat), "playlists");

            // listeners des onglets
            JButton[] tousLesBtns = { btnMorceaux, btnArtistes, btnAlbums, btnPlaylists };
            String[]  toutesCartes = { "morceaux",  "artistes",  "albums",  "playlists" };

            for (int i = 0; i < tousLesBtns.length; i++) {
                final int idx = i;
                tousLesBtns[i].addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        cartesCategoriesLayout.show(cartesCategories, toutesCartes[idx]);
                        for (int j = 0; j < tousLesBtns.length; j++) {
                            appliquerStyleCategorieActive(tousLesBtns[j], j == idx, ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
                        }
                    }
                });
            }

            // Catégorie initiale
            int idxInitial = 0;
            if (nbMorceaux == 0 && nbArtistes > 0) idxInitial = 1;
            else if (nbMorceaux == 0 && nbArtistes == 0 && nbAlbums > 0) idxInitial = 2;
            else if (nbMorceaux == 0 && nbArtistes == 0 && nbAlbums == 0 && nbPlaylists > 0) idxInitial = 3;

            cartesCategoriesLayout.show(cartesCategories, toutesCartes[idxInitial]);
            for (int j = 0; j < tousLesBtns.length; j++) {
                appliquerStyleCategorieActive(tousLesBtns[j], j == idxInitial, ACCENT, BG_CARTE, TEXT_GRIS, BORDER);
            }

            // Assemblage
            contenu.add(haut,BorderLayout.NORTH);
            contenu.add(cartesCategories, BorderLayout.CENTER);

            fenetreVisite.setPanelCentral(contenu);
        });
    }

    /* pouir avoir les résultats de recherche > morceaux */
    private JComponent creerVueMorceaux(ArrayList<Morceau> morceauxResultat) {
        JPanel liste = new JPanel();
        liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));
        liste.setBackground(new Color(26, 26, 26));
        liste.setBorder(new EmptyBorder(10, 0, 10, 0));

        if (morceauxResultat == null || morceauxResultat.isEmpty()) {
            JLabel vide = new JLabel("Aucun resultat dans cette categorie.");
            vide.setFont(new Font("SansSerif", Font.PLAIN, 14));
            vide.setForeground(new Color(120, 120, 120));
            vide.setBorder(new EmptyBorder(14, 6, 0, 0));
            vide.setAlignmentX(Component.LEFT_ALIGNMENT);
            liste.add(vide);
        } else {
            // on parcourt les morceaux et crée une ligne
            for (final Morceau morceau : morceauxResultat) {
                LigneResultat<Morceau> ligne = new LigneResultat<>(
                        morceau,
                        morceau.getNom(),
                        formatterArtistes(morceau.getArtistes()),
                        formatterDuree(morceau.getDuree()),
                        true
                );
                boolean aimeActuel = estAime(morceau);
                liste.add(creerLigne(
                        ligne,
                        new Runnable() { public void run() { afficherDetails(morceau); } },
                    new Runnable() { public void run() { basculerAime(morceau); } },
                        true,
                        aimeActuel
                ));
                liste.add(Box.createVerticalStrut(4));
            }
        }

        return creerScrollCategorie(liste);
    }

    private JComponent creerVueArtistes(ArrayList<Artiste> artistesResultat) {
        JPanel liste = new JPanel();
        liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));
        liste.setBackground(new Color(26, 26, 26));
        liste.setBorder(new EmptyBorder(10, 0, 10, 0));

        if (artistesResultat == null || artistesResultat.isEmpty()) {
            JLabel vide = new JLabel("Aucun resultat dans cette categorie.");
            vide.setFont(new Font("SansSerif", Font.PLAIN, 14));
            vide.setForeground(new Color(120, 120, 120));
            vide.setBorder(new EmptyBorder(14, 6, 0, 0));
            vide.setAlignmentX(Component.LEFT_ALIGNMENT);
            liste.add(vide);
        } else {
            for (final Artiste artiste : artistesResultat) {
                String detail = artiste.getAlbums() == null ? "0 album" : artiste.getAlbums().size() + " album(s)";
                LigneResultat<Artiste> ligne = new LigneResultat<>(artiste, artiste.getNom(), detail, "", false);
                liste.add(creerLigne(
                        ligne,
                        new Runnable() { public void run() { afficherDetails(artiste); } },
                        new Runnable() { public void run() {} },
                        false,
                        false
                ));
                liste.add(Box.createVerticalStrut(4));
            }
        }

        return creerScrollCategorie(liste);
    }

    private JComponent creerVueAlbums(ArrayList<Album> albumsResultat) {
        JPanel liste = new JPanel();
        liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));
        liste.setBackground(new Color(26, 26, 26));
        liste.setBorder(new EmptyBorder(10, 0, 10, 0));

        if (albumsResultat == null || albumsResultat.isEmpty()) {
            JLabel vide = new JLabel("Aucun resultat dans cette categorie.");
            vide.setFont(new Font("SansSerif", Font.PLAIN, 14));
            vide.setForeground(new Color(120, 120, 120));
            vide.setBorder(new EmptyBorder(14, 6, 0, 0));
            vide.setAlignmentX(Component.LEFT_ALIGNMENT);
            liste.add(vide);
        } else {
            for (final Album album : albumsResultat) {
                String nomArtiste = album.getArtiste() != null ? album.getArtiste().getNom() : "Inconnu";
                String detail = nomArtiste + (album.getAnnee() > 0 ? " — " + album.getAnnee() : "");
                LigneResultat<Album> ligne = new LigneResultat<>(album, album.getNom(), detail, "", false);
                liste.add(creerLigne(
                        ligne,
                        new Runnable() { public void run() { afficherDetails(album); } },
                        new Runnable() { public void run() {} },
                        false,
                        false
                ));
                liste.add(Box.createVerticalStrut(4));
            }
        }

        return creerScrollCategorie(liste);
    }

    private JComponent creerVuePlaylists(ArrayList<Playlist> playlistsResultat) {
        JPanel liste = new JPanel();
        liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));
        liste.setBackground(new Color(26, 26, 26));
        liste.setBorder(new EmptyBorder(10, 0, 10, 0));

        if (playlistsResultat == null || playlistsResultat.isEmpty()) {
            JLabel vide = new JLabel("Aucun resultat dans cette categorie.");
            vide.setFont(new Font("SansSerif", Font.PLAIN, 14));
            vide.setForeground(new Color(120, 120, 120));
            vide.setBorder(new EmptyBorder(14, 6, 0, 0));
            vide.setAlignmentX(Component.LEFT_ALIGNMENT);
            liste.add(vide);
        } else {
            for (final Playlist playlist : playlistsResultat) {
                String detail = (playlist.getMorceaux() == null ? 0 : playlist.getMorceaux().size()) + " morceau(x) · " + playlist.getCreation();
                LigneResultat<Playlist> ligne = new LigneResultat<>(playlist, playlist.getNom(), detail, "", true);
                boolean aimeActuel = estAimee(playlist);
                liste.add(creerLigne(
                        ligne,
                        new Runnable() { public void run() { afficherDetails(playlist); } },
                    new Runnable() { public void run() { basculerAime(playlist); } },
                        true,
                        aimeActuel
                ));
                liste.add(Box.createVerticalStrut(4));
            }
        }

        return creerScrollCategorie(liste);
    }

    private JComponent creerScrollCategorie(JPanel liste) {
        JScrollPane scroll = new JScrollPane(liste);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor= new Color(70, 70, 70);
                trackColor= new Color(30, 30, 30);
            }
            @Override protected JButton createDecreaseButton(int o) { return boutonVide(); }
            @Override protected JButton createIncreaseButton(int o) { return boutonVide(); }
            private JButton boutonVide() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setVisible(false);
                return b;
            }
        });
        scroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        scroll.getViewport().setBackground(new Color(26, 26, 26));
        return scroll;
    }

    private JButton creerBoutonCategorie(String texte, Color accent, Color bgCarte, Color textGris, Color border) {
        JButton btn = new JButton(texte) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fond arrondi
                boolean actif = Boolean.TRUE.equals(getClientProperty("actif"));
                g2.setColor(actif ? accent : bgCarte);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // Bordure subtile si inactif
                if (!actif) {
                    g2.setColor(border);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                }
                // Texte
                g2.setFont(getFont());
                g2.setColor(actif ? Color.WHITE : textGris);
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setPreferredSize(new Dimension(160, 32));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("actif", false);

        // Hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.repaint(); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { btn.repaint(); }
        });
        return btn;
    }

    private void appliquerStyleCategorieActive(JButton btn, boolean actif,
                                               Color accent, Color bgCarte, Color textGris, Color border) {
        btn.putClientProperty("actif", actif);
        btn.repaint();
    } //reuslata rehcerche
   
    public MorceauForm demanderMorceau(){return null;}; //admin ajouter
    public ArtisteForm demanderArtiste(){return null;}; //admin
    public PlaylistForm demanderPlaylist(int numUtilisateur){return null;}; //abonéé

    public String choisirMorceau(){
        return "0";
    }

    public void afficherUtilisateurs(ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {}

    private void naviguerVers(JComponent nouveauPanel) {
        JComponent actuel = fenetreVisite.getPanelCentral();
        if (actuel != null) {
            historique_fenetre.push(actuel);
        }
        mettreAJourBoutonRetour();
        fenetreVisite.setPanelCentral(nouveauPanel);
    }

    private void mettreAJourBoutonRetour() {
        // à appeler après chaque push/pop
        JLabel btnRetour = fenetreVisite.getBtnRetour2();
        if (btnRetour != null) {
            btnRetour.setVisible(!historique_fenetre.isEmpty());
        }
    }

    private void ajouterPlaceholder(JTextField champ, String placeholder) {
        champ.setText(placeholder);
        champ.setForeground(Color.GRAY);

        champ.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (champ.getText().equals(placeholder)) {
                    champ.setText("");
                    champ.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (champ.getText().isEmpty()) {
                    champ.setText(placeholder);
                    champ.setForeground(Color.GRAY);
                }
            }
        });
    }


}