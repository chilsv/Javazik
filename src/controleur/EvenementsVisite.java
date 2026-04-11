package controleur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import vue.FenetreVisite;

public class EvenementsVisite {

    public interface VisiteListener {
        void onChoix(int choix);
    }

    public static void ajouterEvenements(FenetreVisite fenetre, VisiteListener listener) {
        JLabel profil = fenetre.getProfil();
        JLabel librairie = fenetre.getLibrairie();

        // Profil choix 1
        profil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(1);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                profil.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        // Librairie choix 2
        librairie.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onChoix(2);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                librairie.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
    }
}
