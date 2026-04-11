package vue;

import javax.swing.*;

public class FenetreInscription {
    private JFrame frame;
    private final JPanel panel;
    private final JTextField champNom;
    private final JTextField champMail;
    private final JPasswordField champMdp;
    private final JButton btnValider;
    private final JLabel btnRetourLabel;

    public FenetreInscription() {
        panel = new JPanel(null);
        panel.setPreferredSize(new java.awt.Dimension(1392, 768));

        panel.setBackground(java.awt.Color.BLUE);

        // Composants bouton ect
        champNom = new JTextField();
        champMail = new JTextField();
        champMdp = new JPasswordField();
        btnValider = new JButton("S'inscrire");
        btnRetourLabel = new JLabel(new ImageIcon("assets/btn_retour.png"));

        // Positions
        champNom.setBounds(506, 260, 380, 40);
        champMail.setBounds(506, 300, 380, 40);
        champMdp.setBounds(506, 360, 380, 40);
        //btnValiderLabel.setBounds(506, 420, btnValiderLabel.getIcon().getIconWidth(), btnValiderLabel.getIcon().getIconHeight());
        btnValider.setBounds(506, 420, 380, 40);
        btnRetourLabel.setBounds(100, 100, 100, 100);

        panel.add(btnRetourLabel);
        panel.add(btnValider);
        panel.add(champMdp);
        panel.add(champMail);
        panel.add(champNom);
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

    public JButton getBtnValider() {
        return btnValider;
    }

    public JLabel getBtnRetourLabel() {
        return btnRetourLabel;
    }

    public JTextField getChampMail() {
        return champMail;
    }

    public JPasswordField getChampMdp() {
        return champMdp;
    }

    public JTextField getChampNom() {
        return champNom;
    }

}
