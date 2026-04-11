package controleur.formulaires;

// Formulaire pour créer un artiste
public final class ArtisteForm {
    public final String nom;
    public String type = "solo";

    public ArtisteForm(String nom) {
        this.nom = nom;
    }

    public ArtisteForm(String nom, String type) {
        this.nom = nom;
        this.type = type;
    }
}