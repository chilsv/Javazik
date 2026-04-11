package vue;

import javax.swing.*;

public class FenetreInscription {
    private final JFrame frame;
    private final JTextField champMail;
    private final JPasswordField champMdp;
    private final JButton btnValider;
    private final JLabel btnRetourLabel;

    public FenetreInscription() {
        frame = new JFrame();
        frame.setSize(1392, 768);
        frame.setLocationRelativeTo(null); //mettre au milieu
        frame.setUndecorated(true); //Enlever les bord de base de windows

        frame.getContentPane().setBackground(java.awt.Color.BLUE);
        frame.getContentPane().setLayout(null);

        // Composants bouton ect
        champMail = new JTextField();
        champMdp = new JPasswordField();
        btnValider = new JButton("S'inscrire");
        btnRetourLabel = new JLabel(new ImageIcon("assets/btn_retour.png"));

        // Positions
        champMail.setBounds(506, 300, 380, 40);
        champMdp.setBounds(506, 360, 380, 40);
        //btnValiderLabel.setBounds(506, 420, btnValiderLabel.getIcon().getIconWidth(), btnValiderLabel.getIcon().getIconHeight());
        btnValider.setBounds(506, 420, 380, 40);
        btnRetourLabel.setBounds(100, 100, 100, 100);

        frame.add(btnRetourLabel);
        frame.add(btnValider);
        frame.add(champMdp);
        frame.add(champMail);

        frame.setVisible(true);
    }

}
