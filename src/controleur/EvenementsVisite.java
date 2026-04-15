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
        JTextField barreRecherche = fenetre.getBarreRecherche();
        JLabel menuArtistes = fenetre.getMenuArtistes();
        JLabel menuAlbums = fenetre.getMenuAlbums();
        JLabel menuMorceaux = fenetre.getMenuMorceaux();

        // Profil choix 1
        profil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fenetre.afficherErreur(new ActionException("A venir"));
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
                fenetre.afficherErreur(new ActionException("A venir"));
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
            public void mouseClicked(MouseEvent e) { // quand on clique sur le filtre
                listener.onChoix(4);
            }
            @Override
            public void mouseEntered(MouseEvent e) { // quand on apsse la souris sur le filtre
                if (filtreVisible) { // si c'est pas un visiteur
                    fenetre.afficherPanelFiltre();
                }
                filtre.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) { // quand la souris est plus sur le fitlre
                if (filtreVisible) {
                    // on masque le filtre, mais il est pas forcément masqué visuellement
                    // si la souris est toujours sur le panel, il le filtre va rester visiible
                    masquerFiltre(fenetre, fenetre.getPanelFiltre());
                }
            }
        });

        if (filtreVisible) {
            // autres coniditions pour afficher le panneau des filtres
            passageSouris(fenetre.getPanelFiltre(), fenetre, "filtre");
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

        // j'ai rajouté ça sinon ça bug
        // si la souris est sur la barre de recherche le panneau filtres disparait
        barreRecherche.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (filtreVisible) {
                    fenetre.masquerPanelFiltre();
                }
            }
        });

        // Artistes choix 6
        menuArtistes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fenetre.afficherErreur(new ActionException("A venir"));
                listener.onChoix(6);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                menuArtistes.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        // Albums choix 7
        menuAlbums.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fenetre.afficherErreur(new ActionException("A venir"));
                listener.onChoix(7);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                menuAlbums.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        // Morceaux choix 8
        menuMorceaux.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fenetre.afficherErreur(new ActionException("A venir"));
                listener.onChoix(8);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                menuMorceaux.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

    }

    /* si la souris est sur ce composant on l'affiche */
    private static void passageSouris(Component composant, FenetreVisite fenetre, String type) {
        composant.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { // si la souris est sur le composant on l'affiche
                if (type.equals("filtre")) {
                    System.out.println("Afficher le panneau de filtre");
                    fenetre.afficherPanelFiltre();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) { // si la souris part, on masque le panneau
                if (type.equals("filtre")) {
                    fenetre.masquerPanelFiltre();
                }
            }
        });

        // on fait ça récursivement pour les composants du composants
        // en rgos les composants du panneau sont considérés comme exéterieurs
        // donc le panneau disparaitrait si on faisait pas ça
        if (composant instanceof Container) {
            Container container = (Container) composant;
            for (Component enfant : container.getComponents()) {
                passageSouris(enfant, fenetre, type);
            }
        }
    }

    /* si la souris passe en dehors, le panneau disparait :) (c'était long) */
    private static void masquerFiltre(FenetreVisite fenetre, JComponent zone) {
        SwingUtilities.invokeLater(() -> {
            boolean surPanneau = estSurvole(zone);
            boolean surBoutonsRecherche = estSurvole(fenetre.getBoutonsRecherche());
            boolean surBarre = estSurvole(fenetre.getBarreRecherche());

            if (surBarre) {
                fenetre.masquerPanelFiltre();
                return;
            }
            if (!surPanneau && !surBoutonsRecherche) {
                fenetre.masquerPanelFiltre();
            }
        });
    }

    private static boolean estSurvole(JComponent composant) {
        if (!composant.isShowing()) {
            return false;
        }

        // d'après la docu c'est ça
        PointerInfo pointeur = MouseInfo.getPointerInfo();
        if (pointeur == null) {
            return false;
        }

        Point positionSouris = pointeur.getLocation();
        Point positionComposant = composant.getLocationOnScreen();
        // on vérifie si la souris est dans le composant
        Rectangle zone = new Rectangle(positionComposant.x, positionComposant.y, composant.getWidth(), composant.getHeight());
        return zone.contains(positionSouris);
    }
}
