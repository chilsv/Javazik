package vue;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.*;

import metier.Filtre;


public class FenetreVisite {


    private static final int largeur_bandeauGauche = 220;
    private static final int largeur_barreDeRecherche = Ecran.LONGUEUR - largeur_bandeauGauche;
    private static final int hauteur_barreDeRecherche = 64;
    private static final int largueur_filtre = 330;
    private static final int hauteur_filtre = 340;

    private JFrame frame;
    private final JPanel panel; // conteneur racine
    private final JLayeredPane coucheContenu;  // permet la superposition du contenu
    private final JPanel base; // structure principale sans la barre de recherche
    private final JPanel central; // zone de contenu centre
    private final JPanel bandeSelection; // barre latérale gauche de choix
    private final JPanel lecture; // barre de lecture musique

    //Barre de recherche
    private final JPanel zoneRecherche;
    private final JPanel boutonsRecherche;
    private final JTextField barreRecherche;
    private final FenetreFiltre panelFiltre;

    // Boutons sur la barre latérale gauche de choix
    private final JLabel profil;
    private final JLabel librairie;
    private final JLabel menuArtistes;
    private final JLabel menuAlbums;
    private final JLabel menuSongs;
    private final JLabel loupe;
    private final JLabel filtre;
    private final JLabel btnRetour;
    private final JLabel logoJavasik;
    private final JLabel fond;

    public FenetreVisite() {

        panel = new JPanel(new BorderLayout()); //panel de fond, pour eviter les probleme de superposition
        panel.setPreferredSize(new Dimension(Ecran.LONGUEUR, Ecran.HAUTEUR));
        //panel.setBackground(new Color(18, 18, 18)); // couleur du fond

        // Background
        ImageIcon background = new ImageIcon("assets/Q.png");
        fond = new JLabel(background);
        fond.setLayout(null);
        fond.setBounds(0, 0, Ecran.LONGUEUR, Ecran.HAUTEUR);

        coucheContenu = new JLayeredPane(); //panel de contenu, pour le filtrage par exemple
        coucheContenu.setPreferredSize(new Dimension(Ecran.LONGUEUR, Ecran.HAUTEUR));
        coucheContenu.setLayout(null);
        coucheContenu.add(fond, Integer.valueOf(-30000)); // couche tout en dessous
        coucheContenu.setOpaque(false);

        base = new JPanel(new BorderLayout()); //panel de base pour mettre barre gauche, barre ecoute ect
        base.setBounds(0, 0, Ecran.LONGUEUR, Ecran.HAUTEUR);
        base.setOpaque(false); // laisse voir le fond du panel


        bandeSelection = new JPanel(); //panel barre gauche de choix
        bandeSelection.setLayout(new BoxLayout(bandeSelection, BoxLayout.Y_AXIS));
        bandeSelection.setPreferredSize(new Dimension(220, Ecran.HAUTEUR)); //220 en x et y sur tout l'ecran
        bandeSelection.setBackground(new Color(18, 18, 18));


        //Contenu de la barre laterale  gauche, le logo pour commencer
        logoJavasik = creerLabel("assets/logo.png", "Javazik", 32, 32);
        logoJavasik.setForeground(new Color(255, 255, 255));
        logoJavasik.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoJavasik.setBorder(BorderFactory.createEmptyBorder(50, 16, 50, 0));
        logoJavasik.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoJavasik.setMaximumSize(new Dimension(220, 60));

        // Creation du contenu de la selection librairie
        JLabel labelLibrary = creerLabelTitre("LIBRAIRIE");
        profil = creerLabelMenu("assets/profil.png","Votre compte",22, 22); //32 est la taille du coté du carre de l'image
        menuArtistes = creerLabelMenu("assets/playlists.png","Artistes",25, 25);
        librairie = creerLabelMenu("assets/librairie.png","Playlists",25, 25);
        menuAlbums = creerLabelMenu("assets/songs.png","Albums",25, 25);
        menuSongs = creerLabelMenu("assets/songs.png","Morceaux",25, 25);

        // --- Section DISCOVER ---
        JLabel labelDiscover = creerLabelTitre("DISCOVER");
        JLabel menuBrowse  = creerLabelMenu(null, "Browse",   32, 32);
        JLabel menuForYou  = creerLabelMenu(null, "For you",  32, 32);
        // "Popular" est mis en surbrillance accent (page active)
        JLabel menuPopular = creerLabelMenu(null, "Popular", 32, 32);
        JLabel menuRadio   = creerLabelMenu(null, "Radio",    32, 32);
        JLabel menuPodcast = creerLabelMenu(null, "Podcasts", 32, 32);

        btnRetour = creerLabelMenu("assets/btn_retour.png", "Back", 32, 32);

        // Assemblage de la barre latérale
        bandeSelection.add(Box.createVerticalStrut(20));
        bandeSelection.add(logoJavasik);
        bandeSelection.add(Box.createVerticalStrut(20));
        bandeSelection.add(creerSeparateur());
        bandeSelection.add(labelLibrary);
        bandeSelection.add(profil);
        bandeSelection.add(librairie);
        bandeSelection.add(menuArtistes);
        bandeSelection.add(menuAlbums);
        bandeSelection.add(menuSongs);
        bandeSelection.add(Box.createVerticalStrut(12));
        bandeSelection.add(creerSeparateur());
        bandeSelection.add(labelDiscover);
        bandeSelection.add(menuBrowse);
        bandeSelection.add(menuForYou);
        bandeSelection.add(menuPopular);
        bandeSelection.add(menuRadio);
        bandeSelection.add(menuPodcast);
        bandeSelection.add(Box.createVerticalGlue()); // pousse btnRetour vers le bas
        bandeSelection.add(creerSeparateur());
        bandeSelection.add(Box.createVerticalStrut(10));
        bandeSelection.add(btnRetour);
        bandeSelection.add(Box.createVerticalStrut(10));

        //zone centrale ou on va pouvoir afficher les stats, proposition de morceau, playlist ect
        central = new JPanel(new BorderLayout());
        central.setBackground(new Color(26, 26, 26)); // On ajoute un padding en haut pour laisser la place à la barre de recherche
        central.setBorder(BorderFactory.createEmptyBorder(hauteur_barreDeRecherche, 0, 0, 0));
        central.setOpaque(false);

        //barre de lecture
        lecture = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 16));
        lecture.setPreferredSize(new Dimension(Ecran.LONGUEUR, 72));
        lecture.setBackground(new Color(18, 18, 18));
        lecture.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 50, 50)));// Placeholder : sera rempli par le contrôleur
        JLabel placeholderLecture = new JLabel("▶  Aucune lecture en cours");
        placeholderLecture.setForeground(new Color(160, 160, 160));
        placeholderLecture.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lecture.add(placeholderLecture);

        //Barre de rechercher
        zoneRecherche = new JPanel(null);
        zoneRecherche.setOpaque(false);
        zoneRecherche.setBounds(largeur_bandeauGauche, 0, largeur_barreDeRecherche, hauteur_barreDeRecherche + 64 + hauteur_filtre);

        // Barre de recherche avec fond arrondi simulé avec JPanel
        boutonsRecherche = new JPanel(new BorderLayout(8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                // Fond arrondi pour la barre de recherche
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(40, 40, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
            }
        };
        boutonsRecherche.setOpaque(false);
        boutonsRecherche.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12)); // On centre verticalement en ajoutant un padding
        boutonsRecherche.setBounds(8, 8, largeur_barreDeRecherche - 16, hauteur_barreDeRecherche - 16);

        // Champ texte stylisé
        barreRecherche = new JTextField();
        barreRecherche.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        barreRecherche.setBackground(new Color(40, 40, 40));// transparent
        barreRecherche.setForeground(new Color(255, 255, 255));
        barreRecherche.setCaretColor(new Color(255, 255, 255));
        barreRecherche.setFont(new Font("SansSerif", Font.PLAIN, 14));
        barreRecherche.putClientProperty("JTextField.placeholderText", "Search...");// Texte placeholder simulé

        // Icônes loupe et filtre
        loupe  = creerLabel("assets/loupe.png",  "", 24, 24);
        filtre = creerLabel("assets/filtre.png",  "",  24, 24);
        loupe.setForeground(new Color(160, 160, 160));
        filtre.setForeground(new Color(160, 160, 160));
        JPanel actionsRecherche = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionsRecherche.setOpaque(false);
        actionsRecherche.add(loupe);
        actionsRecherche.add(filtre);

        boutonsRecherche.add(barreRecherche, BorderLayout.CENTER);
        boutonsRecherche.add(actionsRecherche, BorderLayout.EAST);

        // Panneau filtre (caché par défaut)
        panelFiltre = new FenetreFiltre();
        int xPanneau = largeur_barreDeRecherche - largueur_filtre - 8;
        panelFiltre.setBounds(Math.max(0, xPanneau), largeur_barreDeRecherche, largueur_filtre, hauteur_filtre);
        panelFiltre.setVisible(false);
        zoneRecherche.add(boutonsRecherche);
        zoneRecherche.add(panelFiltre);

        //assemblage, mettre tout dans la base
        base.add(bandeSelection,BorderLayout.WEST);
        base.add(central,BorderLayout.CENTER);
        base.add(lecture,BorderLayout.SOUTH);

        //mettre dans les couches
        coucheContenu.add(base,JLayeredPane.DEFAULT_LAYER);
        coucheContenu.add(zoneRecherche,JLayeredPane.PALETTE_LAYER);
        panel.add(coucheContenu, BorderLayout.CENTER); // On ajoute uniquement coucheContenu
    }

    //methode pour creer un label + texte
    private JLabel creerLabelMenu(String cheminIcone, String texte, int w, int h) {
        JLabel label = creerLabel(cheminIcone, texte, w, h);
        label.setForeground(new Color(160, 160, 160));
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 8));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(220, h + 12)); //220 largeur x du conteneur gauche
        return label;
    }


    //Creer un sous titre dans la barre de choix gauche, librairie ou autre
    private JLabel creerLabelTitre(String titre) {
        JLabel label = new JLabel(titre);
        label.setForeground(new Color(100, 100, 100));
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setBorder(BorderFactory.createEmptyBorder(16, 16, 6, 8));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(220, 36));
        return label;
    }

    //creer un separateur dans le barre gauche
    private JSeparator creerSeparateur() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50, 50, 50));
        sep.setBackground(new Color(50, 50, 50));
        sep.setMaximumSize(new Dimension(220, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }

    //creer un label, appeler par par creer labelMenu, reidmensioner l'image et tt
    private JLabel creerLabel(String cheminIcone, String texte, int cibleLargeur, int cibleHauteur) {
        JLabel label;

        if (cheminIcone != null) {
            ImageIcon icon = new ImageIcon(cheminIcone);
            if (icon.getIconWidth() > 0) {
                int lO = icon.getIconWidth(), hO = icon.getIconHeight();
                double ratio = Math.min((double) cibleLargeur / lO, (double) cibleHauteur / hO);
                int nL = Math.max(1, (int) Math.round(lO * ratio));
                int nH = Math.max(1, (int) Math.round(hO * ratio));
                Image img = icon.getImage().getScaledInstance(nL, nH, Image.SCALE_SMOOTH);
                label = new JLabel(new ImageIcon(img));
                label.setText(texte.isEmpty() ? "" : "  " + texte); // espace entre icône et texte
                label.setHorizontalTextPosition(SwingConstants.RIGHT); // texte à DROITE de l'icône
                label.setHorizontalAlignment(SwingConstants.LEFT);     // tout aligné à gauche
            } else {
                label = new JLabel(texte, SwingConstants.LEFT);
            }
        } else {
            label = new JLabel(texte, SwingConstants.LEFT);
        }

        label.setPreferredSize(new Dimension(cibleLargeur , cibleHauteur));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }


    // GETTERS (utilisés par EvenementsVisite)
    public JFrame getFrame(){ return frame; }
    public void setFrame(JFrame f){ this.frame = f; }
    public JPanel getPanel(){ return panel; }
    public JLabel getProfil(){ return profil; }
    public JLabel getLibrairie() { return librairie; }
    public JLabel getBtnRetour(){ return btnRetour; }
    public JTextField getBarreRecherche()  { return barreRecherche; }
    public JLabel getFiltre(){ return filtre; }
    public JLabel getLoupe(){ return loupe; }
    public JPanel getPanelFiltre() { return panelFiltre; }
    public JPanel getBoutonsRecherche(){ return boutonsRecherche; }

    // methode publique appelées par EvenementsVisite, je vais changer ca
    // rend le panneau filtre visible
    public void afficherPanelFiltre() {
        panelFiltre.setVisible(true);
        zoneRecherche.revalidate();
        zoneRecherche.repaint();
        coucheContenu.repaint();
    }

    //Bascule la visibilité du panneau filtre
    public void basculerPanelFiltre() {
        panelFiltre.setVisible(!panelFiltre.isVisible());
        zoneRecherche.revalidate();
        zoneRecherche.repaint();
        coucheContenu.repaint();
    }

    // Cache le panneau filtre
    public void masquerPanelFiltre() {
        panelFiltre.setVisible(false);
        zoneRecherche.revalidate();
        zoneRecherche.repaint();
        coucheContenu.repaint();
    }

    // Renvoie les filtres sélectionnés
    public Filtre getFiltres() {
        return panelFiltre.getFiltre();
    }

    //Remplace le contenu de la zone centrale, Appelée par le contrôleur pour changer de "page" (résultats, profil, etc.)
    public void setPanelCentral(JComponent nouveauPanel) {
        central.removeAll();
        central.add(nouveauPanel, BorderLayout.CENTER);
        central.revalidate();
        central.repaint();
    }

    //Retire tous les MouseListeners ajoutés par EvenementsVisite
    private void enleverML(Component composant) {
        for (MouseListener listener : composant.getMouseListeners()) {
            if (listener.getClass().getName().startsWith("controleur.EvenementsVisite")) {
                composant.removeMouseListener(listener);
            }
        }
        if (composant instanceof Container) {
            for (Component c : ((Container) composant).getComponents()) enleverML(c);
        }
    }

    public void reinitialiserEvenements() {
        enleverML(profil);
        enleverML(librairie);
        enleverML(btnRetour);
        enleverML(loupe);
        enleverML(filtre);
        enleverML(barreRecherche);
        enleverML(panelFiltre);
    }

    // Vide la zone centrale et réinitialise la barre de recherche
    public void viderPanelCentral() {
        JPanel vide = new JPanel(new BorderLayout());
        vide.setOpaque(true);
        vide.setBackground(new Color(26, 26, 26));
        setPanelCentral(vide);
        barreRecherche.setText("");
    }
}