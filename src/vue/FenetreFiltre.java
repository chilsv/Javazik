package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.JComponent;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import metier.Filtre;

public class FenetreFiltre extends JPanel {
    private static final int ANNEE_SPINNER_MIN = 0;
    private static final int ANNEE_MIN = 1920;
    private static final int ANNEE_MAX = LocalDate.now().getYear();

    private final JCheckBox morceau;
    private final JCheckBox artiste;
    private final JCheckBox album;
    private final JCheckBox playlist;
    private final JCheckBox croissant;
    private final JSpinner annee;
    private final RangeSlider anneeIntervalle;

    public FenetreFiltre() {
        setLayout(new java.awt.GridLayout(0, 1, 8, 8));
        setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)),
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(java.awt.Color.WHITE);
        setPreferredSize(new Dimension(330, 340));

        morceau = new JCheckBox("Morceaux", true);
        artiste = new JCheckBox("Artistes", true);
        album = new JCheckBox("Albums", false);
        playlist = new JCheckBox("Playlists", false);
        croissant = new JCheckBox("Trier par ordre croissant", false);
        annee = new JSpinner(new SpinnerNumberModel(0, ANNEE_SPINNER_MIN, ANNEE_MAX, 1));
        anneeIntervalle = new RangeSlider(ANNEE_MIN, ANNEE_MAX);
        anneeIntervalle.setPreferredSize(new Dimension(280, 92));

        annee.addChangeListener(e -> {
            int anneePrecise = (Integer) annee.getValue();
            anneeIntervalle.setEnabled(anneePrecise == 0);
            anneeIntervalle.repaint();
        });

        add(morceau);
        add(artiste);
        add(album);
        add(playlist);
        add(croissant);
        JPanel ligneAnnee = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        ligneAnnee.setOpaque(false);
        ligneAnnee.add(new JLabel("Annee :"));
        ligneAnnee.add(annee);
        add(ligneAnnee);
        add(anneeIntervalle);
    }

    public Filtre getFiltre() {
        int anneePrecise = (Integer) annee.getValue();
        int[] intervalle;

        if (anneePrecise > 0) {
            intervalle = new int[] {anneePrecise, anneePrecise};
        } else {
            int min = anneeIntervalle.getLowerValue();
            int max = anneeIntervalle.getUpperValue();
            if (min == ANNEE_MIN && max == ANNEE_MAX) {
                intervalle = new int[] {0, 0};
            } else {
                intervalle = new int[] {min, max};
            }
        }

        return new Filtre(
            morceau.isSelected(),
            artiste.isSelected(),
            album.isSelected(),
            playlist.isSelected(),
            croissant.isSelected(),
            intervalle
        );
    }

    private static class RangeSlider extends JComponent {
        private static final int PADDING = 12;
        private static final int THUMB_RADIUS = 5;

        private final int min;
        private final int max;
        private int lowerValue;
        private int upperValue;
        private boolean dragLower;
        private boolean dragUpper;

        RangeSlider(int min, int max) {
            this.min = min;
            this.max = max;
            this.lowerValue = min;
            this.upperValue = max;

            MouseAdapter mouse = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int x = e.getX();
                    int lowerX = valueToX(lowerValue);
                    int upperX = valueToX(upperValue);
                    dragLower = Math.abs(x - lowerX) <= Math.abs(x - upperX);
                    dragUpper = !dragLower;
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    dragLower = false;
                    dragUpper = false;
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!isEnabled()) {
                        return;
                    }
                    int value = xToValue(e.getX());
                    if (dragLower) {
                        lowerValue = Math.min(value, upperValue);
                    } else if (dragUpper) {
                        upperValue = Math.max(value, lowerValue);
                    }
                    repaint();
                }
            };

            addMouseListener(mouse);
            addMouseMotionListener(mouse);
        }

        int getLowerValue() {
            return lowerValue;
        }

        int getUpperValue() {
            return upperValue;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int y = getHeight() / 2 - 10;
            int start = PADDING;
            int end = getWidth() - PADDING;
            int lowerX = valueToX(lowerValue);
            int upperX = valueToX(upperValue);

            Color trackColor = isEnabled() ? new Color(180, 180, 180) : new Color(210, 210, 210);
            Color selectedColor = isEnabled() ? new Color(80, 130, 220) : new Color(170, 190, 230);

            g2.setStroke(new BasicStroke(4f));
            g2.setColor(trackColor);
            g2.drawLine(start, y, end, y);

            g2.setColor(selectedColor);
            g2.drawLine(lowerX, y, upperX, y);

            g2.setColor(isEnabled() ? new Color(70, 70, 70) : new Color(140, 140, 140));
            String bas = String.valueOf(lowerValue);
            String haut = String.valueOf(upperValue);
            int basW = g2.getFontMetrics().stringWidth(bas);
            int hautW = g2.getFontMetrics().stringWidth(haut);
            int texteY = y + THUMB_RADIUS + g2.getFontMetrics().getAscent() + 12;
            g2.drawString(bas, lowerX - basW / 2, texteY);
            g2.drawString(haut, upperX - hautW / 2, texteY);

            drawThumb(g2, lowerX, y, isEnabled());
            drawThumb(g2, upperX, y, isEnabled());
            g2.dispose();
        }

        private void drawThumb(Graphics2D g2, int x, int y, boolean enabled) {
            g2.setColor(enabled ? Color.WHITE : new Color(240, 240, 240));
            g2.fillOval(x - THUMB_RADIUS, y - THUMB_RADIUS, THUMB_RADIUS * 2, THUMB_RADIUS * 2);
            g2.setColor(new Color(90, 90, 90));
            g2.drawOval(x - THUMB_RADIUS, y - THUMB_RADIUS, THUMB_RADIUS * 2, THUMB_RADIUS * 2);
        }

        private int valueToX(int value) {
            int width = Math.max(1, getWidth() - 2 * PADDING);
            double t = (value - min) / (double) (max - min);
            return PADDING + (int) Math.round(t * width);
        }

        private int xToValue(int x) {
            int width = Math.max(1, getWidth() - 2 * PADDING);
            int clamped = Math.max(PADDING, Math.min(getWidth() - PADDING, x));
            double t = (clamped - PADDING) / (double) width;
            int value = min + (int) Math.round(t * (max - min));
            return Math.max(min, Math.min(max, value));
        }
    }

}
