package controleur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import metier.Abonne;
import metier.Personne;
import metier.Visiteur;
import vue.FenetreVisite;
import vue.InterfaceVue;

public class EvenementsVisite {

    public interface VisiteListener {
        void onChoix(int choix);
    }

    public static void ajouterEvenements(FenetreVisite fenetre, VisiteListener listener) {
        // pour les visiteurs
        ajouterEvenements(fenetre, listener, false);
    }

    public static void ajouterEvenements(FenetreVisite fenetre, VisiteListener listener, boolean filtreVisible) {
        JLabel profil = fenetre.getProfil();
        JLabel librairie = fenetre.getLibrairie();
        JLabel btnRetour = fenetre.getBtnRetour();
        JLabel loupe = fenetre.getLoupe();
        JLabel filtre = fenetre.getFiltre();
        JPanel panelFiltre = fenetre.getPanelFiltre();
        JTextField barreRecherche = fenetre.getBarreRecherche();

        // Profil choix 1
        profil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(1);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                profil.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        // Librairie choix 2
        librairie.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(2);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                librairie.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        // Retour choix 3
        btnRetour.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(3);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        // Filtre choix 4
        filtre.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(4);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (filtreVisible) { // si c'est pas un visiteur
                    fenetre.afficherPanelFiltre();
                }
                filtre.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (filtreVisible) {
                    masquerFiltre(fenetre, fenetre.getPanelFiltre());
                }
            }
        });

        if (filtreVisible) {
            installerSurvolRecursif(fenetre.getPanelFiltre(), fenetre);
        }

        // Loupe choix 5
        loupe.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(5);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                loupe.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        // pour effacer le panneau où sont les filtres
        barreRecherche.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (filtreVisible) {
                    fenetre.masquerPanelFiltre();
                }
            }
        });
    }

    private static void installerSurvolRecursif(Component composant, FenetreVisite fenetre) {
        composant.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fenetre.afficherPanelFiltre();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                masquerFiltre(fenetre, fenetre.getPanelFiltre());
            }
        });

        if (composant instanceof Container) {
            Container container = (Container) composant;
            for (Component enfant : container.getComponents()) {
                installerSurvolRecursif(enfant, fenetre);
            }
        }
    }

    /* si la souris passe en dehors, le panneau disparait :) (c'était long) */
    private static void masquerFiltre(FenetreVisite fenetre, JComponent zone) {
        SwingUtilities.invokeLater(() -> {
            boolean surPanneau = estSurvole(zone);
            boolean surLigneGrise = estSurvole(fenetre.getBoutonsRecherche());
            boolean surChampTexte = estSurvole(fenetre.getBarreRecherche());

            if (surChampTexte) {
                fenetre.masquerPanelFiltre();
                return;
            }

            if (!surPanneau && !surLigneGrise) {
                fenetre.masquerPanelFiltre();
            }
        });
    }

    private static boolean estSurvole(JComponent composant) {
        if (!composant.isShowing()) {
            return false;
        }

        PointerInfo pointeur = MouseInfo.getPointerInfo();
        if (pointeur == null) {
            return false;
        }

        Point positionEcran = pointeur.getLocation();
        Point origine = composant.getLocationOnScreen();
        Rectangle zone = new Rectangle(origine.x, origine.y, composant.getWidth(), composant.getHeight());
        return zone.contains(positionEcran);
    }
}
