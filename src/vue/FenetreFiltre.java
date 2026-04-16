package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicSpinnerUI;

import metier.Filtre;

public class FenetreFiltre extends JPanel {

    // Couleur utilisé dans genetre visiter charte graphique, reeutiliser les meme pour coherence
    private static final Color BG_PANEL = new Color(28, 28, 28);
    private static final Color BG_ITEM = new Color(40, 40, 40);
    private static final Color BG_ITEM_HOVER= new Color(50, 50, 50);
    private static final Color BORDER_COLOR = new Color(50, 50, 50);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY= new Color(160, 160, 160);
    private static final Color ACCENT = new Color(80, 130, 220);
    private static final Color TRACK_OFF= new Color(70, 70, 70);
    private static final int   RADIUS = 8;

    private static final int ANNEE_SPINNER_MIN = 0;
    private static final int ANNEE_MIN = 1920;
    private static final int ANNEE_MAX = LocalDate.now().getYear();

    // definition de ce que on va mettre dans le filtre
    private final DarkCheckBox Pop;
    private final DarkCheckBox Rock;
    private final DarkCheckBox Rap;
    private final DarkCheckBox Variete;
    private final DarkRadioButton radioCroissant;
    private final DarkRadioButton radioDecroissant;
    private final JSpinner    annee;
    private final RangeSlider anneeIntervalle;


    public FenetreFiltre() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(RADIUS, BORDER_COLOR), BorderFactory.createEmptyBorder(14, 14, 14, 14))); //definition du plceent
        setBackground(BG_PANEL);
        setOpaque(true);
        setPreferredSize(new Dimension(310, 410));

        // on creer les Checkboxes et les boutons de tri
        Pop = new DarkCheckBox("Morceaux",true);
        Rock = new DarkCheckBox("Artistes",true);
        Rap = new DarkCheckBox("Albums",false);
        Variete = new DarkCheckBox("Playlists",false);
        radioCroissant = new DarkRadioButton("Croissant",true);
        radioDecroissant= new DarkRadioButton("Décroissant",false);
        ButtonGroup groupeTri = new ButtonGroup();
        groupeTri.add(radioCroissant);
        groupeTri.add(radioDecroissant);

        // def spiner et slider
        annee = new JSpinner(new SpinnerNumberModel(0, ANNEE_SPINNER_MIN, ANNEE_MAX, 1));
        styliserSpinner(annee);

        anneeIntervalle = new RangeSlider(ANNEE_MIN, ANNEE_MAX);
        anneeIntervalle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        anneeIntervalle.setPreferredSize(new Dimension(270, 60));
        anneeIntervalle.setOpaque(false);
        anneeIntervalle.setAlignmentX(LEFT_ALIGNMENT);

        //spinner desactive pas slider
        // Slider remet spinner à 0
        anneeIntervalle.addChangeCallback(() -> annee.setValue(0));

        // on met dans l'odre
        add(sectionTitre("FILTRER PAR TYPE"));
        add(espaceV(4));
        add(Pop);
        add(Rock);
        add(Rap);
        add(Variete);
        add(espaceV(10));
        add(separateur());
        add(espaceV(8));
        add(sectionTitre("TRI PAR ANNÉE"));
        add(espaceV(4));
        JPanel ligneTri = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        ligneTri.setOpaque(false);
        ligneTri.setAlignmentX(LEFT_ALIGNMENT);
        ligneTri.add(radioCroissant);
        ligneTri.add(radioDecroissant);
        add(ligneTri);
        add(espaceV(10));
        add(separateur());
        add(espaceV(8));
        add(sectionTitre("ANNÉE"));
        add(espaceV(6));
        JPanel ligneSpinner = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        ligneSpinner.setOpaque(false);
        ligneSpinner.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lblAnnee = new JLabel("Année précise :");
        lblAnnee.setForeground(TEXT_SECONDARY);
        lblAnnee.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ligneSpinner.add(lblAnnee);
        ligneSpinner.add(annee);
        add(ligneSpinner);
        add(espaceV(14));
        add(anneeIntervalle);
    }


    //comment est les titre des sous sectoion
    private JLabel sectionTitre(String titre) {
        JLabel lbl = new JLabel(titre);
        lbl.setForeground(new Color(100, 100, 100));
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    //separateur, liigne qui separe
    private JPanel separateur() {
        JPanel sep = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BORDER_COLOR);
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        sep.setOpaque(false);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        return sep;
    }

    //faire un espace
    private javax.swing.Box.Filler espaceV(int hauteur) {
        return (javax.swing.Box.Filler) javax.swing.Box.createVerticalStrut(hauteur);
    }

    //fait avec ia pour styliser le spinner j'ai pas reussi
    private void styliserSpinner(JSpinner spinner) {
        spinner.setPreferredSize(new Dimension(80, 28));
        spinner.setBackground(BG_ITEM);
        spinner.setForeground(TEXT_PRIMARY);
        spinner.setFont(new Font("SansSerif", Font.PLAIN, 13));
        spinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 2)
        ));
        spinner.setUI(new BasicSpinnerUI() {
            @Override protected java.awt.Component createNextButton() { return makeArrowBtn("▲", true);}
            @Override protected java.awt.Component createPreviousButton(){ return makeArrowBtn("▼", false);}

            private javax.swing.JButton makeArrowBtn(String txt, boolean isNext) {
                javax.swing.JButton b = new javax.swing.JButton(txt) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(getModel().isRollover() ? BG_ITEM_HOVER : BG_ITEM);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.setColor(getModel().isRollover() ? TEXT_PRIMARY : TEXT_SECONDARY);
                        g2.setFont(getFont());
                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        java.awt.FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                        g2.dispose();
                    }
                };
                b.setFont(new Font("SansSerif", Font.PLAIN, 8));
                b.setPreferredSize(new Dimension(18, 14));
                b.setFocusPainted(false);
                b.setBorderPainted(false);
                b.setContentAreaFilled(false);
                b.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                if (isNext) installNextButtonListeners(b);
                else installPreviousButtonListeners(b);
                return b;
            }
        });
        if (spinner.getEditor() instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor ed = (JSpinner.DefaultEditor) spinner.getEditor();
            ed.getTextField().setBackground(BG_ITEM);
            ed.getTextField().setForeground(TEXT_PRIMARY);
            ed.getTextField().setCaretColor(TEXT_PRIMARY);
            ed.getTextField().setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
            ed.getTextField().setFont(new Font("SansSerif", Font.PLAIN, 13));
        }
    }

    // Getter du filtre
    public Filtre getFiltre() {
        int anneePrecise = (Integer) annee.getValue();
        int[] intervalle;
        if (anneePrecise > 0) {
            intervalle = new int[]{anneePrecise, anneePrecise};
        } else {
            int lo = anneeIntervalle.getLowerValue();
            int hi = anneeIntervalle.getUpperValue();
            intervalle = (lo == ANNEE_MIN && hi == ANNEE_MAX) ? new int[]{0, 0} : new int[]{lo, hi};
        }
        return new Filtre(
                Pop.isSelected(), Rock.isSelected(),
                Rap.isSelected(),   Variete.isSelected(),
                radioCroissant.isSelected(),   // true = croissant, false = décroissant
                intervalle
        );
    }


    // Icône partagée checkbox est carre et radio est cercle
    private static class BoxIcon implements javax.swing.Icon {
        private final boolean checked, round;
        BoxIcon(boolean checked, boolean round) { this.checked = checked; this.round = round; }

        @Override public int getIconWidth()  { return 16; }
        @Override public int getIconHeight() { return 16; }

        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(checked ? ACCENT : BG_ITEM);
            if (round) g2.fillOval(x, y, 16, 16);
            else g2.fillRoundRect(x, y, 16, 16, 4, 4);

            g2.setColor(checked ? ACCENT : BORDER_COLOR);
            g2.setStroke(new BasicStroke(1.2f));
            if (round) g2.drawOval(x, y, 15, 15);
            else g2.drawRoundRect(x, y, 15, 15, 4, 4);

            if (checked) {
                g2.setColor(Color.WHITE);
                if (round) {
                    g2.fillOval(x + 5, y + 5, 6, 6);
                }
                else
                {
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x + 3, y + 8, x + 6, y + 11);
                    g2.drawLine(x + 6, y + 11, x + 12, y + 5);
                }
            }
            g2.dispose();
        }
    }


    // styliser les checkbox avec nos couleur de chartergraphique
    private static class DarkCheckBox extends JCheckBox {
        DarkCheckBox(String texte, boolean sel) {
            super(texte, sel);
            setOpaque(false);
            setForeground(TEXT_SECONDARY);
            setFont(new Font("SansSerif", Font.PLAIN, 13));
            setFocusPainted(false);
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));
            setAlignmentX(LEFT_ALIGNMENT);
            setIcon(new BoxIcon(false, false));
            setSelectedIcon(new BoxIcon(true, false));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { setForeground(TEXT_PRIMARY); }
                @Override public void mouseExited(MouseEvent e)  { setForeground(TEXT_SECONDARY); }
            });
        }
    }


    // styliser les boutons avec nos couleur de chartergraphique
    private static class DarkRadioButton extends JRadioButton {
        DarkRadioButton(String texte, boolean sel) {
            super(texte, sel);
            setOpaque(false);
            setForeground(TEXT_SECONDARY);
            setFont(new Font("SansSerif", Font.PLAIN, 13));
            setFocusPainted(false);
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));
            setIcon(new BoxIcon(false, true));
            setSelectedIcon(new BoxIcon(true, true));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { setForeground(TEXT_PRIMARY); }
                @Override public void mouseExited(MouseEvent e)  { setForeground(TEXT_SECONDARY); }
            });
        }
    }

    // Slider avec deux poignées indépendantes, fait avec claude  la ca me depasse
    private static class RangeSlider extends JComponent {
        private static final int PAD    = 12;
        private static final int THUMB  = 6;

        private final int min, max;
        private int lowerValue, upperValue;
        private boolean dragLower, dragUpper;
        private Runnable changeCallback;

        void addChangeCallback(Runnable r) { this.changeCallback = r; }

        RangeSlider(int min, int max) {
            this.min = min; this.max = max;
            lowerValue = min; upperValue = max;

            MouseAdapter m = new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    if (!isEnabled()) return;
                    int x = e.getX();
                    int dLo = Math.abs(x - toX(lowerValue));
                    int dHi = Math.abs(x - toX(upperValue));
                    // Cas où les deux poignées sont confondues : on choisit selon côté
                    if (dLo == dHi) { dragLower = x <= toX(lowerValue); dragUpper = !dragLower; }
                    else { dragLower = dLo < dHi; dragUpper = !dragLower; }
                }
                @Override public void mouseReleased(MouseEvent e) { dragLower = dragUpper = false; }
                @Override public void mouseDragged(MouseEvent e) {
                    if (!isEnabled()) return;
                    int v = toVal(e.getX());
                    if (dragLower) lowerValue = Math.min(v, upperValue);
                    else if (dragUpper) upperValue = Math.max(v, lowerValue);
                    if (changeCallback != null) changeCallback.run();
                    repaint();
                }
            };
            addMouseListener(m);
            addMouseMotionListener(m);
        }

        int getLowerValue() { return lowerValue; }
        int getUpperValue() { return upperValue; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int trackY = 16; // Y de la piste
            int lx= toX(lowerValue);
            int ux= toX(upperValue);
            Color trackCol = isEnabled() ? TRACK_OFF : new Color(55, 55, 55);
            Color selCol = isEnabled() ? ACCENT: new Color(60, 90, 160);
            g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));// Piste fond
            g2.setColor(trackCol);
            g2.drawLine(PAD, trackY, getWidth() - PAD, trackY);
            g2.setColor(selCol);// Piste sélectionnée
            g2.drawLine(lx, trackY, ux, trackY);
            drawThumb(g2, lx, trackY, isEnabled());// Poignées
            drawThumb(g2, ux, trackY, isEnabled());
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));// Labels — anti-chevauchement
            g2.setColor(isEnabled() ? TEXT_SECONDARY : TRACK_OFF);
            String sLo = String.valueOf(lowerValue);
            String sHi = String.valueOf(upperValue);
            java.awt.FontMetrics fm = g2.getFontMetrics();
            int wLo = fm.stringWidth(sLo);
            int wHi = fm.stringWidth(sHi);
            int ty  = trackY + THUMB + fm.getAscent() + 4;
            int xLo = lx - wLo / 2;
            int xHi = ux - wHi / 2;
            if (xHi - xLo < wLo + 4) xLo = xHi - wLo - 4;// Si les labels se chevauchent, on pousse le label gauche à gauche
            xLo = Math.max(PAD, xLo);// Clamp dans le composant
            xHi = Math.min(getWidth() - PAD - wHi, xHi);
            g2.drawString(sLo, xLo, ty);
            g2.drawString(sHi, xHi, ty);
            g2.dispose();
            //CAUCHEMAR CE CODE
        }

        //sous focntion de spinner
        private void drawThumb(Graphics2D g2, int x, int y, boolean en) {
            g2.setColor(new Color(80, 130, 220, 40));
            g2.fillOval(x - THUMB - 3, y - THUMB - 3, (THUMB + 3) * 2, (THUMB + 3) * 2);
            g2.setColor(en ? Color.WHITE : new Color(100, 100, 100));
            g2.fillOval(x - THUMB, y - THUMB, THUMB * 2, THUMB * 2);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(en ? ACCENT : TRACK_OFF);
            g2.drawOval(x - THUMB, y - THUMB, THUMB * 2, THUMB * 2);
        }

        private int toX(int val) {
            int w = Math.max(1, getWidth() - 2 * PAD);
            return PAD + (int) Math.round((val - min) / (double) (max - min) * w);
        }

        private int toVal(int x) {
            int w = Math.max(1, getWidth() - 2 * PAD);
            int c = Math.max(PAD, Math.min(getWidth() - PAD, x));
            return Math.max(min, Math.min(max, min + (int) Math.round((c - PAD) / (double) w * (max - min))));
        }
    }


    // stylier bordure arrondie
    private static class RoundedBorder implements javax.swing.border.Border {
        private final int r; private final Color c;
        RoundedBorder(int r, Color c) { this.r = r; this.c = c; }
        @Override public void paintBorder(java.awt.Component comp, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(x, y, w - 1, h - 1, r, r);
            g2.dispose();
        }
        @Override public java.awt.Insets getBorderInsets(java.awt.Component c) { return new java.awt.Insets(r, r, r, r); }
        @Override public boolean isBorderOpaque() { return false; }
    }
}