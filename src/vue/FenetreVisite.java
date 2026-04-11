package vue;

import javax.swing.*;
import java.awt.*;

import metier.Filtre;

public class FenetreVisite {
    private static final int ICON_TARGET_WIDTH = 72;
    private static final int ICON_TARGET_HEIGHT = 72;
    private static final int BANDE_SELECTION_LARGEUR = 110;
    private static final int ZONE_RECHERCHE_X = BANDE_SELECTION_LARGEUR;
    private static final int ZONE_RECHERCHE_Y = 8;
    private static final int ZONE_RECHERCHE_LARGEUR = Ecran.LONGUEUR - BANDE_SELECTION_LARGEUR;
    private static final int ZONE_RECHERCHE_HAUTEUR = 60;
    private static final int PANNEAU_FILTRE_Y = ZONE_RECHERCHE_HAUTEUR;
    private static final int PANNEAU_FILTRE_LARGEUR = 330;
    private static final int PANNEAU_FILTRE_HAUTEUR = 340;

    private JFrame frame;
    private final JPanel panel;
    private final JLayeredPane coucheContenu;
    private final JPanel base;
    private final JPanel central; // tout le reste

    private final JPanel bandeSelection; // Bande à gauche : compte, librairie, etc
    private final JPanel lecture; // bande en bas : play pause like et tout

    private final JPanel zoneRecherche;
    private final JPanel ligneRecherche;
    private final JTextField barreRecherche;
    private final FenetreFiltre panneauFiltre;

    private final JLabel profil;
    private final JLabel librairie;
    private final JLabel loupe;
    private final JLabel filtre;
    private final JLabel btnRetour;

    public FenetreVisite() {
        // On crée le panel princiapl qui contient tout
        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(Ecran.LONGUEUR, Ecran.HAUTEUR));

        // lui aussi contient tout. en gros panel contient coucheContenu, et ça permet d'afficher des trucs au-dessus de coucheContenu (ex: panneau de filtre)
        coucheContenu = new JLayeredPane();
        coucheContenu.setPreferredSize(new Dimension(Ecran.LONGUEUR, Ecran.HAUTEUR));
        coucheContenu.setLayout(null);

        // base contient tout sauf la barre de recherche
        base = new JPanel(new BorderLayout());
        base.setBounds(0, 0, Ecran.LONGUEUR, Ecran.HAUTEUR);

        // contient tout ce qui concerne la recherche
        zoneRecherche = new JPanel(null);
        zoneRecherche.setOpaque(false);
        zoneRecherche.setBounds(ZONE_RECHERCHE_X, ZONE_RECHERCHE_Y, ZONE_RECHERCHE_LARGEUR, ZONE_RECHERCHE_HAUTEUR + PANNEAU_FILTRE_Y + PANNEAU_FILTRE_HAUTEUR);

        central = new JPanel(new BorderLayout());
        central.setBackground(Color.WHITE);

        // bande à gauche
        bandeSelection = new JPanel();
        bandeSelection.setLayout(new BoxLayout(bandeSelection, BoxLayout.Y_AXIS));
        bandeSelection.setPreferredSize(new Dimension(BANDE_SELECTION_LARGEUR, Ecran.HAUTEUR));
        bandeSelection.setBackground(new Color(220, 240, 250));

        // bande de lecture en bas
        lecture = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        lecture.setPreferredSize(new Dimension(Ecran.LONGUEUR, 70));
        lecture.setBackground(Color.LIGHT_GRAY);

        profil = creerLabel("assets/profil.png", "Profil", ICON_TARGET_WIDTH, ICON_TARGET_HEIGHT);
        librairie = creerLabel("assets/librairie.png", "Librairie", ICON_TARGET_WIDTH, ICON_TARGET_HEIGHT);
        btnRetour = creerLabel("assets/btn_retour.png", "Retour", ICON_TARGET_WIDTH, ICON_TARGET_HEIGHT);
        loupe = creerLabel("assets/loupe.png", "Rechercher", 32, 32);
        filtre = creerLabel("assets/filtre.png", "", 32, 32);
        panneauFiltre = new FenetreFiltre();
        panneauFiltre.setVisible(false);

        // là ou on écrit
        barreRecherche = new JTextField();
        barreRecherche.setPreferredSize(new Dimension(200, 32));

        ligneRecherche = new JPanel(new BorderLayout(12, 10));
        ligneRecherche.setBackground(Color.LIGHT_GRAY);
        ligneRecherche.setBounds(0, 0, ZONE_RECHERCHE_LARGEUR, ZONE_RECHERCHE_HAUTEUR);

        JPanel actionsRecherche = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        actionsRecherche.setOpaque(false);
        actionsRecherche.add(loupe);
        actionsRecherche.add(filtre);

        ligneRecherche.add(actionsRecherche, BorderLayout.EAST);
        ligneRecherche.add(barreRecherche, BorderLayout.CENTER);
        zoneRecherche.add(ligneRecherche);

        int xPanneau = ZONE_RECHERCHE_LARGEUR - PANNEAU_FILTRE_LARGEUR;
        panneauFiltre.setBounds(Math.max(0, xPanneau), PANNEAU_FILTRE_Y, PANNEAU_FILTRE_LARGEUR, PANNEAU_FILTRE_HAUTEUR);
        panneauFiltre.setVisible(false);
        zoneRecherche.add(panneauFiltre);

        bandeSelection.add(Box.createVerticalStrut(20));
        bandeSelection.add(profil);
        bandeSelection.add(Box.createVerticalStrut(20));
        bandeSelection.add(librairie);
        bandeSelection.add(Box.createVerticalStrut(20));
        bandeSelection.add(btnRetour);
        bandeSelection.add(Box.createVerticalGlue());

        central.add(new JLabel("", SwingConstants.CENTER), BorderLayout.CENTER);
        lecture.add(new JLabel());

        base.add(bandeSelection, BorderLayout.WEST);
        base.add(central, BorderLayout.CENTER);
        base.add(lecture, BorderLayout.SOUTH);

        coucheContenu.add(base, JLayeredPane.DEFAULT_LAYER);
        coucheContenu.add(zoneRecherche, JLayeredPane.PALETTE_LAYER);

        panel.add(coucheContenu, BorderLayout.CENTER);
    }

    /*
     * Pour créer un JLabel 
     */
    private JLabel creerLabel(String cheminIcone, String texte, int cibleLargeur, int cibleHauteur) {
        ImageIcon icon = new ImageIcon(cheminIcone);
        JLabel label;

        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            int largeurOriginale = icon.getIconWidth();
            int hauteurOriginale = icon.getIconHeight();
            double ratio = Math.min((double) cibleLargeur / largeurOriginale, (double) cibleHauteur / hauteurOriginale);
            int nouvelleLargeur = Math.max(1, (int) Math.round(largeurOriginale * ratio));
            int nouvelleHauteur = Math.max(1, (int) Math.round(hauteurOriginale * ratio));

            Image imageRedimensionnee = icon.getImage().getScaledInstance(nouvelleLargeur, nouvelleHauteur, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(imageRedimensionnee));
            label.setPreferredSize(new Dimension(cibleLargeur, cibleHauteur));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
        } else {
            label = new JLabel(texte, SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(cibleLargeur, cibleHauteur));
        }

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JLabel getProfil() {
        return profil;
    }

    public JLabel getLibrairie() {
        return librairie;
    }

    public JLabel getBtnRetour() {
        return btnRetour;
    }

    public JTextField getBarreRecherche() {
        return barreRecherche;
    }

    public JLabel getFiltre() {
        return filtre;
    }

    public JLabel getLoupe() {
        return loupe;
    }

    public JPanel getPanneauFiltre() {
        return panneauFiltre;
    }

    public JPanel getLigneRecherche() {
        return ligneRecherche;
    }

    /* pour afficher les filtres quand on passe la souris dessus */
    public void afficherPanneauFiltre() {
        panneauFiltre.setVisible(true);
        zoneRecherche.revalidate();
        zoneRecherche.repaint();
        coucheContenu.repaint();
    }

    /* pour enlever les filtres quand la souris est pas dessu */
    public void basculerPanneauFiltre() {
        panneauFiltre.setVisible(!panneauFiltre.isVisible());
        zoneRecherche.revalidate();
        zoneRecherche.repaint();
        coucheContenu.repaint();
    }

    /* ça cache les filtres */
    public void masquerPanneauFiltre() {
        panneauFiltre.setVisible(false);
        zoneRecherche.revalidate();
        zoneRecherche.repaint();
        coucheContenu.repaint();
    }

    public Filtre getFiltreSelectionne() {
        return panneauFiltre.getFiltre();
    }
}
