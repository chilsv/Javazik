
package controleur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controleur.exceptions.ActionException;
import vue.FenetreVisite;

public class EvenementsVisite {

    public interface VisiteListener {
        void onChoix(int choix);
    }

    //Constantes des choix
    public static final int CHOIX_PROFIL = 1;
    public static final int CHOIX_PLAYLISTS = 2;
    public static final int CHOIX_RETOUR = 3;
    public static final int CHOIX_FILTRE = 4;
    public static final int CHOIX_LOUPE = 5;
    public static final int CHOIX_ARTISTES = 6;
    public static final int CHOIX_ALBUMS = 7;
    public static final int CHOIX_MORCEAUX = 8;
    public static final int CHOIX_PARCOURIR = 9;
    public static final int CHOIX_POUR_VOUS = 10;
    public static final int CHOIX_POPULAIRE= 11;
    public static final int CHOIX_RADIO = 12;
    public static final int CHOIX_PODCASTS = 13;

    public static void ajouterEvenements(FenetreVisite fenetre, VisiteListener listener, boolean filtreVisible) {

        // Barre latérale gauche section librairie listener
        JLabel profil = fenetre.getProfil();
        if (profil != null) {
            profil.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    fenetre.afficherErreur(new ActionException("À venir"));
                    listener.onChoix(CHOIX_PROFIL);
                }
                @Override public void mouseEntered(MouseEvent e) {
                    profil.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    profil.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    profil.setForeground(new Color(160, 160, 160));
                }
            });
        }

        JLabel librairie = fenetre.getLibrairie();
        if (librairie != null) {
            librairie.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    listener.onChoix(CHOIX_PLAYLISTS);
                }
                @Override public void mouseEntered(MouseEvent e) {
                    librairie.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    librairie.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    librairie.setForeground(new Color(160, 160, 160));
                }
            });
        }

        JLabel menuArtistes = fenetre.getMenuArtistes();
        if (menuArtistes != null) {
            menuArtistes.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    listener.onChoix(CHOIX_ARTISTES);
                }
                @Override public void mouseEntered(MouseEvent e) {
                    menuArtistes.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    menuArtistes.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    menuArtistes.setForeground(new Color(160, 160, 160));
                }
            });
        }

        JLabel menuAlbums = fenetre.getMenuAlbums();
        if (menuAlbums != null) {
            menuAlbums.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    listener.onChoix(CHOIX_ALBUMS);
                }
                @Override public void mouseEntered(MouseEvent e) {
                    menuAlbums.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    menuAlbums.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    menuAlbums.setForeground(new Color(160, 160, 160));
                }
            });
        }

        JLabel menuMorceaux = fenetre.getMenuMorceaux();
        if (menuMorceaux != null) {
            menuMorceaux.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    listener.onChoix(CHOIX_MORCEAUX);
                }
                @Override public void mouseEntered(MouseEvent e) {
                    menuMorceaux.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    menuMorceaux.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    menuMorceaux.setForeground(new Color(160, 160, 160));
                }
            });
        }

        // arre latérale gauche section decouverte listener
        JLabel parcourir = fenetre.getParcourir();
        if (parcourir != null) {
            parcourir.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_PARCOURIR); }
                @Override public void mouseEntered(MouseEvent e) {
                    parcourir.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    parcourir.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    parcourir.setForeground(new Color(160, 160, 160));
                }
            });
        }

        JLabel pourVous = fenetre.getPourVous();
        if (pourVous != null) {
            pourVous.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_POUR_VOUS); }
                @Override public void mouseEntered(MouseEvent e) {
                    pourVous.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    pourVous.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    pourVous.setForeground(new Color(160, 160, 160));
                }
            });
        }

        JLabel populaire = fenetre.getPopulaire();
        if (populaire != null) {
            populaire.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_POPULAIRE); }
                @Override public void mouseEntered(MouseEvent e) {
                    populaire.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    populaire.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    populaire.setForeground(new Color(160, 160, 160));
                }
            });
        }

        // la radio pffff
        JLabel radio = fenetre.getRadio();
        if (radio != null) {
            radio.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_RADIO); }
                @Override public void mouseEntered(MouseEvent e) {
                    radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    radio.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    radio.setForeground(new Color(160, 160, 160));
                }
            });
        }

        // podscats
        JLabel podcasts = fenetre.getPodcasts();
        if (podcasts != null) {
            podcasts.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_PODCASTS); }
                @Override public void mouseEntered(MouseEvent e) {
                    podcasts.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    podcasts.setForeground(new Color(220, 220, 220));
                }
                @Override public void mouseExited(MouseEvent e) {
                    podcasts.setForeground(new Color(160, 160, 160));
                }
            });
        }

        //retour
        fenetre.getBtnRetour().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_RETOUR); }
            @Override public void mouseEntered(MouseEvent e) {
                fenetre.getBtnRetour().setCursor(new Cursor(Cursor.HAND_CURSOR));
                surbriller(fenetre.getBtnRetour(), true);
            }
            @Override public void mouseExited(MouseEvent e) {
                surbriller(fenetre.getBtnRetour(), false);
            }
        });

        // zone de recherche des sons
        JLabel filtre = fenetre.getFiltre();
        JLabel loupe  = fenetre.getLoupe();
        JTextField barreRecherche = fenetre.getBarreRecherche();
        
        //detection souris filtre
        filtre.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_FILTRE); }
            @Override public void mouseEntered(MouseEvent e) {
                if (filtreVisible) fenetre.afficherPanelFiltre();
                filtre.setCursor(new Cursor(Cursor.HAND_CURSOR));
                surbriller(filtre, true);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (filtreVisible) masquerFiltre(fenetre, fenetre.getPanelFiltre());
                surbriller(filtre, false);
            }
        });

        //detection souris recherche
        loupe.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { listener.onChoix(CHOIX_LOUPE); }
            @Override public void mouseEntered(MouseEvent e) {
                loupe.setCursor(new Cursor(Cursor.HAND_CURSOR));
                surbriller(loupe, true);
            }
            @Override public void mouseExited(MouseEvent e) { surbriller(loupe, false); }
        });

        // Masquer le filtre quand la souris entre dans la barre de recherche
        barreRecherche.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (filtreVisible) fenetre.masquerPanelFiltre();
            }
        });

        if (filtreVisible) {passageSouris(fenetre.getPanelFiltre(), fenetre, "filtre");}
    }

    //surbriller pour + de style et plus lisible
    private static void surbriller(JLabel label, boolean actif) {
        label.setForeground(actif ? new Color(220, 220, 220) : new Color(160, 160, 160));
    }

    // Affiche/masque le panneau filtre selon la position de la souris
    private static void passageSouris(Component composant, FenetreVisite fenetre, String type) {
        composant.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { // si la souris est sur le composant on l'affiche
                if (type.equals("filtre")) {
                    System.out.println("Afficher le panneau de filtre");
                    fenetre.afficherPanelFiltre();
                }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (type.equals("filtre")) fenetre.masquerPanelFiltre();
            }
        });
        if (composant instanceof Container) {
            for (Component enfant : ((Container) composant).getComponents()) {
                passageSouris(enfant, fenetre, type);
            }
        }
    }

    //masquer filtre si souris pas desssus
    private static void masquerFiltre(FenetreVisite fenetre, JComponent zone) {
        SwingUtilities.invokeLater(() -> {
            boolean surPanneau = estSurvole(zone);
            boolean surBoutonsRecherche = estSurvole(fenetre.getBoutonsRecherche());
            boolean surBarre = estSurvole(fenetre.getBarreRecherche());
            if (surBarre || (!surPanneau && !surBoutonsRecherche)) { fenetre.masquerPanelFiltre(); }
        });
    }

    //methode pour si souirs dessus sur composant
    private static boolean estSurvole(JComponent composant) {
        if (!composant.isShowing()) return false;
        PointerInfo pointeur = MouseInfo.getPointerInfo();
        if (pointeur == null) return false;
        Point souris = pointeur.getLocation();
        Point origine = composant.getLocationOnScreen();
        return new Rectangle(origine.x, origine.y, composant.getWidth(), composant.getHeight()).contains(souris);
    }
}