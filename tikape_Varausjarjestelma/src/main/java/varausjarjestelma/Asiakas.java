package varausjarjestelma;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Asiakas {

    List<Varaus> varaukset;

    int id;
    String kokonimi;
    String puhelinnumero;
    String email;
    Double total;

    public Asiakas(int id, String kokonimi, String puhelinnumero, String email) {
        this.id = id;
        this.kokonimi = kokonimi;
        this.puhelinnumero = puhelinnumero;
        this.email = email;
    }

    public Asiakas(int id, String kokonimi, String puhelinnumero, String email, Integer paivienMaara) {
        this.id = id;
        this.kokonimi = kokonimi;
        this.puhelinnumero = puhelinnumero;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getKokonimi() {
        return kokonimi;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public String getEmail() {
        return email;
    }

    public Double getTotal() {
        return total;
    }

    public List<Varaus> getVaraukset() {
        return varaukset;
    }

    public void setKokonimi(String kokonimi) {
        this.kokonimi = kokonimi;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    static int tarkistaLoytyykoAsiakas(String nimi, String puhelinnumero, String sahkoposti) {
        int id = -1;

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id FROM Asiakas WHERE kokonimi = ? AND puhelinnumero = ? AND email = ?;");

            stmt.setString(1, nimi);
            stmt.setString(2, puhelinnumero);
            stmt.setString(3, sahkoposti);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("ID");
            }
            conn.close();
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }

    static int tallennaAsiakasTietokantaan(String nimi, String puhelinnumero, String sahkoposti) {
        //, palauta uusi asiakasnumero     
        int id = -1;
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {

            PreparedStatement uusiAsiakasLisays = conn.prepareStatement(
                    "INSERT INTO Asiakas (kokonimi, puhelinnumero, email) VALUES (?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);

            uusiAsiakasLisays.setString(1, nimi);
            uusiAsiakasLisays.setString(2, puhelinnumero);
            uusiAsiakasLisays.setString(3, sahkoposti);
            uusiAsiakasLisays.executeUpdate();
            
            ResultSet generatedKeys = uusiAsiakasLisays.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    static void parhaatAsiakkaat() throws SQLException {
        System.out.println("Tulostetaan parhaat asiakkaat");
        System.out.println("");
        HashMap<Integer, Double> asiakkaidenTotalit = new HashMap<>();
        HashMap<Integer, String> asiakkaidenNimet = new HashMap<>();
        HashMap<Integer, String> asiakkaidenEmailit = new HashMap<>();
        HashMap<Integer, String> asiakkaidenPuhelinnumerot = new HashMap<>();
        
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            ResultSet rs = conn.prepareStatement("SELECT TOP 10 Asiakas.id, kokonimi, email, puhelinnumero, alkupvm, loppupvm, (loppupvm-alkupvm), "
                    + "hinta, (hinta * (loppupvm-alkupvm)), "
                    + "COUNT (DISTINCT Varattuhuone.Huone_id) FROM Varaus "
                    + "JOIN Asiakas ON Asiakas.id = Varaus.asiakas_id "
                    + "JOIN Varattuhuone ON Varattuhuone.varaus_id = Varaus.id "
                    + "JOIN Huone ON Huone.id = Varattuhuone.huone_id "
                    + "GROUP BY Asiakas.id, Varaus.alkupvm, hinta "
                    + "ORDER BY HINTA * DATEDIFF('DAY', ALKUPVM, LOPPUPVM) DESC"
                    + ";").executeQuery();

            if (rs.isLast()) {
                System.out.println("Ei asiakkaita");
            } else {
                while (rs.next()) {

                    Integer asiakas_id = Integer.valueOf(rs.getString("id"));
                    String kokonimi = rs.getString("kokonimi");
                    String email = rs.getString("email");
                    String puhelinnumero = rs.getString("puhelinnumero");

                    Integer paivienMaara = rs.getInt("DATEDIFF('DAY', ALKUPVM, LOPPUPVM)");

                    PreparedStatement stmt = conn.prepareStatement("SELECT tyyppi, numero, hinta FROM Huone "
                            + "JOIN Varattuhuone ON Varattuhuone.huone_id = Huone.id "
                            + "JOIN Varaus ON Varaus.id = Varattuhuone.varaus_id "
                            + "WHERE asiakas_id = ?"
                            + "GROUP BY numero;");
                    stmt.setInt(1, asiakas_id);
                    ResultSet rs_huoneet = stmt.executeQuery();
                    Double summa = 0.00;
                    while (rs_huoneet.next()) {
                        Double hinta = rs_huoneet.getDouble("hinta");
                        summa = summa + paivienMaara * hinta;
                    }
                    if (asiakkaidenTotalit.containsKey(asiakas_id)) {
                        asiakkaidenTotalit.put(asiakas_id, (asiakkaidenTotalit.get(asiakas_id) + summa));
                    } else {
                        asiakkaidenTotalit.put(asiakas_id, summa);
                        asiakkaidenNimet.put(asiakas_id, kokonimi);
                        asiakkaidenEmailit.put(asiakas_id, email);
                        asiakkaidenPuhelinnumerot.put(asiakas_id, puhelinnumero);
                    }
                    stmt.close();
                    rs_huoneet.close();
                }
            }
            rs.close();
            conn.close();
            for (Integer id : asiakkaidenTotalit.keySet()) {
                        System.out.println(asiakkaidenNimet.get(id) +  ", " + asiakkaidenEmailit.get(id) + ", " 
                                + asiakkaidenPuhelinnumerot.get(id) + ", " + asiakkaidenTotalit.get(id) + 
                                " euroa");
                        
                    }
            
        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
