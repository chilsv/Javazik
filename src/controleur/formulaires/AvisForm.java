package controleur.formulaires;

import java.time.LocalDate;

import metier.Abonne;
import metier.Morceau;

public class AvisForm {
    public final Morceau morceau;
    public final Abonne abonne;
    public final int note;
    public final String commentaire;
    public final LocalDate date = LocalDate.now();

    public AvisForm(Morceau morceau, Abonne abonne, int note, String commentaire) {
        this.morceau = morceau;
        this.abonne = abonne;
        this.note = note;
        this.commentaire = commentaire;
    }

}
