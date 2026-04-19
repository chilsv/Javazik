package vue;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class FenetreConnexion {
    private static final String PLACEHOLDER_MAIL = "Mail";
    private static final String PLACEHOLDER_MDP = "Mot de passe";

    private JFrame frame;
    private final JPanel panel;
    private final JTextField champMail;
    private final JPasswordField champMdp;
    private final JButton btnValider;
    private final JLabel btnRetourLabel;

    public FenetreConnexion() {

        panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(Ecran.LONGUEUR, Ecran.HAUTEUR));

        // Background
        ImageIcon background = new ImageIcon("assets/Q.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setLayout(null);
        backgroundLabel.setBounds(0, 0, Ecran.LONGUEUR, Ecran.HAUTEUR);
        panel.add(backgroundLabel);

        // ── Carte centrale (panneau semi-transparent) ──
        // Dimensions de la carte
        int carteLongeur = 340;
        int carteLargeur = 260;
        int carteX = (Ecran.LONGUEUR - carteLongeur) / 2; // centré horizontalement
        int CarteY = (Ecran.HAUTEUR - carteLargeur) / 2;  // centré verticalement

        //Creation d'une carte ou l'on va afficher nom d'utilisateur password bouton valider et retour
        JPanel card = new JPanel(null) { //mettre le panel a NULL pour pouvoir placer les elemnts nous meme avec des coordoones
            @Override
            protected void paintComponent(Graphics g) {  //methode pour changer de forme couleur un composant
                Graphics2D carteForme = (Graphics2D) g.create();
                carteForme.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //lisse les bords -> + style
                // Fond semi-transparent bleu-noir pour + de style
                carteForme.setColor(new Color(20, 28, 58));  //couleur
                carteForme.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18); //rectangle plus coin arrondie a 18
                carteForme.dispose();
            }
        };
        card.setOpaque(false);
        card.setBounds(carteX, CarteY, carteLongeur, carteLargeur);


        //Texte login
        JLabel titre = new JLabel("Login", SwingConstants.CENTER);// Centre + login
        titre.setFont(new Font("Segoe UI", Font.PLAIN, 26));//font
        titre.setForeground(Color.WHITE);
        titre.setBounds(80, 20, carteLongeur - 2 * 80, 40);//placement en evitant le bouton sur le coté avec une marge de 80




        // creation du champ username
        champMail = new JTextField();
        champMail.setBounds(30, 80, carteLongeur - 60, 40); //placement
        styliserChamp(champMail, PLACEHOLDER_MAIL);//ce qu'il y'a ecrit dans le champs a remplir avec un efocntion defini en dessous

        // creation du champ password
        champMdp = new JPasswordField();
        champMdp.setBounds(30, 135, carteLongeur - 60, 40);//placement
        styliserChampPassword(champMdp, PLACEHOLDER_MDP);//ce qu'il y'a ecrit dans le champs a remplir avec un efocntion defini en dessous

        // creation du bouton se connecter
        btnValider = new JButton("Se connecter") {
            @Override
            protected void paintComponent(Graphics g) { //changement de forme couleur comme tout a l'heure
                Graphics2D BoutonForme = (Graphics2D) g.create();
                BoutonForme.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //lisser les bords
                // Dégradé bleu
                GradientPaint gp = new GradientPaint( //focntion qui permet de faire un degrade de couleur (trouver sur internet je trouvais ca stylé)
                        0, 0, new Color(74, 144, 226),
                        getWidth(), 0, new Color(52, 100, 200)
                );
                BoutonForme.setPaint(gp); //peinddre le bouton avec le degradé
                BoutonForme.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                BoutonForme.dispose();
                super.paintComponent(g); //mettre dessus le texte
            }
        };
        btnValider.setBounds(30, 195, carteLongeur - 60, 42); //placement
        btnValider.setForeground(Color.WHITE);
        btnValider.setFont(new Font("Segoe UI", Font.BOLD, 14)); //font
        btnValider.setOpaque(false); //fonction pour rendre le bouton mieux
        btnValider.setContentAreaFilled(false); //fonction pour rendre le bouton mieux
        btnValider.setBorderPainted(false); //fonction pour rendre le bouton mieux
        btnValider.setFocusPainted(false); //fonction pour rendre le bouton mieux
        btnValider.setCursor(new Cursor(Cursor.HAND_CURSOR));//placr le curseur en mode main au dessus du botuon pour que l'utilisateur sache qu'il peut cliquer


        ImageIcon icon = new ImageIcon("assets/btn_retour.png"); //chercher l'image
        Image img = icon.getImage(); //defeinir une nouvelle image que l'onva redimensuionner
        Image newImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); //redimension
        btnRetourLabel = new JLabel(new ImageIcon(newImg)); //affciher l'iamge redomasionner
        btnRetourLabel.setBounds(20,10, 50, 50);


        // ajouter sur la carte
        card.add(titre);
        card.add(champMail);
        card.add(champMdp);
        card.add(btnValider);
        card.add(btnRetourLabel);


        //ajouter la carte sur le fond
        backgroundLabel.add(card);


    }

    //fonction fait avec chat wallah j'arrivais pas
    private void styliserChamp(JTextField champ, String placeholder) {
        // Style visuel
        champ.setBackground(new Color(40, 48, 78));
        champ.setCaretColor(Color.WHITE);
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        champ.setBorder(new CompoundBorder(
                new LineBorder(new Color(80, 100, 160), 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));

        // État initial : placeholder affiché en gris
        champ.setText(placeholder);
        champ.setForeground(new Color(160, 160, 180));

        // Comportement au focus
        champ.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // L'utilisateur clique dans le champ
                if (champ.getText().equals(placeholder)) {
                    champ.setText("");                        // efface le placeholder
                    champ.setForeground(Color.WHITE);         // texte blanc pour la saisie
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // L'utilisateur clique ailleurs
                if (champ.getText().isEmpty()) {
                    champ.setText(placeholder);               // remet le placeholder
                    champ.setForeground(new Color(160, 160, 180)); // remet le gris
                }
            }
        });
    }

    private void styliserChampPassword(JPasswordField champ, String placeholder) {
        // Style visuel
        champ.setBackground(new Color(40, 48, 78));
        champ.setCaretColor(Color.WHITE);
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        champ.setBorder(new CompoundBorder(
                new LineBorder(new Color(80, 100, 160), 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        champ.setOpaque(true);

        // État initial : placeholder visible en clair + gris
        champ.setEchoChar((char) 0);                          // désactive le masquage
        champ.setText(placeholder);
        champ.setForeground(new Color(160, 160, 180));

        champ.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(champ.getPassword()).equals(placeholder)) {
                    champ.setText("");
                    champ.setForeground(Color.WHITE);
                    champ.setEchoChar('●');                   // active le masquage
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (champ.getPassword().length == 0) {
                    champ.setEchoChar((char) 0);              // désactive le masquage
                    champ.setText(placeholder);
                    champ.setForeground(new Color(160, 160, 180));
                }
            }
        });
    }

    public JFrame getFrame() { return frame; }  //GETTERS pour la taille
    public void setFrame(JFrame frame) { this.frame = frame; }
    public JPanel getPanel() { return panel; }
    public JButton getBtnValider() { return btnValider; }  //GETTERS pour la taille
    public JLabel getBtnRetourLabel() { return btnRetourLabel; }  //GETTERS pour la taille
    public JTextField getChampMail() { return champMail; }  //GETTERS pour la taille
    
    public JPasswordField getChampMdp() {
        if (String.valueOf(champMdp.getPassword()).equals(PLACEHOLDER_MDP)) {
            champMdp.setText("");
        }
        return champMdp;
    }
}
