package controleur;

import java.util.ArrayList;
import java.util.List;

import controleur.actions.ActionArguments;
import controleur.actions.Aimer;
import controleur.actions.AjouterPlaylist;
import controleur.actions.AjouterUtilisateur;
import controleur.exceptions.PlaylistDejaExistanteException;
import controleur.exceptions.UtilisateurDejaCreeException;
import controleur.formulaires.InscriptionForm;
import controleur.formulaires.PlaylistForm;
import metier.Abonne;
import metier.Admin;
import metier.Album;
import metier.Artiste;
import metier.Avis;
import metier.Catalogue;
import metier.Groupe;
import metier.Morceau;
import metier.Playlist;
import metier.Solo;

public class ScriptPeuplement {
    public static void main(String[] args) {
        ArrayList<Abonne> abonnes = new ArrayList<>();
        ArrayList<Admin> admins = new ArrayList<>();
        ArrayList<Morceau> morceaux = new ArrayList<>();
        ArrayList<Playlist> playlists = new ArrayList<>();
        ArrayList<Artiste> artistes = new ArrayList<>();
        ArrayList<Album> albums = new ArrayList<>();
        ArrayList<Avis> avis = new ArrayList<>();

        Main.charger(abonnes, "abonnes.ser");
        Main.charger(admins, "admins.ser");
        Main.charger(morceaux, "morceaux.ser");
        Main.charger(playlists, "playlists.ser");
        Main.charger(artistes, "artistes.ser");
        Main.charger(albums, "albums.ser");
        Main.charger(avis, "avis.ser");

        Catalogue catalogue = new Catalogue(morceaux, playlists, artistes, albums);

        Solo daftPunk = getOrCreateSolo(catalogue, "Daft Punk");
        Solo stromae = getOrCreateSolo(catalogue, "Stromae");
        Solo adele = getOrCreateSolo(catalogue, "Adele");
        Solo weeknd = getOrCreateSolo(catalogue, "The Weeknd");
        Solo billie = getOrCreateSolo(catalogue, "Billie Eilish");
        Solo orelsan = getOrCreateSolo(catalogue, "Orelsan");
        Solo sza = getOrCreateSolo(catalogue, "SZA");
        Solo drake = getOrCreateSolo(catalogue, "Drake");
        Solo coldplaySolo = getOrCreateSolo(catalogue, "Chris Martin");
        Groupe coldplay = getOrCreateGroupe(catalogue, "Coldplay");

        Album randomAccess = getOrCreateAlbum(catalogue, daftPunk, "Random Access Memories", 2013);
        Album discovery = getOrCreateAlbum(catalogue, daftPunk, "Discovery", 2001);
        Album racineCarree = getOrCreateAlbum(catalogue, stromae, "Racine Carree", 2013);
        Album multitude = getOrCreateAlbum(catalogue, stromae, "Multitude", 2022);
        Album twentyOne = getOrCreateAlbum(catalogue, adele, "21", 2011);
        Album thirty = getOrCreateAlbum(catalogue, adele, "30", 2021);
        Album afterHours = getOrCreateAlbum(catalogue, weeknd, "After Hours", 2020);
        Album dawnFm = getOrCreateAlbum(catalogue, weeknd, "Dawn FM", 2022);
        Album happierThanEver = getOrCreateAlbum(catalogue, billie, "Happier Than Ever", 2021);
        Album dontSmileAtMe = getOrCreateAlbum(catalogue, billie, "Dont Smile At Me", 2017);
        Album civilisation = getOrCreateAlbum(catalogue, orelsan, "Civilisation", 2021);
        Album leChantDesSirenes = getOrCreateAlbum(catalogue, orelsan, "Le Chant des Sirenes", 2011);
        Album sos = getOrCreateAlbum(catalogue, sza, "SOS", 2022);
        Album ctrl = getOrCreateAlbum(catalogue, sza, "Ctrl", 2017);
        Album scorpion = getOrCreateAlbum(catalogue, drake, "Scorpion", 2018);
        Album views = getOrCreateAlbum(catalogue, drake, "Views", 2016);
        Album parachutes = getOrCreateAlbum(catalogue, coldplay, "Parachutes", 2000);
        Album aRushOfBlood = getOrCreateAlbum(catalogue, coldplay, "A Rush of Blood to the Head", 2002);
        Album xAndY = getOrCreateAlbum(catalogue, coldplay, "X&Y", 2005);

        Morceau getLucky = getOrCreateMorceau(catalogue, daftPunk, randomAccess, "Get Lucky", 248);
        Morceau instantCrush = getOrCreateMorceau(catalogue, daftPunk, randomAccess, "Instant Crush", 337);
        Morceau loseYourselfToDance = getOrCreateMorceau(catalogue, daftPunk, randomAccess, "Lose Yourself to Dance", 353);
        Morceau oneMoreTime = getOrCreateMorceau(catalogue, daftPunk, discovery, "One More Time", 320);
        Morceau hardBetter = getOrCreateMorceau(catalogue, daftPunk, discovery, "Harder Better Faster Stronger", 224);

        Morceau formi = getOrCreateMorceau(catalogue, stromae, racineCarree, "Formidable", 214);
        Morceau papaoutai = getOrCreateMorceau(catalogue, stromae, racineCarree, "Papaoutai", 232);
        Morceau taFete = getOrCreateMorceau(catalogue, stromae, racineCarree, "Ta fete", 173);
        Morceau sante = getOrCreateMorceau(catalogue, stromae, multitude, "Sante", 191);
        Morceau lenfer = getOrCreateMorceau(catalogue, stromae, multitude, "Lenfer", 189);

        Morceau rolling = getOrCreateMorceau(catalogue, adele, twentyOne, "Rolling in the Deep", 228);
        Morceau someoneLikeYou = getOrCreateMorceau(catalogue, adele, twentyOne, "Someone Like You", 285);
        Morceau easyOnMe = getOrCreateMorceau(catalogue, adele, thirty, "Easy On Me", 224);
        Morceau ohMyGod = getOrCreateMorceau(catalogue, adele, thirty, "Oh My God", 225);

        Morceau blindingLights = getOrCreateMorceau(catalogue, weeknd, afterHours, "Blinding Lights", 200);
        Morceau saveYourTears = getOrCreateMorceau(catalogue, weeknd, afterHours, "Save Your Tears", 215);
        Morceau takeMyBreath = getOrCreateMorceau(catalogue, weeknd, dawnFm, "Take My Breath", 220);
        Morceau lessThanZero = getOrCreateMorceau(catalogue, weeknd, dawnFm, "Less Than Zero", 211);

        Morceau badGuy = getOrCreateMorceau(catalogue, billie, dontSmileAtMe, "Bad Guy", 194);
        Morceau oceanEyes = getOrCreateMorceau(catalogue, billie, dontSmileAtMe, "Ocean Eyes", 200);
        Morceau happier = getOrCreateMorceau(catalogue, billie, happierThanEver, "Happier Than Ever", 298);
        Morceau yourPower = getOrCreateMorceau(catalogue, billie, happierThanEver, "Your Power", 245);

        Morceau jourMeilleur = getOrCreateMorceau(catalogue, orelsan, civilisation, "Jour meilleur", 242);
        Morceau basique = getOrCreateMorceau(catalogue, orelsan, civilisation, "Basique", 228);
        Morceau raelsan = getOrCreateMorceau(catalogue, orelsan, leChantDesSirenes, "Raelsan", 210);
        Morceau suicideSocial = getOrCreateMorceau(catalogue, orelsan, leChantDesSirenes, "Suicide social", 477);

        Morceau killBill = getOrCreateMorceau(catalogue, sza, sos, "Kill Bill", 153);
        Morceau snooze = getOrCreateMorceau(catalogue, sza, sos, "Snooze", 201);
        Morceau loveGalore = getOrCreateMorceau(catalogue, sza, ctrl, "Love Galore", 275);
        Morceau theWeekend = getOrCreateMorceau(catalogue, sza, ctrl, "The Weekend", 273);

        Morceau godsPlan = getOrCreateMorceau(catalogue, drake, scorpion, "Gods Plan", 198);
        Morceau inMyFeelings = getOrCreateMorceau(catalogue, drake, scorpion, "In My Feelings", 217);
        Morceau hotlineBling = getOrCreateMorceau(catalogue, drake, views, "Hotline Bling", 267);
        Morceau oneDance = getOrCreateMorceau(catalogue, drake, views, "One Dance", 173);

        Morceau yellow = getOrCreateMorceau(catalogue, coldplay, parachutes, "Yellow", 268);
        Morceau trouble = getOrCreateMorceau(catalogue, coldplay, parachutes, "Trouble", 270);
        Morceau clocks = getOrCreateMorceau(catalogue, coldplay, aRushOfBlood, "Clocks", 307);
        Morceau theScientist = getOrCreateMorceau(catalogue, coldplay, aRushOfBlood, "The Scientist", 309);
        Morceau fixYou = getOrCreateMorceau(catalogue, coldplay, xAndY, "Fix You", 294);

        getOrCreatePlaylist(catalogue, "Top Electro", 1, List.of(getLucky, instantCrush, loseYourselfToDance, oneMoreTime, hardBetter));
        getOrCreatePlaylist(catalogue, "Top Francophone", 1, List.of(formi, papaoutai, taFete, sante, lenfer, jourMeilleur, basique));
        getOrCreatePlaylist(catalogue, "Top Mix", 1, List.of(getLucky, papaoutai, rolling, blindingLights, killBill, yellow));
        getOrCreatePlaylist(catalogue, "Mood Chill", 1, List.of(someoneLikeYou, easyOnMe, oceanEyes, theScientist, snooze));
        getOrCreatePlaylist(catalogue, "Cardio Boost", 1, List.of(blindingLights, oneDance, godsPlan, hardBetter, clocks));
        getOrCreatePlaylist(catalogue, "Nocturne", 1, List.of(instantCrush, takeMyBreath, lessThanZero, hotlineBling, trouble));
        getOrCreatePlaylist(catalogue, "Classiques 2000", 1, List.of(yellow, clocks, oneMoreTime, hotlineBling, loveGalore));
        getOrCreatePlaylist(catalogue, "Hits 2020", 1, List.of(easyOnMe, takeMyBreath, killBill, jourMeilleur, yourPower));
        getOrCreatePlaylist(catalogue, "Rap & RnB", 1, List.of(godsPlan, inMyFeelings, killBill, snooze, raelsan));
        getOrCreatePlaylist(catalogue, "Weekend Playlist", 1, List.of(saveYourTears, takeMyBreath, oneDance, badGuy, fixYou));

        peuplerAbonnesEtUsages(
            catalogue,
            abonnes,
            admins,
            avis,
            getLucky,
            oneMoreTime,
            blindingLights,
            killBill,
            yellow,
            sante,
            easyOnMe,
            fixYou
        );

        ajouterImagesParDefaut(catalogue);

        Main.sauvegarder(abonnes, "abonnes.ser");
        Main.sauvegarder(admins, "admins.ser");
        Main.sauvegarder(avis, "avis.ser");
        Main.sauvegarder(catalogue.getMorceaux(), "morceaux.ser");
        Main.sauvegarder(catalogue.getPlaylists(), "playlists.ser");
        Main.sauvegarder(catalogue.getArtistes(), "artistes.ser");
        Main.sauvegarder(catalogue.getAlbums(), "albums.ser");

        System.out.println("ajout automatique de");
        System.out.println(abonnes.size() + " abonnes");
        System.out.println(admins.size() + " admins");
        System.out.println(avis.size() + " avis");
        System.out.println(catalogue.getArtistes().size() + " artistes");
        System.out.println(catalogue.getAlbums().size() + " albums");
        System.out.println(catalogue.getMorceaux().size() + " morceaux");
        System.out.println(catalogue.getPlaylists().size() + " playlists");
    }

    private static void peuplerAbonnesEtUsages(
        Catalogue catalogue,
        ArrayList<Abonne> abonnes,
        ArrayList<Admin> admins,
        ArrayList<Avis> avis,
        Morceau getLucky,
        Morceau oneMoreTime,
        Morceau blindingLights,
        Morceau killBill,
        Morceau yellow,
        Morceau sante,
        Morceau easyOnMe,
        Morceau fixYou
    ) {
        Abonne lea = getOrCreateAbonne(catalogue, abonnes, admins, "Lea", "lea@javazik.fr", "lea123");
        Abonne max = getOrCreateAbonne(catalogue, abonnes, admins, "Max", "max@javazik.fr", "max123");
        Abonne nina = getOrCreateAbonne(catalogue, abonnes, admins, "Nina", "nina@javazik.fr", "nina123");

        if (lea != null) {
            simulerEcoute(lea, getLucky, oneMoreTime, blindingLights, yellow);
            aimerSiNecessaire(lea, catalogue, getLucky);
            aimerSiNecessaire(lea, catalogue, blindingLights);
            getOrCreatePlaylistAbonne(catalogue, lea, "Lea - Running", List.of(blindingLights, oneMoreTime, sante));
            mettreAvisSiAbsent(avis, lea, getLucky, 5, "Toujours un classique qui met de bonne humeur.");
            mettreAvisSiAbsent(avis, lea, blindingLights, 4, "Hyper efficace pour courir.");
        }

        if (max != null) {
            simulerEcoute(max, killBill, yellow, fixYou, blindingLights);
            aimerSiNecessaire(max, catalogue, killBill);
            aimerSiNecessaire(max, catalogue, fixYou);
            getOrCreatePlaylistAbonne(catalogue, max, "Max - Chill Soir", List.of(killBill, yellow, fixYou));
            mettreAvisSiAbsent(avis, max, killBill, 5, "Le refrain reste en tete toute la journee.");
            mettreAvisSiAbsent(avis, max, fixYou, 4, "Parfait pour se poser le soir.");
        }

        if (nina != null) {
            simulerEcoute(nina, sante, getLucky, easyOnMe, oneMoreTime);
            aimerSiNecessaire(nina, catalogue, sante);
            aimerSiNecessaire(nina, catalogue, easyOnMe);
            getOrCreatePlaylistAbonne(catalogue, nina, "Nina - Mix Daily", List.of(sante, easyOnMe, getLucky));
            mettreAvisSiAbsent(avis, nina, sante, 4, "Un son super original et entetant.");
            mettreAvisSiAbsent(avis, nina, easyOnMe, 5, "Voix incroyable, emotion garantie.");
        }
    }

    private static void mettreAvisSiAbsent(
        ArrayList<Avis> avis,
        Abonne abonne,
        Morceau morceau,
        int note,
        String commentaire
    ) {
        if (abonne == null || morceau == null || commentaire == null) {
            return;
        }

        for (Avis existant : avis) {
            if (existant.getAbonne() != null
                && existant.getMorceau() != null
                && existant.getAbonne().getMail().equalsIgnoreCase(abonne.getMail())
                && existant.getMorceau().getNom().equalsIgnoreCase(morceau.getNom())
                && commentaire.equals(existant.getCommentaire())) {
                return;
            }
        }

        Avis nouvelAvis = new Avis(morceau, abonne, note, commentaire);
        avis.add(nouvelAvis);
        morceau.ajouterAvis(nouvelAvis);
        abonne.ajouterAvis(nouvelAvis);
    }

    private static Abonne getOrCreateAbonne(
        Catalogue catalogue,
        ArrayList<Abonne> abonnes,
        ArrayList<Admin> admins,
        String nom,
        String mail,
        String mdp
    ) {
        for (Abonne abonne : abonnes) {
            if (abonne.getMail().equalsIgnoreCase(mail)) {
                return abonne;
            }
        }

        ActionArguments arguments = new ActionArguments(
            new InscriptionForm("abonne", nom, mail, mdp),
            catalogue,
            abonnes,
            admins,
            null
        );

        try {
            new AjouterUtilisateur().executer(arguments);
        } catch (UtilisateurDejaCreeException ignored) {
            // idempotent: l'abonne existe deja.
        }

        if (arguments.utilisateur instanceof Abonne) {
            return (Abonne) arguments.utilisateur;
        }

        for (Abonne abonne : abonnes) {
            if (abonne.getMail().equalsIgnoreCase(mail)) {
                return abonne;
            }
        }
        return null;
    }

    private static Playlist getOrCreatePlaylistAbonne(
        Catalogue catalogue,
        Abonne abonne,
        String nom,
        List<Morceau> morceaux
    ) {
        for (Playlist playlist : catalogue.getPlaylists()) {
            if (playlist.getNom().equalsIgnoreCase(nom)
                && playlist.getNumUtilisateur() == abonne.getNum()) {
                if (!abonne.playlistDejaSauvegardee(playlist.getNum())) {
                    abonne.ajouterPlaylist(playlist.getNum());
                }
                return playlist;
            }
        }

        try {
            new AjouterPlaylist().executer(
                new ActionArguments(
                    abonne,
                    catalogue,
                    new PlaylistForm(nom, new ArrayList<>(morceaux), abonne.getNum())
                )
            );
        } catch (PlaylistDejaExistanteException ignored) {
            // idempotent: on cherche ensuite la playlist deja existante.
        }

        for (Playlist playlist : catalogue.getPlaylists()) {
            if (playlist.getNom().equalsIgnoreCase(nom)
                && playlist.getNumUtilisateur() == abonne.getNum()) {
                return playlist;
            }
        }
        return null;
    }

    private static void aimerSiNecessaire(Abonne abonne, Catalogue catalogue, Morceau morceau) {
        if (morceau == null || abonne.morceauDejaAime(morceau, catalogue)) {
            return;
        }
        new Aimer().executer(new ActionArguments(null, abonne, catalogue, morceau));
    }

    private static void simulerEcoute(Abonne abonne, Morceau... morceaux) {
        for (Morceau morceau : morceaux) {
            abonne.ajouterHistorique(morceau);
        }
    }

    private static boolean imageVide(String image) {
        return image == null || image.isBlank();
    }

    private static void ajouterImagesParDefaut(Catalogue catalogue) {
        for (Artiste artiste : catalogue.getArtistes()) {
            if (imageVide(artiste.getImage())) {
                artiste.setImage("assets/profil.png");
            }
        }

        for (Album album : catalogue.getAlbums()) {
            if (imageVide(album.getImage())) {
                album.setImage("assets/librairie.png");
            }
        }

        for (Morceau morceau : catalogue.getMorceaux()) {
            if (imageVide(morceau.getImage())) {
                morceau.setImage("assets/logo.png");
            }
        }

        for (Playlist playlist : catalogue.getPlaylists()) {
            if (imageVide(playlist.getImage())) {
                playlist.setImage("assets/librairie.png");
            }
        }
    }

    private static Solo getOrCreateSolo(Catalogue catalogue, String nom) {
        for (Artiste artiste : catalogue.getArtistes()) {
            if (artiste.getNom().equalsIgnoreCase(nom) && artiste instanceof Solo) {
                return (Solo) artiste;
            }
        }

        Solo solo = new Solo(nom);
        catalogue.ajouterArtiste(solo);
        return solo;
    }

    private static Groupe getOrCreateGroupe(Catalogue catalogue, String nom) {
        for (Artiste artiste : catalogue.getArtistes()) {
            if (artiste.getNom().equalsIgnoreCase(nom) && artiste instanceof Groupe) {
                return (Groupe) artiste;
            }
        }

        Groupe groupe = new Groupe(nom);
        catalogue.ajouterArtiste(groupe);
        return groupe;
    }

    private static Album getOrCreateAlbum(Catalogue catalogue, Artiste artiste, String titre, int annee) {
        for (Album album : catalogue.getAlbums()) {
            if (album.getNom().equalsIgnoreCase(titre)
                && album.getArtiste().getNom().equalsIgnoreCase(artiste.getNom())) {
                return album;
            }
        }

        Album album = new Album(titre, artiste, annee);
        artiste.ajouterAlbum(album);
        catalogue.ajouterAlbum(album);
        return album;
    }

    private static Morceau getOrCreateMorceau(
        Catalogue catalogue,
        Artiste artiste,
        Album album,
        String titre,
        int duree
    ) {
        for (Morceau morceau : catalogue.getMorceaux()) {
            if (morceau.getNom().equalsIgnoreCase(titre)) {
                return morceau;
            }
        }

        Morceau morceau = new Morceau(titre, artiste, duree);
        album.ajouterMorceau(morceau);
        artiste.ajouterMorceau(morceau);
        catalogue.ajouterMorceau(morceau);
        return morceau;
    }

    private static Playlist getOrCreatePlaylist(
        Catalogue catalogue,
        String nom,
        int numUtilisateur,
        List<Morceau> morceaux
    ) {
        for (Playlist playlist : catalogue.getPlaylists()) {
            if (playlist.getNom().equalsIgnoreCase(nom)) {
                return playlist;
            }
        }

        Playlist playlist = new Playlist(nom, numUtilisateur, catalogue);
        for (Morceau morceau : morceaux) {
            playlist.ajouterMorceau(morceau);
        }

        try {
            catalogue.ajouterPlaylist(playlist);
        } catch (Exception ignored) {
            // Si deja existante, on renvoie la playlist existante plus haut.
        }

        return playlist;
    }
}
