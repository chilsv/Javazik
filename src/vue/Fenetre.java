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
            if (visiteCourante == null) {
                visiteCourante = new FenetreVisite();
                visiteCourante.setFrame(frame);
            }
            afficherCarte(CARTE_VISITE, visiteCourante.getPanel());
            visiteCourante.reinitialiserEvenementsVisite();

            boolean filtreVisible = !(utilisateur instanceof Visiteur);
            EvenementsVisite.ajouterEvenements(visiteCourante, choix -> {
                synchronized (verrou) {
                    if (choix == 1) {
                        visiteCourante.viderPanelCentral();
                        resultat[0] = new ConsulterProfil();
                    } else if (choix == 2) {
                        visiteCourante.viderPanelCentral();
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

    private void appliquerStyleCategorieActive(JButton bouton, boolean active) {
        if (active) {
            bouton.setBackground(new Color(45, 140, 240));
            bouton.setForeground(Color.WHITE);
        } else {
            bouton.setBackground(new Color(235, 235, 235));
            bouton.setForeground(new Color(55, 55, 55));
        }
    }

    private JPanel creerCarteResultat(String titre, String detail) {
        JPanel carte = new JPanel(new BorderLayout());
        carte.setOpaque(true);
        carte.setBackground(new Color(248, 251, 255));
        carte.setBorder(new EmptyBorder(10, 12, 10, 12));
        carte.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        carte.setPreferredSize(new Dimension(100, 70));
        carte.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel principal = new JLabel(titre);
        principal.setFont(new Font("SansSerif", Font.BOLD, 14));
        principal.setForeground(new Color(25, 25, 25));

        JLabel secondaire = new JLabel(detail);
        secondaire.setFont(new Font("SansSerif", Font.PLAIN, 12));
        secondaire.setForeground(new Color(95, 95, 95));

        carte.add(principal, BorderLayout.NORTH);
        carte.add(secondaire, BorderLayout.SOUTH);
        return carte;
    }

    private JComponent creerVueCategorie(ArrayList<String> lignes) {
        JPanel liste = new JPanel();
        liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));
        liste.setBackground(Color.WHITE);
        liste.setBorder(new EmptyBorder(10, 0, 10, 0));

        if (lignes == null || lignes.isEmpty()) {
            JLabel vide = new JLabel("Aucun resultat dans cette categorie.");
            vide.setFont(new Font("SansSerif", Font.PLAIN, 14));
            vide.setForeground(new Color(120, 120, 120));
            vide.setBorder(new EmptyBorder(14, 6, 0, 0));
            vide.setAlignmentX(Component.LEFT_ALIGNMENT);
            liste.add(vide);
        } else {
            for (String ligne : lignes) {
                String titre = ligne;
                String detail = "";
                int idx = ligne.indexOf(" - ");
                if (idx >= 0) {
                    titre = ligne.substring(0, idx);
                    detail = ligne.substring(idx + 3);
                }
                liste.add(creerCarteResultat(titre, detail));
                liste.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(liste);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
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
        if (visiteCourante == null) {
            return;
        }

        executerSurEdtEtAttendre(() -> {
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

            ArrayList<String> lignesMorceaux = new ArrayList<>();
            if (resultat != null && resultat.morceaux != null) {
                for (Morceau morceau : resultat.morceaux) {
                    lignesMorceaux.add(morceau.getNom() + " - " + formatterArtistes(morceau.getArtistes()));
                }
            }

            ArrayList<String> lignesArtistes = new ArrayList<>();
            if (resultat != null && resultat.artistes != null) {
                for (Artiste artiste : resultat.artistes) {
                    lignesArtistes.add(artiste.getNom());
                }
            }

            ArrayList<String> lignesAlbums = new ArrayList<>();
            if (resultat != null && resultat.albums != null) {
                for (Album album : resultat.albums) {
                    String nomArtiste = album.getArtiste() != null ? album.getArtiste().getNom() : "Inconnu";
                    lignesAlbums.add(album.getNom() + " - " + nomArtiste);
                }
            }

            ArrayList<String> lignesPlaylists = new ArrayList<>();
            if (resultat != null && resultat.playlists != null) {
                for (Playlist playlist : resultat.playlists) {
                    lignesPlaylists.add(playlist.getNom());
                }
            }

            JPanel barreCategories = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            barreCategories.setOpaque(false);

            CardLayout cartesCategoriesLayout = new CardLayout();
            JPanel cartesCategories = new JPanel(cartesCategoriesLayout);
            cartesCategories.setOpaque(false);

            JButton btnMorceaux = creerBoutonCategorie("Morceaux (" + nbMorceaux + ")");
            JButton btnArtistes = creerBoutonCategorie("Artistes (" + nbArtistes + ")");
            JButton btnAlbums = creerBoutonCategorie("Albums (" + nbAlbums + ")");
            JButton btnPlaylists = creerBoutonCategorie("Playlists (" + nbPlaylists + ")");

            Map<String, JButton> boutons = new HashMap<>();
            boutons.put("morceaux", btnMorceaux);
            boutons.put("artistes", btnArtistes);
            boutons.put("albums", btnAlbums);
            boutons.put("playlists", btnPlaylists);

            cartesCategories.add(creerVueCategorie(lignesMorceaux), "morceaux");
            cartesCategories.add(creerVueCategorie(lignesArtistes), "artistes");
            cartesCategories.add(creerVueCategorie(lignesAlbums), "albums");
            cartesCategories.add(creerVueCategorie(lignesPlaylists), "playlists");

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

            visiteCourante.setPanelCentral(contenu);
        });
    } //reuslata rehcerche
    public MorceauForm demanderMorceau(){return null;}; //admin ajouter
    public ArtisteForm demanderArtiste(){return null;}; //admin
    public PlaylistForm demanderPlaylist(int numUtilisateur){return null;}; //abonéé

    public String choisirMorceau(){
        return "0";
    }

    public void afficherUtilisateurs(ArrayList<Abonne> abonnes, ArrayList<Admin> admins) {}

    public void afficherAimer(String nom) {}
}