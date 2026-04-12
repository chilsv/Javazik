package metier;

import java.io.Serializable;

// Interface pour les morceaux, artistes etc...
public interface TypeObjets extends Serializable {
    int getAnnee();
    String getNom();
    String getImage();
}
