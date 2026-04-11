package vue;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class FenetreMenu {

    //defintion des elements qui vont composer notre fenetre
    private JFrame frame;
    private final JPanel panel;
    private final JLabel btnQuitterLabel;
    private final JLabel btnAdminLabel;
    private final JLabel btnConnexionLabel;
    private final JLabel logoLabel;
    private final JLabel bandeauLabel;


    public FenetreMenu (){

        panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(1392, 768));

        // Background
        ImageIcon background = new ImageIcon("assets/Q.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setLayout(null);
        backgroundLabel.setBounds(0, 0, 1392, 768);
        panel.add(backgroundLabel);


        // Composants bouton ect
        logoLabel = new JLabel(new ImageIcon("assets/logo.png"));
        bandeauLabel = new JLabel(new ImageIcon("assets/bandeau.png"));
        btnConnexionLabel = new JLabel(new ImageIcon("assets/btn_connexion.png"));
        btnAdminLabel = new JLabel(new ImageIcon("assets/btn_admin.png"));
        btnQuitterLabel = new JLabel(new ImageIcon("assets/btn_quitter.png"));


        // Positions
        logoLabel.setBounds(506, 194, logoLabel.getIcon().getIconWidth(), logoLabel.getIcon().getIconHeight()); // longueur de l'image : 380px, largeur de l'image : 380px -> centrer le logo 1392/2-380/2 pour x et 768/2-380/2 pour y
        bandeauLabel.setBounds(0, 0, bandeauLabel.getIcon().getIconWidth(), bandeauLabel.getIcon().getIconHeight());
        btnConnexionLabel.setBounds(1135, 109, btnConnexionLabel.getIcon().getIconWidth(), btnConnexionLabel.getIcon().getIconHeight());// pour les x : 1392 - 247 - 10 (10px pour le paddding) pour les y (bandeau est de 99 de hauteur) 99 + 10px de padding
        btnAdminLabel.setBounds(1135, 199, btnAdminLabel.getIcon().getIconWidth(), btnAdminLabel.getIcon().getIconHeight());//meme x que l'autre bouton, pour le y juste + 10 de padding + hauteur bouton
        btnQuitterLabel.setBounds(1135, 679, btnQuitterLabel.getIcon().getIconWidth(), btnQuitterLabel.getIcon().getIconHeight());//meme x qui les deux autres, pour le y il est en bas dc c'est 768 - la hauteur du bouton (79) - 10px de padding


        // Ajouter les elements
        backgroundLabel.add(logoLabel);
        backgroundLabel.add(bandeauLabel);
        backgroundLabel.add(btnConnexionLabel);
        backgroundLabel.add(btnAdminLabel);
        backgroundLabel.add(btnQuitterLabel);



    }

    // Getters pour accéder aux composants, notamment car on a besoins de leur taille
    public JFrame getFrame() { return frame; }
    public void setFrame(JFrame frame) { this.frame = frame; }
    public JPanel getPanel() { return panel; }
    public JLabel getBtnQuitterLabel() { return btnQuitterLabel; }
    public JLabel getBtnAdminLabel() { return btnAdminLabel; }
    public JLabel getBtnConnexionLabel() { return btnConnexionLabel; }
    public JLabel getLogoLabel() { return logoLabel; }
    public JLabel getBandeauLabel() { return bandeauLabel; }
}



