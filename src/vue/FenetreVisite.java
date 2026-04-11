package vue;

import javax.swing.*;

public class FenetreVisite {
    private final JFrame frame;
    private final JLabel profil;

    public FenetreVisite() {
        frame = new JFrame("Fenêtre de visite");
        frame.setSize(1392, 768);
        frame.setLocationRelativeTo(null); //mettre au milieu
        frame.setUndecorated(true); //Enlever les bord de base de windows

        profil = new JLabel(new ImageIcon("assets/profil.png"));

        profil.setBounds(100, 100, 100, 100);
        
        frame.add(profil);

        frame.setVisible(true);
    }

}
