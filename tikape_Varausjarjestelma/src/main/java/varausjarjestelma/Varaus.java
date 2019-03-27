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

public class Varaus {

    List<VarattuHuone> varatuthuoneet;

    Asiakas asiakas;
    int id;
    int asiakas_id;
    Boolean toteutunut;

    public Varaus(List<VarattuHuone> varatuthuoneet, Asiakas asiakas, int id, int asiakas_id, Boolean toteutunut) {
        this.varatuthuoneet = varatuthuoneet;
        this.asiakas = asiakas;
        this.id = id;
        this.asiakas_id = asiakas_id;
        this.toteutunut = toteutunut;
    }

    public int getId() {
        return id;
    }

    public int getAsiakas_id() {
        return asiakas_id;
    }

    public Boolean getToteutunut() {
        return toteutunut;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAsiakas_id(int asiakas_id) {
        this.asiakas_id = asiakas_id;
    }

    public void setToteutunut(Boolean toteutunut) {
        this.toteutunut = toteutunut;
    }

    @Override
    public String toString() {
        return this.id + " id. toString Varaukselle, viimeistele";

    }

    static void lisaaVaraus(Scanner s) throws SQLException {
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
            maksimihinta = 3000000;
        } else {
            maksimihinta = Integer.valueOf(maksimihintakayttajalta);
        }
        System.out.println("Vapaat huoneet: ");
        int vapaathuoneet = 0;

        List<Huone> kyselynTulosHuoneet = Huone.haeHuoneetListalle(alku, loppu, tyyppirajaus, maksimihinta);

        if (kyselynTulosHuoneet.isEmpty()) {
            System.out.println("Ei vapaita huoneita.");
        } else {
            for (Huone Huone : kyselynTulosHuoneet) {
                System.out.println(Huone);
                vapaathuoneet++;
            }
            System.out.println();
        }
        System.out.println("Huoneita vapaana " + vapaathuoneet);
        int haluttuHuoneMaara;
        while (true) {
            System.out.println("Montako huonetta varataan?");
            haluttuHuoneMaara = Integer.valueOf(s.nextLine());
            if (haluttuHuoneMaara >= 1 && haluttuHuoneMaara <= vapaathuoneet) {
                break;
            }
            System.out.println("Epäkelpo huoneiden lukumäärä.");
        }

        List<Lisavaruste> lisavarusteet = new ArrayList<>();
        while (true) {
            System.out.println("Syötä lisävaruste, tyhjä lopettaa");
            String lisavaruste = s.nextLine();
            if (lisavaruste.isEmpty()) {
                break;
            }
            Lisavaruste l = new Lisavaruste(-1, lisavaruste);
            lisavarusteet.add(l);

        }
        System.out.println("Syötä varaajan nimi:");

        String nimi = s.nextLine();
        System.out.println("Syötä varaajan puhelinnumero:");
        String puhelinnumero = s.nextLine();
        System.out.println("Syötä varaajan sähköpostiosoite:");
        String sahkoposti = s.nextLine();

        int asiakas_id = -1;
        asiakas_id = Asiakas.tarkistaLoytyykoAsiakas(nimi, puhelinnumero, sahkoposti);

        if (asiakas_id == -1) {
            asiakas_id = Asiakas.tallennaAsiakasTietokantaan(nimi, puhelinnumero, sahkoposti);
        }
        int varaus_id = -1;
        varaus_id = tallennaVaraus(alku, loppu, asiakas_id);
        for (int i = 0; i < haluttuHuoneMaara; i++) {
            int valitunHuoneenNumero = kyselynTulosHuoneet.get(i).getId();
            tallennaVarattuHuone(valitunHuoneenNumero, varaus_id);

        }
        // Käy läpi asiakkaan syöttämät lisävarusteet, vie jokainen niistä tietokantaan ja palauta id:t listalle:
        List<Integer> lisavaruste_id_lista = new ArrayList<>();

        if (!lisavarusteet.isEmpty()) {
            for (Lisavaruste l : lisavarusteet) {
                int lisavaruste_id = l.LisaaLisavarusteetTietokantaan(l);

                lisavaruste_id_lista.add(lisavaruste_id);
            }
        }
        // Käy läpi asiakkaan syöttämät lisävarusteet ja lisää ne varaukselle tietokantaan:
        if (!lisavarusteet.isEmpty()) {
            for (int i = 0; i < lisavaruste_id_lista.size(); i++) {
                String varuste = lisavarusteet.get(i).getVaruste();
                int lisavaruste_id = lisavaruste_id_lista.get(i);
                Lisavaruste.LisaaVaraukselleLisavaruste(varaus_id, lisavaruste_id, varuste);
            }
        }
        kyselynTulosHuoneet.clear();
        lisavarusteet.clear();

    }

    public static int tallennaVaraus(Date alku, Date loppu, Integer id) {
        int varaus_id = -1;

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {

            PreparedStatement uusiVarausLisays = conn.prepareStatement("INSERT INTO Varaus (asiakas_id, toteutunut, alkupvm, loppupvm) "
                    + "VALUES (?, ?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);

            uusiVarausLisays.setInt(1, id);
            uusiVarausLisays.setBoolean(2, true);
            uusiVarausLisays.setDate(3, alku);
            uusiVarausLisays.setDate(4, loppu);
            uusiVarausLisays.executeUpdate();

            ResultSet generatedKeys = uusiVarausLisays.getGeneratedKeys();
            if (generatedKeys.next()) {
                varaus_id = generatedKeys.getInt(1);
            }
            uusiVarausLisays.close();
            generatedKeys.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
        return varaus_id;
    }

    public static void tallennaVarattuHuone(Integer valitunHuoneenNumero, Integer varaus_id) {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {

            PreparedStatement uusiVarattuHuoneLisays = conn.prepareStatement("INSERT INTO Varattuhuone (varaus_id, huone_id) VALUES (?, ?);");
            uusiVarattuHuoneLisays.setInt(1, varaus_id);
            uusiVarattuHuoneLisays.setInt(2, valitunHuoneenNumero);
            uusiVarattuHuoneLisays.executeUpdate();

            uusiVarattuHuoneLisays.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void listaaVaraukset() {
        System.out.println("Listataan varaukset");
        System.out.println("");

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            ResultSet rs_varaus = conn.prepareStatement("SELECT Asiakas.id, kokonimi, email, alkupvm, loppupvm, (loppupvm-alkupvm), "
                    + "COUNT (DISTINCT VaratunhuoneenLisavaruste.lisavaruste_id), "
                    + "COUNT (DISTINCT Varattuhuone.Huone_id) FROM Varaus "
                    + "JOIN Asiakas ON Asiakas.id = Varaus.asiakas_id "
                    + "LEFT JOIN VaratunhuoneenLisavaruste ON VaratunhuoneenLisavaruste.varaus_id = Varaus.id "
                    + "JOIN Varattuhuone ON Varattuhuone.varaus_id = Varaus.id "
                    + "JOIN Huone ON Huone.id = Varattuhuone.huone_id "
                    + "GROUP BY Asiakas.id, Varaus.alkupvm"
                    + ";").executeQuery();

            if (rs_varaus.isLast()) {
                System.out.println("Ei varauksia");
            } else {
                while (rs_varaus.next()) {

                    Integer asiakas_id = Integer.valueOf(rs_varaus.getString("id"));
                    String kokonimi = rs_varaus.getString("kokonimi");
                    String email = rs_varaus.getString("email");
                    Date alku = rs_varaus.getDate("alkupvm");
                    Date loppu = rs_varaus.getDate("loppupvm");
                    Integer paivienMaara = rs_varaus.getInt("DATEDIFF('DAY', ALKUPVM, LOPPUPVM)");
                    Integer lisavarusteidenMaara = rs_varaus.getInt("COUNT(DISTINCT VARATUNHUONEENLISAVARUSTE.LISAVARUSTE_ID)");
                    Integer huoneidenMaara = rs_varaus.getInt("COUNT(DISTINCT VARATTUHUONE.HUONE_ID)");

                    String paivaaYksikkoVaiMonikko = "ä";
                    if (paivienMaara > 1) {
                        paivaaYksikkoVaiMonikko = "ää";
                    }

                    System.out.println(kokonimi + ", "
                            + email
                            + ", "
                            + alku
                            + ", "
                            + loppu
                            + ", "
                            + paivienMaara
                            + " päiv"
                            + paivaaYksikkoVaiMonikko
                            + ", "
                            + lisavarusteidenMaara
                            + " lisävarustetta, "
                            + huoneidenMaara
                            + " huonetta. Huoneet:");

                    PreparedStatement stmt = conn.prepareStatement("SELECT tyyppi, numero, hinta FROM Huone "
                            + "JOIN Varattuhuone ON Varattuhuone.huone_id = Huone.id "
                            + "JOIN Varaus ON Varaus.id = Varattuhuone.varaus_id "
                            + "WHERE asiakas_id = ?"
                            + "GROUP BY numero;");
                    stmt.setInt(1, asiakas_id);
                    ResultSet rs_huoneet = stmt.executeQuery();
                    Double summa = 0.00;
                    while (rs_huoneet.next()) {
                        String tyyppi = rs_huoneet.getString("tyyppi");
                        String numero = rs_huoneet.getString("numero");
                        Double hinta = rs_huoneet.getDouble("hinta");
                        summa = summa + paivienMaara * hinta;

                        System.out.println(tyyppi + ", " + numero + ", " + hinta + " euroa");
                    }
                    System.out.println("Yhteensä: " + summa + " euroa");
                    stmt.close();
                    rs_huoneet.close();

                }
            }
            rs_varaus.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
