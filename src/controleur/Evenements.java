package controleur;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import vue.FenetreMenu;

public class Evenements {

    public static void ajouterEvenements(FenetreMenu fenetre) {
        JFrame frame = fenetre.getFrame();
        JLabel btnQuitter = fenetre.getBtnQuitterLabel();
        JLabel btnAdmin = fenetre.getBtnAdminLabel();
        JLabel btnConnexion = fenetre.getBtnConnexionLabel();
        JLabel logo = fenetre.getLogoLabel();
        JLabel bandeau = fenetre.getBandeauLabel();

        // Quitter
        btnQuitter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { frame.dispose(); }
            @Override
            public void mouseEntered(MouseEvent e) { btnQuitter.setCursor(new Cursor(Cursor.HAND_CURSOR)); }
        });

        // Admin
        btnAdmin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  }
            @Override
            public void mouseEntered(MouseEvent e) { btnAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR)); }
        });

        // Connexion
        btnConnexion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  }
            @Override
            public void mouseEntered(MouseEvent e) { btnConnexion.setCursor(new Cursor(Cursor.HAND_CURSOR)); }
        });

        // Logo
        logo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  }
            @Override
            public void mouseEntered(MouseEvent e) { logo.setCursor(new Cursor(Cursor.HAND_CURSOR)); }
        });

        // Déplacement du bandeau
        final Point[] positionFenetre = {null};
        MouseAdapter bandeauListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { positionFenetre[0] = e.getPoint(); }
            @Override
            public void mouseDragged(MouseEvent e) {
                Point coord = e.getLocationOnScreen();
                frame.setLocation(coord.x - positionFenetre[0].x, coord.y - positionFenetre[0].y);
            }
            @Override
            public void mouseEntered(MouseEvent e) { bandeau.setCursor(new Cursor(Cursor.HAND_CURSOR)); }
        };

        bandeau.addMouseListener(bandeauListener);
        bandeau.addMouseMotionListener(bandeauListener);

    }
}
