package vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Fenetre {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setSize(1392, 768); //Dimension du fond
        frame.setLocationRelativeTo(null); //Centrer sur l'ordi
        frame.setUndecorated(true); //enlever les contours de la fenetre de base Windows


        // Mettre en place le background
        ImageIcon background = new ImageIcon("assets/Q.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setLayout(null);
        frame.setContentPane(backgroundLabel);

        // Inclusion du Logo
        ImageIcon logo = new ImageIcon("assets/logo.png");
        JLabel logoLabel = new JLabel(logo);

        //Inclusion du Bandeau
        ImageIcon bandeau = new ImageIcon("assets/bandeau.png");
        JLabel bandeauLabel = new JLabel(bandeau);

        //Inclusion du bouton Connexion/Creerun compte
        ImageIcon btnConnexion = new ImageIcon("assets/btn_connexion.png");
        JLabel btnConnexionLabel = new JLabel(btnConnexion);

        //Inclusion du bouton admin
        ImageIcon btnAdmin = new ImageIcon("assets/btn_admin.png");
        JLabel btnAdminLabel = new JLabel(btnAdmin);

        //Inclusion du bouton quitter
        ImageIcon btnQuitter = new ImageIcon("assets/btn_quitter.png");
        JLabel btnQuitterLabel = new JLabel(btnQuitter);

        // Définir la position et la taille exactes des elements
        logoLabel.setBounds(506, 194, logo.getIconWidth(), logo.getIconHeight()); // longueur de l'image : 380px, largeur de l'image : 380px -> centrer le logo 1392/2-380/2 pour x et 768/2-380/2 pour y
        bandeauLabel.setBounds(0, 0, bandeau.getIconWidth(), bandeau.getIconHeight());
        btnConnexionLabel.setBounds(1135, 109, btnConnexion.getIconWidth(), btnConnexion.getIconHeight()); // pour les x : 1392 - 247 - 10 (10px pour le paddding) pour les y (bandeau est de 99 de hauteur) 99 + 10px de padding
        btnAdminLabel.setBounds(1135, 199, btnAdmin.getIconWidth(), btnAdmin.getIconHeight()); //meme x que l'autre bouton, pour le y juste + 10 de padding + hauteur bouton
        btnQuitterLabel.setBounds(1135, 679, btnQuitter.getIconWidth(), btnQuitter.getIconHeight()); //meme x qui les deux autres, pour le y il est en bas dc c'est 768 - la hauteur du bouton (79) - 10px de padding

        // Ajouter les elements
        backgroundLabel.add(logoLabel);
        backgroundLabel.add(bandeauLabel);
        backgroundLabel.add(btnConnexionLabel);
        backgroundLabel.add(btnAdminLabel);
        backgroundLabel.add(btnQuitterLabel);


        // EVENEMENTS

        //Evenement pour que quand on clique sur le bouton quitter ca quitte
        MouseAdapter listenerQuitter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { frame.dispose(); } // si cliquer sur btn fermer la fenêtre
            @Override
            public void mouseEntered(MouseEvent e) { btnQuitterLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); }// le curseur se tranfome en main au dessus du bouton
        };

        //Evenement pour le bouton admin
        MouseAdapter listenerAdmin = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  } // si cliquer sur btn fermer la fenêtre
            @Override
            public void mouseEntered(MouseEvent e) { btnAdminLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); }// le curseur se tranfome en main au dessus du bouton
        };

        //Evenement pour le bouton Creer un compte/Connexion
        MouseAdapter listenerConnexion = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  } // si cliquer sur btn fermer la fenêtre
            @Override
            public void mouseEntered(MouseEvent e) { btnConnexionLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); }// le curseur se tranfome en main au dessus du bouton
        };

        //Evenement pour le bouton LOGO
        MouseAdapter listenerLogo = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  } // si cliquer sur btn fermer la fenêtre
            @Override
            public void mouseEntered(MouseEvent e) { logoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); }// le curseur se tranfome en main au dessus du bouton
        };

        // On ajoute le listener au bouton
        btnQuitterLabel.addMouseListener(listenerQuitter);
        btnAdminLabel.addMouseListener(listenerAdmin);
        btnConnexionLabel.addMouseListener(listenerConnexion);
        logoLabel.addMouseListener(listenerLogo);


        //Evenement pour deplacer le bandeau comme une vrai fenetre

        final Point[] position_fenetre = {null}; // Variables pour stocker la position initiale

        MouseAdapter listenerBandeau = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                position_fenetre[0] = e.getPoint(); // position ou j'ai cliqué dans le bandeau avec la souris
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                Point coordonees_actuels = e.getLocationOnScreen(); // position sur l'écran
                frame.setLocation(coordonees_actuels.x - position_fenetre[0].x,coordonees_actuels.y - position_fenetre[0].y);
            }
            @Override
            public void mouseEntered(MouseEvent e) { bandeauLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); }// le curseur se tranfome en main au dessus du bouton
        };

        bandeauLabel.addMouseListener(listenerBandeau);
        bandeauLabel.addMouseMotionListener(listenerBandeau);




        frame.setVisible(true);
    }
}
