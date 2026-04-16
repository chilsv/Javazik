package metier;

import java.time.LocalDate;

public class Avis {
    private Morceau morceau;
    private Abonne abonne;
    private int note;
    private String commentaire;
    private LocalDate date;

    public Avis(Morceau morceau, Abonne abonne, int note, String commentaire) {
        this.morceau = morceau;
        this.abonne = abonne;
        this.note = note;
        this.commentaire = commentaire;
        date = LocalDate.now();
    }

    public Morceau getMorceau() {
        return morceau;
    }

    public Abonne getAbonne() {
        return abonne;
    }

    public int getNote() {
        return note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public LocalDate getDate() {
        return date;
    }

}
