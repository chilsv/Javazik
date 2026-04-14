package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    private static final int DUREE_NOTIF = 1500;

    private final JFrame frame; // fenêtre de l'appli
    private final CardLayout cardLayout;
    private final JPanel pages; // le panel princiapal qui va contenir toutes les pages
    private final Map<String, JPanel> pagesMap; // map pour trouver le panel grâcec à son nom
    private FenetreVisite fenetreVisite;

    private Consumer<Morceau> basculerMorceauAimeHandler = morceau -> {};
    private Function<Morceau, Boolean> estMorceauAimeProvider = morceau -> false;
    private Consumer<Playlist> basculerPlaylistAimeeHandler = playlist -> {};
    private Function<Playlist, Boolean> estPlaylistAimeeProvider = playlist -> false;

    // notification pour faire les erreurs "résevré aux abonnés"
    private JWindow notif;
    private Timer tempsNotif;

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

    /*quelle bouton on clique*/
    public Action choisirAction(String accueil, Personne utilisateur) {
        final Action[] resultat = {null};
        final Object verrou = new Object();

        executerEtAttendre(() -> {
            if (fenetreVisite == null) {
                fenetreVisite = new FenetreVisite(this);
                fenetreVisite.setFrame(frame);
            }
            // On affiche la page de visite et on réinitialise tous les événeemnts
            afficherPanel("visite", fenetreVisite.getPanel());
            fenetreVisite.reinitialiserEvenements();

            boolean filtreVisible = !(utilisateur instanceof Visiteur); // ça dépend de si c'est un visiteur ou pas
            EvenementsVisite.ajouterEvenements(fenetreVisite, choix -> {
                synchronized (verrou) {
                    if (choix == 1) {
                        fenetreVisite.viderPanelCentral();
                        resultat[0] = new ConsulterProfil();
                    } else if (choix == 2) {
                        fenetreVisite.viderPanelCentral();
                        resultat[0] = new ConsulterLibrairie();
                    } else if (choix == 3) {
                        fenetreVisite.viderPanelCentral();
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

    /* barre de lecture à faire*/
    public void afficherLecture(Morceau morceau){
    };

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

    public void afficherProfilAdmin(Admin admin) {
    }

    public RechercheForm demanderRecherche(Filtre filtre) {
        return new RechercheForm(fenetreVisite.getBarreRecherche().getText(), filtre);
    }

    public Filtre afficherFiltres() {
        return fenetreVisite.getFiltres();
    }

    public void configurerActionsResultats(
        Consumer<Morceau> basculerMorceauAime,
        Function<Morceau, Boolean> estMorceauAime,
        Consumer<Playlist> basculerPlaylistAimee,
        Function<Playlist, Boolean> estPlaylistAimee
    ) {
        basculerMorceauAimeHandler = basculerMorceauAime != null ? basculerMorceauAime : (morceau -> {});
        if (basculerMorceauAime != null) {
            basculerMorceauAimeHandler = basculerMorceauAime;
        }
        estMorceauAimeProvider = estMorceauAime != null ? estMorceauAime : (morceau -> false);
        basculerPlaylistAimeeHandler = basculerPlaylistAimee != null ? basculerPlaylistAimee : (playlist -> {});
        estPlaylistAimeeProvider = estPlaylistAimee != null ? estPlaylistAimee : (playlist -> false);
    }

    private JButton creerBoutonCategorie(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setOpaque(true);
        bouton.setBackground(new Color(235, 235, 235));
        bouton.setForeground(new Color(55, 55, 55));
        bouton.setFont(new Font("SansSerif", Font.BOLD, 13));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setBorder(new EmptyBorder(8, 12, 8, 12));
        return bouton;
    }

    /* pour afficher la catégorie en bleu quand on est dedans */
    private void appliquerStyleCategorieActive(JButton bouton, boolean active) {
        if (active) {
            bouton.setBackground(new Color(45, 140, 240));
            bouton.setForeground(Color.WHITE);
        } else {
            bouton.setBackground(new Color(235, 235, 235));
            bouton.setForeground(new Color(55, 55, 55));
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
    private <T extends TypeObjets> JPanel creerLigne(LigneResultat<T> ligne, Runnable actionPlay, Runnable actionDetails, Runnable actionAimer, boolean afficherAimer, Supplier<Boolean> aimeActuel) {
        JPanel lignePanel = new JPanel(new BorderLayout(12, 0));
        lignePanel.setOpaque(true);
        lignePanel.setBackground(new Color(248, 251, 255));
        lignePanel.setBorder(new EmptyBorder(6, 10, 6, 10));
        lignePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        lignePanel.setPreferredSize(new Dimension(100, 76));
        lignePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // bouton play
        JLabel play = creerLabelImage("assets/play.png", "play", actionPlay, 22, 22);
        JLabel vignette = creerVignetteObjet(ligne.objet);
        play.setAlignmentY(Component.CENTER_ALIGNMENT);
        vignette.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel gauche = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        gauche.setOpaque(false);
        gauche.setAlignmentY(Component.CENTER_ALIGNMENT);
        gauche.add(play);
        gauche.add(vignette);

        JPanel texte = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        texte.setOpaque(false);
        texte.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel principal = new JLabel(ligne.titre);
        principal.setFont(new Font("SansSerif", Font.BOLD, 14));
        principal.setForeground(new Color(25, 25, 25));
        texte.add(principal);

        if (!ligne.detail.isBlank()) {
            JLabel secondaire = new JLabel(ligne.detail);
            secondaire.setFont(new Font("SansSerif", Font.PLAIN, 12));
            secondaire.setForeground(new Color(95, 95, 95));
            texte.add(secondaire);
        }

        if (!ligne.duree.isBlank()) {
            JLabel duree = new JLabel(ligne.duree);
            duree.setFont(new Font("SansSerif", Font.PLAIN, 12));
            duree.setForeground(new Color(110, 110, 110));
            texte.add(duree);
        }

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.setAlignmentY(Component.CENTER_ALIGNMENT);
        if (afficherAimer) {
            final JLabel[] aimerLabelRef = new JLabel[1];
            boolean aimeInitial = aimeActuel != null && Boolean.TRUE.equals(aimeActuel.get());
            String chemin;
            if (aimeInitial) {
                chemin = "assets/aime_true.png";
            } else {
                chemin = "assets/aime_false.png";
            }
            aimerLabelRef[0] = creerLabelImage(chemin, "aimer", () -> {
                actionAimer.run();
                boolean aimeMaj = aimeActuel != null && Boolean.TRUE.equals(aimeActuel.get());
                mettreIconeAimer(aimerLabelRef[0], aimeMaj);
            }, 22, 22);
            actions.add(aimerLabelRef[0]);
        }
        actions.add(creerLabelImage("assets/details.png", "details", actionDetails, 22, 22));

        lignePanel.add(gauche, BorderLayout.WEST);
        lignePanel.add(texte, BorderLayout.CENTER);
        lignePanel.add(actions, BorderLayout.EAST);
        return lignePanel;
    }

    private <T extends TypeObjets> JComponent creerVueCategorie(List<T> objets, Function<T, LigneResultat<T>> mapper, Consumer<T> actionPlay, Consumer<T> actionDetails, Consumer<T> actionAimer, Function<T, Boolean> aimeProvider) {
        JPanel liste = new JPanel();
        liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));
        liste.setBackground(Color.WHITE);
        liste.setBorder(new EmptyBorder(10, 0, 10, 0));

        if (objets == null || objets.isEmpty()) {
            JLabel vide = new JLabel("Aucun resultat dans cette categorie.");
            vide.setFont(new Font("SansSerif", Font.PLAIN, 14));
            vide.setForeground(new Color(120, 120, 120));
            vide.setBorder(new EmptyBorder(14, 6, 0, 0));
            vide.setAlignmentX(Component.LEFT_ALIGNMENT);
            liste.add(vide);
        } else {
            for (T objet : objets) {
                LigneResultat<T> ligne = mapper.apply(objet);
                Supplier<Boolean> aimeActuel = () -> aimeProvider != null && Boolean.TRUE.equals(aimeProvider.apply(objet));
                liste.add(creerLigne(ligne, () -> actionPlay.accept(objet), () -> actionDetails.accept(objet), () -> actionAimer.accept(objet), ligne.peutAimer, aimeActuel));
                liste.add(Box.createVerticalStrut(4));
            }
        }

        JScrollPane scroll = new JScrollPane(liste);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

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

    private void jouerObjet(Object objet) {
        if (objet instanceof Morceau) {
            afficherLecture((Morceau) objet);
        } else if (objet instanceof Album) {
            Morceau morceau = premierMorceau((Album) objet);
            if (morceau != null) {
                afficherLecture(morceau);
            } else {
                afficherMessage("Cet album ne contient aucun morceau.");
            }
        } else if (objet instanceof Playlist) {
            Morceau morceau = premierMorceau((Playlist) objet);
            if (morceau != null) {
                afficherLecture(morceau);
            } else {
                afficherMessage("Cette playlist ne contient aucun morceau.");
            }
        } else if (objet instanceof Artiste) {
            Morceau morceau = premierMorceau((Artiste) objet);
            if (morceau != null) {
                afficherLecture(morceau);
            } else {
                afficherMessage("Aucun morceau disponible pour cet artiste.");
            }
        }
    }

    private void afficherDetailsObjet(Object objet) {
        // Pas de dialogue detail: on conserve juste l'action cliquable sans pop-up.
        if (objet != null) {
            System.out.println("Details: " + objet.getClass().getSimpleName());
        }
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
        if (fenetreVisite == null) {
            return;
        }

        executerEtAttendre(() -> {
            JPanel contenu = new JPanel(new BorderLayout());
            contenu.setBackground(Color.WHITE);
            contenu.setBorder(new EmptyBorder(14, 18, 18, 18));

            JLabel titre = new JLabel("Resultats de recherche");
            titre.setFont(new Font("SansSerif", Font.BOLD, 24));
            titre.setBorder(new EmptyBorder(0, 0, 6, 0));

            int nbMorceaux = (resultat == null || resultat.morceaux == null) ? 0 : resultat.morceaux.size();
            int nbArtistes = (resultat == null || resultat.artistes == null) ? 0 : resultat.artistes.size();
            int nbAlbums = (resultat == null || resultat.albums == null) ? 0 : resultat.albums.size();
            int nbPlaylists = (resultat == null || resultat.playlists == null) ? 0 : resultat.playlists.size();
            int total = nbMorceaux + nbArtistes + nbAlbums + nbPlaylists;

            JLabel resume = new JLabel(total + " resultat(s)");
            resume.setForeground(new Color(90, 90, 90));
            resume.setBorder(new EmptyBorder(0, 0, 10, 0));

            JPanel haut = new JPanel();
            haut.setLayout(new BoxLayout(haut, BoxLayout.Y_AXIS));
            haut.setOpaque(false);
            haut.add(titre);
            haut.add(resume);

            List<Morceau> morceauxResultat = (resultat != null && resultat.morceaux != null)
                ? resultat.morceaux
                : new ArrayList<>();
            List<Artiste> artistesResultat = (resultat != null && resultat.artistes != null)
                ? resultat.artistes
                : new ArrayList<>();
            List<Album> albumsResultat = (resultat != null && resultat.albums != null)
                ? resultat.albums
                : new ArrayList<>();
            List<Playlist> playlistsResultat = (resultat != null && resultat.playlists != null)
                ? resultat.playlists
                : new ArrayList<>();

            JPanel barreCategories = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            barreCategories.setOpaque(false);

            CardLayout cartesCategoriesLayout = new CardLayout();
            JPanel cartesCategories = new JPanel(cartesCategoriesLayout);
            cartesCategories.setOpaque(false);

            JButton btnMorceaux = creerBoutonCategorie("Morceaux (" + nbMorceaux + ")");
            JButton btnArtistes = creerBoutonCategorie("Artistes (" + nbArtistes + ")");
            JButton btnAlbums = creerBoutonCategorie("Albums (" + nbAlbums + ")");
            JButton btnPlaylists = creerBoutonCategorie("Playlists (" + nbPlaylists + ")");

            cartesCategories.add(creerVueCategorie(morceauxResultat,
                morceau -> new LigneResultat<>(morceau, morceau.getNom(), formatterArtistes(morceau.getArtistes()), formatterDuree(morceau.getDuree()), true),
                this::jouerObjet,
                this::afficherDetailsObjet,
                objet -> basculerMorceauAimeHandler.accept((Morceau) objet),
                objet -> estMorceauAimeProvider.apply((Morceau) objet)), "morceaux");
            cartesCategories.add(creerVueCategorie(artistesResultat,
                artiste -> new LigneResultat<>(artiste, artiste.getNom(), artiste.getAlbums() == null ? "0 album" : artiste.getAlbums().size() + " album(s)", "", false),
                this::jouerObjet,
                this::afficherDetailsObjet,
                objet -> {},
                objet -> false), "artistes");
            cartesCategories.add(creerVueCategorie(albumsResultat,
                album -> {
                    String nomArtiste = album.getArtiste() != null ? album.getArtiste().getNom() : "Inconnu";
                    String detail = nomArtiste + (album.getAnnee() > 0 ? " - " + album.getAnnee() : "");
                    return new LigneResultat<>(album, album.getNom(), detail, "", false);
                },
                this::jouerObjet,
                this::afficherDetailsObjet,
                objet -> {},
                objet -> false), "albums");
            cartesCategories.add(creerVueCategorie(playlistsResultat,
                playlist -> new LigneResultat<>(playlist, playlist.getNom(), (playlist.getMorceaux() == null ? 0 : playlist.getMorceaux().size()) + " morceau(x) - " + playlist.getCreation(), "", true),
                this::jouerObjet,
                this::afficherDetailsObjet,
                objet -> basculerPlaylistAimeeHandler.accept((Playlist) objet),
                objet -> estPlaylistAimeeProvider.apply((Playlist) objet)), "playlists");

            btnMorceaux.addActionListener(evt -> {
                cartesCategoriesLayout.show(cartesCategories, "morceaux");
                appliquerStyleCategorieActive(btnMorceaux, true);
                appliquerStyleCategorieActive(btnArtistes, false);
                appliquerStyleCategorieActive(btnAlbums, false);
                appliquerStyleCategorieActive(btnPlaylists, false);
            });
            btnArtistes.addActionListener(evt -> {
                cartesCategoriesLayout.show(cartesCategories, "artistes");
                appliquerStyleCategorieActive(btnMorceaux, false);
                appliquerStyleCategorieActive(btnArtistes, true);
                appliquerStyleCategorieActive(btnAlbums, false);
                appliquerStyleCategorieActive(btnPlaylists, false);
            });
            btnAlbums.addActionListener(evt -> {
                cartesCategoriesLayout.show(cartesCategories, "albums");
                appliquerStyleCategorieActive(btnMorceaux, false);
                appliquerStyleCategorieActive(btnArtistes, false);
                appliquerStyleCategorieActive(btnAlbums, true);
                appliquerStyleCategorieActive(btnPlaylists, false);
            });
            btnPlaylists.addActionListener(evt -> {
                cartesCategoriesLayout.show(cartesCategories, "playlists");
                appliquerStyleCategorieActive(btnMorceaux, false);
                appliquerStyleCategorieActive(btnArtistes, false);
                appliquerStyleCategorieActive(btnAlbums, false);
                appliquerStyleCategorieActive(btnPlaylists, true);
            });

            barreCategories.add(btnMorceaux);
            barreCategories.add(btnArtistes);
            barreCategories.add(btnAlbums);
            barreCategories.add(btnPlaylists);
            barreCategories.setBorder(new EmptyBorder(0, 0, 10, 0));

            haut.add(barreCategories);
            contenu.add(haut, BorderLayout.NORTH);
            contenu.add(cartesCategories, BorderLayout.CENTER);

            String categorieInitiale = "morceaux";
            if (nbMorceaux == 0 && nbArtistes > 0) {
                categorieInitiale = "artistes";
            } else if (nbMorceaux == 0 && nbArtistes == 0 && nbAlbums > 0) {
                categorieInitiale = "albums";
            } else if (nbMorceaux == 0 && nbArtistes == 0 && nbAlbums == 0 && nbPlaylists > 0) {
                categorieInitiale = "playlists";
            }

            cartesCategoriesLayout.show(cartesCategories, categorieInitiale);
            appliquerStyleCategorieActive(btnMorceaux, "morceaux".equals(categorieInitiale));
            appliquerStyleCategorieActive(btnArtistes, "artistes".equals(categorieInitiale));
            appliquerStyleCategorieActive(btnAlbums, "albums".equals(categorieInitiale));
            appliquerStyleCategorieActive(btnPlaylists, "playlists".equals(categorieInitiale));

            fenetreVisite.setPanelCentral(contenu);
        });
    } //reuslata rehcerche
   
    public MorceauForm demanderMorceau(){return null;}; //admin ajouter
    public ArtisteForm demanderArtiste(){return null;}; //admin
    public PlaylistForm demanderPlaylist(int numUtilisateur){return null;}; //abonéé

    public String choisirMorceau(){
        return "0";
    }

    public void afficherUtilisateurs(ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {}

}