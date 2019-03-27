package varausjarjestelma;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Prosenttimetodit {

    static void varausprosenttiHuoneittain(Scanner s) {
        ;
        System.out.println("Tulostetaan varausprosentti huoneittain");
        System.out.println("");
        System.out.println("Mistä lähtien tarkastellaan?");
        String alku = s.nextLine() + "-01";
        System.out.println("Mihin asti tarkastellaan?");
        String loppu = s.nextLine() + "-28";


        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {

            PreparedStatement stmt = conn.prepareStatement("SELECT Huone.id, tyyppi, numero, hinta, alkupvm, loppupvm, (loppupvm - alkupvm) FROM Huone "
                    + "LEFT JOIN Varattuhuone ON Varattuhuone.huone_id = Huone.id "
                    + "LEFT JOIN Varaus ON Varaus.id = Varattuhuone.varaus_id "
                    + "WHERE huone_id IS NULL OR ((alkupvm <= ?) and (loppupvm >= ?));");
            stmt.setString(1, loppu);
            stmt.setString(2, alku);

            ResultSet rs = stmt.executeQuery();
            List<Huone> varattunaOlleetHuoneet = new ArrayList<>();
            while (rs.next()) {
                Huone h = new Huone(rs.getInt("id"), rs.getString("tyyppi"),
                        rs.getInt("numero"), rs.getDate("alkupvm"),
                        rs.getDate("loppupvm"), rs.getInt("DATEDIFF('DAY', ALKUPVM, LOPPUPVM)"), rs.getDouble("hinta"));
                varattunaOlleetHuoneet.add(h);
            }
            for (Huone h : varattunaOlleetHuoneet) {
                System.out.println(h.toStringProsentti());
            }
            stmt.close();
            rs.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void varausprosenttiHuonetyypeittain(Scanner s) {

        System.out.println("Tulostetaan varausprosentti huonetyypeittäin");
        System.out.println("");
        System.out.println("Mistä lähtien tarkastellaan?");
        String alku = s.nextLine() + "-01";
        System.out.println("Mihin asti tarkastellaan?");

        String loppu = s.nextLine() + "-28";

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {

            PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT tyyppi, alkupvm, loppupvm, (loppupvm - alkupvm) FROM Huone "
                    + "JOIN Varattuhuone ON Varattuhuone.huone_id = Huone.id "
                    + "JOIN Varaus ON Varaus.id = Varattuhuone.varaus_id "
                    + "WHERE huone_id IS NULL OR ((alkupvm <= ?) and (loppupvm >= ?));");
            stmt.setString(1, loppu);
            stmt.setString(2, alku);

            ResultSet rs = stmt.executeQuery();
            List<Huone> varattunaOlleetHuoneet = new ArrayList<>();
            while (rs.next()) {
                Huone h = new Huone(rs.getString("tyyppi"),
                        rs.getDate("alkupvm"),
                        rs.getDate("loppupvm"), rs.getInt("DATEDIFF('DAY', ALKUPVM, LOPPUPVM)"));
                varattunaOlleetHuoneet.add(h);
            }
            for (Huone h : varattunaOlleetHuoneet) {
                System.out.println(h.toStringTyyppiKohtainenProsentti());
            }
            stmt.close();
            rs.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
