package controleur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import vue.FenetreConnexion;

public class EvenementsConnexion {

    public interface ConnexionListener {
        void onChoix(int choix);
    }

    public static void ajouterEvenements(FenetreConnexion fenetre, ConnexionListener listener) {
        JFrame frame = fenetre.getFrame();
        JButton btnValider = fenetre.getBtnValider();
        JLabel btnRetour = fenetre.getBtnRetourLabel();

        // Valider → choix 1
        btnValider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(1);
                frame.dispose();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                btnValider.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        // Retour → choix 2
        btnRetour.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(2);
                frame.dispose();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
    }

}
