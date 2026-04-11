package vue;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class FenetreInscription {
    private static final String PLACEHOLDER_NOM = "Nom";
    private static final String PLACEHOLDER_MAIL = "Mail";
    private static final String PLACEHOLDER_MDP = "Mot de passe";

    private JFrame frame;
    private final JPanel panel;
    private final JTextField champNom;
    private final JTextField champMail;
    private final JPasswordField champMdp;
    private final JButton btnValider;
    private final JLabel btnRetourLabel;

    public FenetreInscription() {
        panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(Ecran.LONGUEUR, Ecran.HAUTEUR));

        ImageIcon background = new ImageIcon("assets/Q.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setLayout(null);
        backgroundLabel.setBounds(0, 0, Ecran.LONGUEUR, Ecran.HAUTEUR);
        panel.add(backgroundLabel);

        int carteLongueur = 350;
        int carteLargeur = 310;
        int carteX = (Ecran.LONGUEUR - carteLongueur) / 2;
        int carteY = (Ecran.HAUTEUR - carteLargeur) / 2;

        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D carteForme = (Graphics2D) g.create();
                carteForme.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                carteForme.setColor(new Color(20, 28, 58, 210));
                carteForme.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                carteForme.dispose();
            }
        };
        card.setOpaque(false);
        card.setBounds(carteX, carteY, carteLongueur, carteLargeur);

        JLabel titre = new JLabel("S'inscrire", SwingConstants.CENTER);
        titre.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        titre.setForeground(Color.WHITE);
        titre.setBounds(0, 20, carteLongueur, 40);

        champNom = new JTextField();
        champNom.setBounds(30, 80, carteLongueur - 60, 40);
        styliserChamp(champNom, PLACEHOLDER_NOM);

        champMail = new JTextField();
        champMail.setBounds(30, 135, carteLongueur - 60, 40);
        styliserChamp(champMail, PLACEHOLDER_MAIL);

        champMdp = new JPasswordField();
        champMdp.setBounds(30, 190, carteLongueur - 60, 40);
        styliserChampPassword(champMdp, PLACEHOLDER_MDP);

        btnValider = new JButton("S'inscrire") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D boutonForme = (Graphics2D) g.create();
                boutonForme.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(74, 144, 226),
                    getWidth(), 0, new Color(52, 100, 200)
                );
                boutonForme.setPaint(gp);
                boutonForme.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                boutonForme.dispose();
                super.paintComponent(g);
            }
        };
        btnValider.setBounds(30, 245, carteLongueur - 60, 42);
        btnValider.setForeground(Color.WHITE);
        btnValider.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnValider.setOpaque(false);
        btnValider.setContentAreaFilled(false);
        btnValider.setBorderPainted(false);
        btnValider.setFocusPainted(false);
        btnValider.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRetourLabel = new JLabel(new ImageIcon("assets/btn_retour.png"));
        int btnRetourX = (carteLongueur - 80) / 2;
        int btnRetourY = carteLargeur - 40 - 15;
        btnRetourLabel.setBounds(btnRetourX, btnRetourY, 80, 40);
        btnRetourLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.add(titre);
        card.add(champNom);
        card.add(champMail);
        card.add(champMdp);
        card.add(btnValider);
        card.add(btnRetourLabel);

        backgroundLabel.add(card);
    }

    private void styliserChamp(JTextField champ, String placeholder) {
        champ.setBackground(new Color(40, 50, 80, 200));
        champ.setCaretColor(Color.WHITE);
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        champ.setBorder(new CompoundBorder(
            new LineBorder(new Color(80, 100, 160), 1, true),
            new EmptyBorder(0, 12, 0, 12)
        ));
        champ.setOpaque(true);

        champ.setText(placeholder);
        champ.setForeground(new Color(160, 160, 180));

        champ.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (champ.getText().equals(placeholder)) {
                    champ.setText("");
                    champ.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (champ.getText().isEmpty()) {
                    champ.setText(placeholder);
                    champ.setForeground(new Color(160, 160, 180));
                }
            }
        });
    }

    private void styliserChampPassword(JPasswordField champ, String placeholder) {
        champ.setBackground(new Color(40, 50, 80, 200));
        champ.setCaretColor(Color.WHITE);
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        champ.setBorder(new CompoundBorder(
            new LineBorder(new Color(80, 100, 160), 1, true),
            new EmptyBorder(0, 12, 0, 12)
        ));
        champ.setOpaque(true);

        champ.setEchoChar((char) 0);
        champ.setText(placeholder);
        champ.setForeground(new Color(160, 160, 180));

        champ.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(champ.getPassword()).equals(placeholder)) {
                    champ.setText("");
                    champ.setForeground(Color.WHITE);
                    champ.setEchoChar('●');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (champ.getPassword().length == 0) {
                    champ.setEchoChar((char) 0);
                    champ.setText(placeholder);
                    champ.setForeground(new Color(160, 160, 180));
                }
            }
        });
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
        if (champMail.getText().equals(PLACEHOLDER_MAIL)) {
            champMail.setText("");
        }
        return champMail;
    }

    public JPasswordField getChampMdp() {
        if (String.valueOf(champMdp.getPassword()).equals(PLACEHOLDER_MDP)) {
            champMdp.setText("");
        }
        return champMdp;
    }

    public JTextField getChampNom() {
        if (champNom.getText().equals(PLACEHOLDER_NOM)) {
            champNom.setText("");
        }
        return champNom;
    }

}
