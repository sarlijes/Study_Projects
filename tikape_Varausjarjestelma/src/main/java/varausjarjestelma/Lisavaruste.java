package varausjarjestelma;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Lisavaruste {

    int id;
    String varuste;
    Double lisahinta;

    public Lisavaruste(int id, String varuste) {
        this.id = id;
        this.varuste = varuste;

    }

    public int LisaaLisavarusteetTietokantaan(Lisavaruste lisavaruste) {
        // Tallentaa yhden lisävaruste-olion tietokantaan ja palauttaa sen id:n (joko juuri luotu uusi tai vanha)
        int palauta_id = -1;
        String varuste = lisavaruste.getVaruste();

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id FROM Lisavaruste WHERE varuste = ?;");
            stmt.setString(1, varuste);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                palauta_id = rs.getInt("ID");
            } else {
                PreparedStatement uusiAsiakasLisays = conn.prepareStatement(
                        "INSERT INTO Lisavaruste (varuste) VALUES (?);", PreparedStatement.RETURN_GENERATED_KEYS);
                uusiAsiakasLisays.setString(1, varuste);
                uusiAsiakasLisays.executeUpdate();

                ResultSet generatedKeys = uusiAsiakasLisays.getGeneratedKeys();
                if (generatedKeys.next()) {
                    palauta_id = generatedKeys.getInt(1);
                }
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
        return palauta_id;
    }

    static void LisaaVaraukselleLisavaruste(Integer varaus_id, Integer lisavaruste_id, String varuste) throws SQLException {
        // Tallentaa halutun lisävarusteen tietokantaan oikealle varaukselle

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO VaratunHuoneenLisavaruste (varaus_id, lisavaruste_id) VALUES (?,?);");
            stmt.setInt(1, varaus_id);
            stmt.setInt(2, lisavaruste_id);
            stmt.executeUpdate();
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void suosituimmatLisavarusteet() {
        System.out.println("Tulostetaan suosituimmat lisävarusteet");
        System.out.println("");

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            ResultSet rs = conn.prepareStatement("SELECT TOP 10 varuste, COUNT(varaus_id) FROM Lisavaruste "
                    + "LEFT JOIN VaratunhuoneenLisavaruste ON VaratunhuoneenLisavaruste.lisavaruste_id = Lisavaruste.id "
                    + "GROUP BY varuste "
                    + "ORDER BY COUNT(VARAUS_ID) DESC;").executeQuery();

            while (rs.next()) {
                String varuste = rs.getString("varuste");
                Integer maara = rs.getInt("COUNT(VARAUS_ID)");
                System.out.println(varuste + " " + maara + " varausta");
            }
            rs.close();
            conn.close();
         
        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getId() {
        return id;
    }

    public String getVaruste() {
        return varuste;
    }

    public Double getLisahinta() {
        return lisahinta;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVaruste(String varuste) {
        this.varuste = varuste;
    }

    public void setLisahinta(Double lisahinta) {
        this.lisahinta = lisahinta;
    }

}
