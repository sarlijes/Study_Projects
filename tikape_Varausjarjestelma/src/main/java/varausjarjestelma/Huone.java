package varausjarjestelma;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Huone {

    VarattuHuone varattuhuone;

    int id;
    String tyyppi;
    int numero;
    Double hinta;
    int paivienMaara;
    Date alkupvm;
    Date loppupvm;
    Double varausprosentti;

    public Huone(int id, String tyyppi, int numero, Double hinta) {

        this.id = id;
        this.tyyppi = tyyppi;
        this.numero = numero;
        this.hinta = hinta;
    }

    public Huone(int id, String tyyppi, int numero, Date alkupvm, Date loppupvm, int paivienMaara, Double hinta) {

        this.id = id;
        this.tyyppi = tyyppi;
        this.numero = numero;
        this.alkupvm = alkupvm;
        this.loppupvm = loppupvm;
        this.paivienMaara = paivienMaara;
        this.hinta = hinta;
    }

    public Huone(String tyyppi, Date alkupvm, Date loppupvm, int paivienMaara) {
        this.tyyppi = tyyppi;
        this.alkupvm = alkupvm;
        this.loppupvm = loppupvm;
        this.paivienMaara = paivienMaara;

    }

    public int getId() {
        return id;
    }

    public String getTyyppi() {
        return tyyppi;
    }

    public int getNumero() {
        return numero;
    }

    public int getPaivienMaara() {
        return paivienMaara;
    }

    public String toStringProsentti() {
        if (paivienMaara >= 31) {
            paivienMaara = 31;
        }
        varausprosentti = Double.valueOf(100 * paivienMaara / 31);
        return tyyppi + ", " + numero + ", " + hinta + " euroa, " + varausprosentti + " %";

    }

    public String toStringTyyppiKohtainenProsentti() {
        if (paivienMaara >= 31) {
            paivienMaara = 31;
        }
        varausprosentti = Double.valueOf(100 * paivienMaara / 31);
        return tyyppi + ", " + varausprosentti + " %";
    }

    public Double getHinta() {
        return hinta;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTyyppi(String tyyppi) {
        this.tyyppi = tyyppi;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setHinta(Double hinta) {
        this.hinta = hinta;
    }

    @Override
    public String toString() {
        return this.tyyppi + ", " + this.numero + ", " + this.hinta + " euroa";

    }

    static void lisaaHuone(Scanner s) {
        System.out.println("Lisätään huone");
        System.out.println("");
        String syote = "";
        String tyyppi = "";
        Boolean ok = false;
        while (ok == false) {
            
        System.out.println("Minkä tyyppinen huone on?");
            syote = s.nextLine();
            if (!syote.isEmpty()) {
                ok = true;
                tyyppi = syote;
            }
        }        
        
        
        int numero = 0;
        boolean loytyi = true;
        while (loytyi = true) {
            System.out.println("Mikä huoneen numeroksi asetetaan?");
            numero = Integer.valueOf(s.nextLine());

            try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT (*) FROM Huone WHERE numero = ?;");
                stmt.setInt(1, numero);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int tulos = rs.getInt("COUNT(*)");
                    if (tulos == 0) {
                        loytyi = false;
                        break;

                    } else {
                        loytyi = true;
                        System.out.println("Huoneen numero on jo käytössä. Aseta toinen numero.");
                        numero = Integer.valueOf(s.nextLine());
                        break;
                    }
                }
                stmt.close();
                rs.close();
                conn.close();

                break;
            } catch (SQLException ex) {
                Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Kuinka monta euroa huone maksaa yöltä?");
        double hinta;
        while (true) {
            if (s.hasNextDouble()) {
                hinta = s.nextDouble();
                break;
            } else if (s.hasNextInt()) {
                hinta = s.nextInt();
                break;
            } else {
                s.next();
            }
            System.out.println("Syötä summa.");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Huone (tyyppi, numero, hinta) VALUES (?, ?, ?)");
            stmt.setString(1, tyyppi);
            stmt.setInt(2, numero);
            stmt.setDouble(3, hinta);
            stmt.executeUpdate();

            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Huone lisätty tietokantaan.");
    }

    static void listaaHuoneet() {
        System.out.println("Listataan huoneet");
        System.out.println("");
        List<Huone> huoneetListalla = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            ResultSet rs = conn.prepareStatement("SELECT * FROM Huone;").executeQuery();

            while (rs.next()) {
                Huone h = new Huone(rs.getInt("id"), rs.getString("tyyppi"),
                        rs.getInt("numero"), rs.getDouble("hinta"));
                huoneetListalla.add(h);
            }
            for (Huone Huone : huoneetListalla) {
                System.out.println(Huone.getTyyppi() + ", " + Huone.getNumero() + ", " + Huone.getHinta() + " euroa");
            }
            rs.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void haeHuoneita(Scanner s) throws SQLException {

        System.out.println("Haetaan huoneita");
        System.out.println("");
        System.out.println("Milloin varaus alkaisi (yyyy-MM-dd)?");
        Date alku = Date.valueOf(s.nextLine());
        Date loppu = null;
        Boolean ok = false;
        while (ok == false) {
            System.out.println("Milloin varaus loppuisi (yyyy-MM-dd)?");
            loppu = Date.valueOf(s.nextLine());
            if (loppu.after(alku)) {
                ok = true;
            }
        }

        String tyyppi = "";
        System.out.println("Minkä tyyppinen huone? (tyhjä = ei rajausta)");
        String tyyppi_input = s.nextLine();
        String tyyppirajaus;
        if (!tyyppi_input.equals(tyyppi)) {
            tyyppi = tyyppi_input;
            tyyppirajaus = " AND tyyppi = '" + tyyppi + "'";
        } else {
            tyyppirajaus = "";
        }
        Integer maksimihinta;
        System.out.println("Minkä hintainen korkeintaan? (tyhjä = ei rajausta)");
        String maksimihintakayttajalta = s.nextLine();
        if (maksimihintakayttajalta.equals("")) {
            maksimihinta = 3000;
        } else {
            maksimihinta = Integer.valueOf(maksimihintakayttajalta);
        }
        System.out.println("Vapaat huoneet: ");

        List<Huone> kyselynTulosHuoneet = Huone.haeHuoneetListalle(alku, loppu, tyyppirajaus, maksimihinta);

        if (kyselynTulosHuoneet.isEmpty()) {
            System.out.println("Ei vapaita huoneita.");
        } else {
            for (Huone Huone : kyselynTulosHuoneet) {
                System.out.println(Huone);
            }
            System.out.println();
        }
    }

    static List haeHuoneetListalle(Date alku, Date loppu, String tyyppirajaus, Integer maksimihinta) throws SQLException {
        Connection connection;
        List<Huone> kyselynTulosHuoneet = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "");
            PreparedStatement stmt = connection.prepareStatement("SELECT id, tyyppi, numero, hinta FROM Huone "
                    + "WHERE hinta <= ? "
                    + tyyppirajaus
                    + "AND Huone.id NOT IN ("
                    + "SELECT DISTINCT Huone.id FROM Huone "
                    + "LEFT JOIN Varattuhuone ON Varattuhuone.huone_id = Huone.id "
                    + "LEFT JOIN Varaus ON Varaus.id = VarattuHuone.varaus_id "
                    + "WHERE(alkupvm <= ?) AND (loppupvm >= ?)"
                    + ") ORDER BY hinta DESC;");
            stmt.setInt(1, maksimihinta);
            stmt.setDate(2, loppu);
            stmt.setDate(3, alku);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Huone h = new Huone(rs.getInt("id"), rs.getString("tyyppi"),
                        rs.getInt("numero"), rs.getDouble("hinta"));
                kyselynTulosHuoneet.add(h);
            }

            stmt.close();
            rs.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kyselynTulosHuoneet;
    }
}
