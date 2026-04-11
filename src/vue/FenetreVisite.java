package vue;

import javax.swing.*;

public class FenetreVisite {
    private final JFrame frame;
    private final JLabel profil;
    private final JLabel librairie;

    public FenetreVisite() {
        frame = new JFrame("Fenêtre de visite");
        frame.setSize(1392, 768);
        frame.setLocationRelativeTo(null); //mettre au milieu
        frame.setUndecorated(true); //Enlever les bord de base de windows

        profil = new JLabel(new ImageIcon("assets/profil.png"));
        librairie = new JLabel(new ImageIcon("assets/librairie.png"));

        profil.setBounds(100, 100, 10, 10);
        librairie.setBounds(200, 100, 10, 10);

        frame.add(profil);
        frame.add(librairie);

        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JLabel getProfil() {
        return profil;
    }

    public JLabel getLibrairie() {
        return librairie;
    }
}
