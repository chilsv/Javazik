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
        JButton btnValider = fenetre.getBtnValider();
        JLabel btnRetour = fenetre.getBtnRetourLabel();

        // Valider → choix 1
        btnValider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(1);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                btnValider.setCursor(new Cursor(Cursor.HAND_CURSOR));

            }
        });

        // Quitter → choix 4
        btnRetour.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(2);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
    }

}




