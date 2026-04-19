package controleur;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import metier.Album;
import metier.Artiste;
import metier.Catalogue;
import metier.Morceau;
import metier.Playlist;
import metier.Solo;

public class ImportMusiques {
    public static String path = "C:\\Users\\ilanb\\Documents\\Cours\\Ecole\\ING3\\POO_Java\\cours\\Javazik\\morceaux\\Faded.mp3";
    public static ArrayList<Morceau> morceauxImportes = new ArrayList<Morceau>();
    public static ArrayList<Artiste> artistesImportes = new ArrayList<Artiste>();
    public static ArrayList<Album> albumsImportes = new ArrayList<Album>();
    public static ArrayList<Playlist> playlistsImportees = new ArrayList<Playlist>();
    public static Catalogue catalogue;

    public ImportMusiques() {
        Main.charger(morceauxImportes, "morceaux.ser");
        Main.charger(playlistsImportees, "playlists.ser");
        Main.charger(artistesImportes, "artistes.ser");
        Main.charger(albumsImportes, "albums.ser");
        catalogue = new Catalogue(morceauxImportes, playlistsImportees, artistesImportes, albumsImportes);

        Path file = Path.of(path);
        if (!Files.exists(file)) {
            return;
        }

        try {
            byte[] fichierEnBytes = Files.readAllBytes(file);
            // pour verifier si y'a une balise id3V2
            if (fichierEnBytes.length < 10) {
                return;
            }

            if (!"ID3".equals(new String(fichierEnBytes, 0, 3, StandardCharsets.ISO_8859_1))) {
                System.out.println("pas de tag ID3v2");
                return;
            }

            int versionMajor = fichierEnBytes[3] & 0xFF;
            int versionRevision = fichierEnBytes[4] & 0xFF;
            // ça j'ai trouvé sur stackoverflow mais pas compris ahahah
            int tailleTag = lireSynchsafeInt(fichierEnBytes, 6);
            int finTag = Math.min(fichierEnBytes.length, 10 + tailleTag);

            String titre = null;
            String artistes = null;
            String album = null;
            String annee = null;
            int duree = 0;
            String imageMime = null;
            byte[] imageBytes = null;
            int decalage = 10; // pour passer l'entête des tags
            while (decalage + 10 <= finTag) {
                String frameId = new String(fichierEnBytes, decalage, 4, StandardCharsets.ISO_8859_1);
                if (frameId.trim().isEmpty() || frameId.charAt(0) == 0) {
                    break;
                }

                int frameSize = lireFrameSize(fichierEnBytes, decalage + 4, versionMajor);
                if (frameSize <= 0 || decalage + 10 + frameSize > finTag) {
                    break;
                }

                byte[] texte = new byte[frameSize];
                System.arraycopy(fichierEnBytes, decalage + 10, texte, 0, frameSize);

                switch (frameId) {
                    case "TIT2":
                    case "TT2":
                        titre = lireTexte(texte);
                        break;
                    case "TPE1":
                    case "TP1":
                        artistes = lireTexte(texte);
                        break;
                    case "TALB":
                    case "TAL":
                        album = lireTexte(texte);
                        break;
                    case "TYER":
                    case "TDRC":
                    case "TYE":
                        annee = lireTexte(texte);
                        break;
                    case "TLEN":
                        duree = parserDuree(lireTexte(texte));
                        break;
                    case "APIC":
                    case "PIC":
                        imageMime = lireMimeTypeApic(texte);
                        imageBytes = extraireImageApic(texte);
                        break;
                    default:
                        break;
                }

                decalage += 10 + frameSize;
            }

            //System.out.println("Titre : " + titre);
            //System.out.println("Artistes : " + artistes);
            //System.out.println("Album : " + album);
            //System.out.println("Année : " + annee);
            //System.out.println("Image MIME : " + imageMime);

            String imagePath = null;
            if (imageBytes != null && imageBytes.length > 0) {
                Files.createDirectories(Path.of("morceaux"));
                String baseNom = nettoyerNomFichier((titre == null ? "inconnu" : titre) + "_" + (artistes == null ? "artiste" : artistes));
                Path imageSortie = Path.of("morceaux", baseNom + extensionDepuisMime(imageMime));
                Files.write(imageSortie, imageBytes);
                imagePath = imageSortie.toString().replace('\\', '/');
            }

            if (titre == null || titre.isBlank() || artistes == null || artistes.isBlank()) {
                System.out.println("Tags insuffisants pour importer (titre/artiste manquants)");
                return;
            }

            String artistePrincipal = extraireArtistePrincipal(artistes);
            int anneeInt = parserAnnee(annee);

            Artiste artiste = getOrCreateArtiste(catalogue, artistePrincipal);
            Album albumObjet = getOrCreateAlbum(catalogue, artiste, album, anneeInt);
            Morceau morceau = getOrCreateMorceau(catalogue, titre, artiste, albumObjet, duree, path, imagePath);

            if (imagePath != null) {
                if (artiste.getImage() == null || artiste.getImage().isBlank()) {
                    artiste.setImage(imagePath);
                }
                if (albumObjet != null && (albumObjet.getImage() == null || albumObjet.getImage().isBlank())) {
                    albumObjet.setImage(imagePath);
                }
                if (morceau.getImage() == null || morceau.getImage().isBlank()) {
                    morceau.setImage(imagePath);
                }
            }

            Main.sauvegarder(catalogue.getMorceaux(), "morceaux.ser");
            Main.sauvegarder(catalogue.getPlaylists(), "playlists.ser");
            Main.sauvegarder(catalogue.getArtistes(), "artistes.ser");
            Main.sauvegarder(catalogue.getAlbums(), "albums.ser");

            System.out.println("Import termine: " + morceau.getNom() + " - " + artiste.getNom());
        } catch (IOException e) {
            System.out.println("Error reading file: " + path);
        }
    }

    private static int parserDuree(String texteDuree) {
        if (texteDuree == null || texteDuree.isBlank()) {
            return 0;
        }

        String valeur = texteDuree.trim();

        // TLEN en ID3 est normalement en millisecondes (ex: "203000").
        if (valeur.matches("\\d+")) {
            try {
                long brut = Long.parseLong(valeur);
                if (brut >= 1000) {
                    return (int) Math.max(1, brut / 1000);
                }
                return (int) brut;
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        String[] split = valeur.split(":");
        try {
            if (split.length == 2) { // mm:ss
                int minutes = Integer.parseInt(split[0].trim());
                int secondes = Integer.parseInt(split[1].trim());
                return Math.max(0, minutes * 60 + secondes);
            }
            if (split.length == 3) { // hh:mm:ss
                int heures = Integer.parseInt(split[0].trim());
                int minutes = Integer.parseInt(split[1].trim());
                int secondes = Integer.parseInt(split[2].trim());
                return Math.max(0, heures * 3600 + minutes * 60 + secondes);
            }
        } catch (NumberFormatException e) {
            return 0;
        }

        return 0;
    }

    private static String extraireArtistePrincipal(String artistesTag) {
        String valeur = artistesTag == null ? "" : artistesTag.trim();
        if (valeur.isBlank()) {
            return "Artiste inconnu";
        }
        String[] split = valeur.split(",|;|/|\\\\| feat\\.| ft\\.");
        if (split.length == 0 || split[0].isBlank()) {
            return valeur;
        }
        return split[0].trim();
    }

    private static int parserAnnee(String anneeTag) {
        if (anneeTag == null) {
            return 0;
        }
        String chiffres = anneeTag.replaceAll("[^0-9]", "");
        if (chiffres.length() >= 4) {
            chiffres = chiffres.substring(0, 4);
        }
        if (chiffres.length() != 4) {
            return 0;
        }
        try {
            return Integer.parseInt(chiffres);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String nettoyerNomFichier(String nom) {
        String propre = nom == null ? "fichier" : nom;
        propre = propre.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
        if (propre.isBlank()) {
            return "fichier";
        }
        return propre;
    }

    private static Artiste getOrCreateArtiste(Catalogue catalogue, String nomArtiste) {
        for (Artiste artiste : catalogue.getArtistes()) {
            if (artiste.getNom().equalsIgnoreCase(nomArtiste)) {
                return artiste;
            }
        }
        Artiste artiste = new Solo(nomArtiste);
        catalogue.ajouterArtiste(artiste);
        return artiste;
    }

    private static Album getOrCreateAlbum(Catalogue catalogue, Artiste artiste, String nomAlbum, int annee) {
        if (nomAlbum == null || nomAlbum.isBlank()) {
            return null;
        }

        for (Album album : catalogue.getAlbums()) {
            if (album.getNom().equalsIgnoreCase(nomAlbum)
                && album.getArtiste().getNom().equalsIgnoreCase(artiste.getNom())) {
                return album;
            }
        }

        Album album = new Album(nomAlbum, artiste, annee);
        artiste.ajouterAlbum(album);
        catalogue.ajouterAlbum(album);
        return album;
    }

    private static Morceau getOrCreateMorceau(Catalogue catalogue, String titre, Artiste artiste, Album album, int duree, String chemin, String image) {
        for (Morceau morceau : catalogue.getMorceaux()) {
            if (!morceau.getNom().equalsIgnoreCase(titre)) {
                continue;
            }
            for (Artiste artisteMorceau : morceau.getArtistes()) {
                if (artisteMorceau.getNom().equalsIgnoreCase(artiste.getNom())) {
                    if (album != null && !contientTitre(album.getMorceaux(), morceau.getNom())) {
                        album.ajouterMorceau(morceau);
                    }
                    return morceau;
                }
            }
        }

        Morceau morceau = new Morceau(titre, artiste, duree, chemin, image);
        if (album != null) {
            album.ajouterMorceau(morceau);
        }
        artiste.ajouterMorceau(morceau);
        catalogue.ajouterMorceau(morceau);
        return morceau;
    }

    private static boolean contientTitre(ArrayList<Morceau> morceaux, String titre) {
        for (Morceau morceau : morceaux) {
            if (morceau.getNom().equalsIgnoreCase(titre)) {
                return true;
            }
        }
        return false;
    }

    private static int lireFrameSize(byte[] data, int offset, int versionMajor) {
        if (versionMajor == 4) {
            return lireSynchsafeInt(data, offset);
        }

        return ((data[offset] & 0xFF) << 24)
            | ((data[offset + 1] & 0xFF) << 16)
            | ((data[offset + 2] & 0xFF) << 8)
            | (data[offset + 3] & 0xFF);
    }

    private static String lireTexte(byte[] texte) {
        if (texte.length == 0) {
            return "";
        }

        int encoding = texte[0] & 0xFF;
        byte[] textBytes = Arrays.copyOfRange(texte, 1, texte.length);

        Charset charset = charsetPourId3(encoding);
        return nettoyerTexte(decodeTexte(textBytes, charset, encoding));
    }

    private static String nettoyerTexte(String decodeTexte) {
        return decodeTexte.replace("\u0000", "").trim();
    }

    private static String decodeTexte(byte[] data, Charset charset, int encoding) {
        try {
            String texte = new String(data, charset);
            if (encoding == 1 || encoding == 2) {
                texte = retirerBOM(texte);
            }
            return texte;
        } catch (Exception e) {
            return new String(data, StandardCharsets.ISO_8859_1);
        }
    }

    private static Charset charsetPourId3(int encoding) {
        switch (encoding) {
            case 1:
                return StandardCharsets.UTF_16;
            case 2:
                return StandardCharsets.UTF_16BE;
            case 3:
                return StandardCharsets.UTF_8;
            case 0:
            default:
                return StandardCharsets.ISO_8859_1;
        }
    }

    private static String lireMimeTypeApic(byte[] frameData) {
        int index = 1;
        while (index < frameData.length && frameData[index] != 0) {
            index++;
        }
        if (index <= 1 || index >= frameData.length) {
            return null;
        }
        return new String(Arrays.copyOfRange(frameData, 1, index), StandardCharsets.ISO_8859_1).trim();
    }

    private static byte[] extraireImageApic(byte[] frameData) {
        if (frameData.length < 4) {
            return null;
        }

        int encoding = frameData[0] & 0xFF;
        int index = 1;

        while (index < frameData.length && frameData[index] != 0) {
            index++;
        }
        index++;

        if (index >= frameData.length) {
            return null;
        }

        index++; // picture type

        int descriptionEnd = indexOfTerminator(frameData, index, encoding);
        if (descriptionEnd < 0) {
            return null;
        }

        int dataStart = descriptionEnd + terminatorLength(encoding);
        if (dataStart >= frameData.length) {
            return null;
        }

        return Arrays.copyOfRange(frameData, dataStart, frameData.length);
    }

    private static int terminatorLength(int encoding) {
        if (encoding == 1 || encoding == 2) {
            return 2;
        }
        return 1;
    }

    private static int indexOfTerminator(byte[] frameData, int index, int encoding) {
        int terminatorLen = terminatorLength(encoding);
        for (int i = index; i <= frameData.length - terminatorLen; i++) {
            boolean isTerminator = true;
            for (int j = 0; j < terminatorLen; j++) {
                if (frameData[i + j] != 0) {
                    isTerminator = false;
                    break;
                }
            }
            if (isTerminator) {
                return i;
            }
        }
        return -1;
    }

    private static String extensionDepuisMime(String mimeType) {
        if (mimeType == null) {
            return ".bin";
        }

        String mime = mimeType.toLowerCase();
        if (mime.contains("jpeg") || mime.contains("jpg")) {
            return ".jpg";
        }
        if (mime.contains("png")) {
            return ".png";
        }
        if (mime.contains("gif")) {
            return ".gif";
        }
        if (mime.contains("bmp")) {
            return ".bmp";
        }
        return ".bin";
    }

    private static String retirerBOM(String texte) {
        if (texte.startsWith("\uFEFF")) {
            return texte.substring(1);
        }
        return texte;
    }

    private static int lireSynchsafeInt(byte[] data, int offset) {
        return ((data[offset] & 0x7F) << 21)
            | ((data[offset + 1] & 0x7F) << 14)
            | ((data[offset + 2] & 0x7F) << 7)
            | (data[offset + 3] & 0x7F);
    }

}
