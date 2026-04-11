package controleur;

import vue.FenetreVisite;

public class EvenementsVisite {

    public interface VisiteListener {
        void onChoix(int choix);
    }

    public static void ajouterEvenements(FenetreVisite fenetre, VisiteListener listener) {
        // Ici, vous pouvez ajouter des listeners pour les composants de la fenêtre de visite
        // Par exemple, si vous avez un bouton "Retour" dans la fenêtre de visite, vous pouvez faire :
        /*
        JButton btnRetour = fenetre.getBtnRetour();
        btnRetour.addActionListener(e -> {
            listener.onChoix(1); // 1 pour retour au menu principal
            fenetre.getFrame().dispose(); // Ferme la fenêtre de visite
        });
         */
    }

}
