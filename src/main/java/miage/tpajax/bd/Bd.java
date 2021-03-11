package miage.tpajax.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe en charge de la base de données.
 */
public class Bd {

    /*---------*/
    /* Données */
    /*---------*/

    /*----- Connexion -----*/
    private static Connection cx = null;

    /*----- Données de connexion -----*/
    private static final String URL = "jdbc:mysql://localhost:3307/ensg_berro";
    private static final String LOGIN = "berro";
    private static final String PASSWORD = "berro";


    /*----------*/
    /* Méthodes */
    /*----------*/

    /**
     * Crée la connexion avec la base de données.
     */
    private static void connexion() throws ClassNotFoundException, SQLException {
        /*----- Chargement du pilote pour la BD -----*/
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("Exception connexion() - Pilote MySql introuvable - " + ex.getMessage());
        }

        /*----- Ouverture de la connexion -----*/
        try {
            Bd.cx = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException ex) {
            throw new SQLException("Exception connexion() - Problème de connexion à la base de données - " + ex.getMessage());
        }
    }

    /**
     * Retourne la liste de citations de 'nom_auteur'.
     */
    public static ArrayList<String> lireCitations(String nom_auteur) throws ClassNotFoundException, SQLException {
        /*----- Création de la connexion à la base de données -----*/
        if (Bd.cx == null) {
            Bd.connexion();
        }

        /*----- Interrogation de la base -----*/
        ArrayList<String> liste = new ArrayList<>();

        /*----- Requête SQL -----*/
        String sql = "SELECT LibCitation FROM Auteur, Citation WHERE Auteur.IdAuteur=Citation.AutCitation AND Auteur.NomAuteur=?";

        /*----- Ouverture de l'espace de requête -----*/
        try (PreparedStatement st = Bd.cx.prepareStatement(sql)) {
            /*----- Exécution de la requête -----*/
            st.setString(1, nom_auteur);
            try (ResultSet rs = st.executeQuery()) {
                /*----- Lecture du contenu du ResultSet -----*/
                while (rs.next()) {
                    liste.add(rs.getString(1));
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Exception lireCitations() : Problème SQL - " + ex.getMessage());
        }

        return liste;
    }

    public static ArrayList<String> LireMots(String mot_begin) throws ClassNotFoundException, SQLException {
        /*----- Création de la connexion à la base de données -----*/
        if (Bd.cx == null) {
            Bd.connexion();
        }

        /*----- Interrogation de la base -----*/
        ArrayList<String> liste = new ArrayList<>();

        /*----- Requête SQL -----*/
        String sql = "SELECT * FROM Mot WHERE Texte LIKE ?";

        if (mot_begin != null) {
            /*----- Ouverture de l'espace de requête -----*/
            try (PreparedStatement st = Bd.cx.prepareStatement(sql)) {
                /*----- Exécution de la requête -----*/
                st.setString(1, mot_begin + "%");
                try (ResultSet rs = st.executeQuery()) {
                    /*----- Lecture du contenu du ResultSet -----*/
                    while (rs.next()) {
                        liste.add(rs.getString(1));
                    }
                }
            } catch (SQLException ex) {
                throw new SQLException("Exception lireMots() : Problème SQL - " + ex.getMessage());
            }

            return liste;

        } else {
            return null;
        }
    }

    public static boolean VerifierMot(String mot) throws ClassNotFoundException, SQLException {
        if (Bd.cx == null) {
            Bd.connexion();
        }

        boolean res = false;

        String sql = "SELECT count(*) FROM Mot WHERE Texte = ?";

        /*----- Ouverture de l'espace de requête -----*/
        try (PreparedStatement st = Bd.cx.prepareStatement(sql)) {
            /*----- Exécution de la requête -----*/
            st.setString(1, mot);
            try (ResultSet rs = st.executeQuery()) {
                /*----- Lecture du contenu du ResultSet -----*/
                if (rs.next()) {
                    if (Integer.parseInt(rs.getString(1)) != 0) {
                        res = true;
                    } else {
                        res = false;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Exception lireMots() : Problème SQL - " + ex.getMessage());
        }

        return res;
    }


    public static int ajouterMot(String mot) throws ClassNotFoundException, SQLException {
        if (Bd.cx == null) {
            Bd.connexion();
        }

        String sql = "INSERT INTO `Mot`(`Texte`) VALUES (?)";

        /*----- Ouverture de l'espace de requête -----*/
        try (PreparedStatement st = Bd.cx.prepareStatement(sql)) {
            /*----- Exécution de la requête -----*/
            st.setString(1, mot);
            return st.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Exception ajouterMots() : Problème SQL - " + ex.getMessage());
        }

    }

    /*----------------------------*/
    /* Programme principal (test) */
    /*----------------------------*/
    public static void main(String[] s) {
        try {
            System.out.println(Bd.VerifierMot("a"));
            System.out.println(ajouterMot("a"));
            System.out.println(Bd.VerifierMot("a"));
//            ArrayList<String> l = Bd.LireMots("c");
//            for (String msg : l) {
//                System.out.println(msg);
            //   }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
/*----- Fin de la classe Bd -----*/
