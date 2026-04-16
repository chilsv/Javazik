package metier;

public class Avis {
    private int numMoreau;
    private int numAbonne;
    private int note;
    private int commentaire;
    private int date;

    public Avis(int numMorceau, int numAbonne, int note, int commentaire, int date) {
        this.numMoreau = numMorceau;
        this.numAbonne = numAbonne;
        this.note = note;
        this.commentaire = commentaire;
        this.date = date;
    }

    public int getNumMoreau() {
        return numMoreau;
    }

    public int getNumAbonne() {
        return numAbonne;
    }

    public int getNote() {
        return note;
    }

    public int getCommentaire() {
        return commentaire;
    }

    public int getDate() {
        return date;
    }

}
