package vue;
import javax.swing.*;
import java.awt.*;

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

        //Evenement pour que quanson clique sur le bouton quitter ca quitte
        btnQuitterLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                frame.dispose();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnQuitterLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        frame.setVisible(true);


        //JLabel infoLabel = new JLabel("Logo Width: " + bandeau.getIconWidth() + " Height: " + bandeau.getIconHeight());
        //infoLabel.setBounds(20, 20, 300, 30); // Position en haut à gauche
        //infoLabel.setForeground(java.awt.Color.RED); // Visible sur fond
        //backgroundLabel.add(infoLabel);


        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
