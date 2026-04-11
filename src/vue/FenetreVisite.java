package vue;

import javax.swing.*;

public class FenetreVisite {
    private JFrame frame;
    private final JPanel panel;
    private final JLabel profil;
    private final JLabel librairie;

    public FenetreVisite() {
        panel = new JPanel(null);
        panel.setPreferredSize(new java.awt.Dimension(1392, 768));

        profil = new JLabel(new ImageIcon("assets/profil.png"));
        librairie = new JLabel(new ImageIcon("assets/librairie.png"));

        profil.setBounds(100, 100, 10, 10);
        librairie.setBounds(200, 100, 10, 10);

        panel.add(profil);
        panel.add(librairie);
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
}
