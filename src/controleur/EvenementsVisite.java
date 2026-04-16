
package controleur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controleur.exceptions.ActionException;
import vue.FenetreVisite;

public class EvenementsVisite {

    public interface VisiteListener {
        void onChoix(int choix);
    }

    //Constantes des choix
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

    public static void ajouterEvenements(FenetreVisite fenetre, VisiteListener listener, boolean filtreVisible) {

        // Barre latérale gauche section librairie listener
        ajouterBoutonLateral(fenetre.getProfil(), fenetre, listener, CHOIX_PROFIL,true);
        ajouterBoutonLateral(fenetre.getLibrairie() ,fenetre, listener, CHOIX_PLAYLISTS,true);
        ajouterBoutonLateral(fenetre.getMenuArtistes(), fenetre, listener, CHOIX_ARTISTES,true);
        ajouterBoutonLateral(fenetre.getMenuAlbums(),fenetre, listener, CHOIX_ALBUMS, true);
        ajouterBoutonLateral(fenetre.getMenuMorceaux(),fenetre, listener, CHOIX_MORCEAUX,true);

        // arre latérale gauche section decouverte listener
        ajouterBoutonLateral(fenetre.getParcourir() ,fenetre, listener, CHOIX_PARCOURIR,false);
        ajouterBoutonLateral(fenetre.getPourVous(),fenetre, listener, CHOIX_POUR_VOUS,false);
        ajouterBoutonLateral(fenetre.getPopulaire(),fenetre, listener, CHOIX_POPULAIRE,false);
        ajouterBoutonLateral(fenetre.getRadio(), fenetre, listener, CHOIX_RADIO, false);
        ajouterBoutonLateral(fenetre.getPodcasts(),fenetre, listener, CHOIX_PODCASTS,false);

        //retour
        fenetre.getBtnRetour().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_RETOUR); }
            @Override public void mouseEntered(MouseEvent e) {
                fenetre.getBtnRetour().setCursor(new Cursor(Cursor.HAND_CURSOR));
                surbriller(fenetre.getBtnRetour(), true);
            }
            @Override public void mouseExited(MouseEvent e) {
                surbriller(fenetre.getBtnRetour(), false);
            }
        });

        // zone de recherche des sons
        JLabel filtre = fenetre.getFiltre();
        JLabel loupe  = fenetre.getLoupe();
        JTextField barreRecherche = fenetre.getBarreRecherche();
        
        //detection souris filtre
        filtre.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_FILTRE); }
            @Override public void mouseEntered(MouseEvent e) {
                if (filtreVisible) fenetre.afficherPanelFiltre();
                filtre.setCursor(new Cursor(Cursor.HAND_CURSOR));
                surbriller(filtre, true);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (filtreVisible) masquerFiltre(fenetre, fenetre.getPanelFiltre());
                surbriller(filtre, false);
            }
        });

        //detection souris recherche
        loupe.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_LOUPE); }
            @Override public void mouseEntered(MouseEvent e) {
                loupe.setCursor(new Cursor(Cursor.HAND_CURSOR));
                surbriller(loupe, true);
            }
            @Override public void mouseExited(MouseEvent e) { surbriller(loupe, false); }
        });

        // Masquer le filtre quand la souris entre dans la barre de recherche
        barreRecherche.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (filtreVisible) fenetre.masquerPanelFiltre();
            }
        });

        if (filtreVisible) {passageSouris(fenetre.getPanelFiltre(), fenetre, "filtre");}
    }


    //Ajoute un listener hover + click sur un bouton de la barre latérale et avenir est true, un ActionException "À venir" est affiché au clic.
    private static void ajouterBoutonLateral(JLabel label, FenetreVisite fenetre, VisiteListener listener, int choix, boolean avenir) {
        if (label == null) return;
        label.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (avenir) fenetre.afficherErreur(new ActionException("À venir"));
                listener.onChoix(choix);
            }
            @Override public void mouseEntered(MouseEvent e) {
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                label.setForeground(new Color(220, 220, 220)); // éclairci au survol
            }
            @Override public void mouseExited(MouseEvent e) {
                label.setForeground(new Color(160, 160, 160)); // retour couleur normale
            }
        });
    }

    //surbriller pour + de style et plus lisible
    private static void surbriller(JLabel label, boolean actif) {
        label.setForeground(actif ? new Color(220, 220, 220) : new Color(160, 160, 160));
    }

    // Affiche/masque le panneau filtre selon la position de la souris
    private static void passageSouris(Component composant, FenetreVisite fenetre, String type) {
        composant.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { // si la souris est sur le composant on l'affiche
                if (type.equals("filtre")) {
                    System.out.println("Afficher le panneau de filtre");
                    fenetre.afficherPanelFiltre();
                }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (type.equals("filtre")) fenetre.masquerPanelFiltre();
            }
        });
        if (composant instanceof Container) {
            for (Component enfant : ((Container) composant).getComponents()) {
                passageSouris(enfant, fenetre, type);
            }
        }
    }

    //masquer filtre si souris pas desssus
    private static void masquerFiltre(FenetreVisite fenetre, JComponent zone) {
        SwingUtilities.invokeLater(() -> {
            boolean surPanneau = estSurvole(zone);
            boolean surBoutonsRecherche = estSurvole(fenetre.getBoutonsRecherche());
            boolean surBarre = estSurvole(fenetre.getBarreRecherche());
            if (surBarre || (!surPanneau && !surBoutonsRecherche)) { fenetre.masquerPanelFiltre(); }
        });
    }

    //methode pour si souirs dessus sur composant
    private static boolean estSurvole(JComponent composant) {
        if (!composant.isShowing()) return false;
        PointerInfo pointeur = MouseInfo.getPointerInfo();
        if (pointeur == null) return false;
        Point souris = pointeur.getLocation();
        Point origine = composant.getLocationOnScreen();
        return new Rectangle(origine.x, origine.y, composant.getWidth(), composant.getHeight()).contains(souris);
    }
}