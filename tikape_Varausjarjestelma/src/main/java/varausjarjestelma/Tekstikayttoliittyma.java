package varausjarjestelma;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import java.sql.*;

@Component
public class Tekstikayttoliittyma {

    public void kaynnista(Scanner lukija) throws SQLException {
        while (true) {
            System.out.println();
            System.out.println("Komennot: ");
            System.out.println(" x - lopeta");
            System.out.println(" 1 - lisaa huone");
            System.out.println(" 2 - listaa huoneet");
            System.out.println(" 3 - hae huoneita");
            System.out.println(" 4 - lisaa varaus");
            System.out.println(" 5 - listaa varaukset");
            System.out.println(" 6 - tilastoja");
            System.out.println("");

            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
            }
            if (komento.equals("1")) {
                Huone.lisaaHuone(lukija);

            } else if (komento.equals("2")) {
                Huone.listaaHuoneet();
            } else if (komento.equals("3")) {
                Huone.haeHuoneita(lukija);
            } else if (komento.equals("4")) {
                Varaus.lisaaVaraus(lukija);
            } else if (komento.equals("5")) {
                Varaus.listaaVaraukset();
            } else if (komento.equals("6")) {
                tilastoja(lukija);
            } 
        }
    }

    private static void tilastoja(Scanner lukija) throws SQLException {
        System.out.println("Mitä tilastoja tulostetaan?");
        System.out.println("");
        System.out.println(" 1 - Suosituimmat lisävarusteet");
        System.out.println(" 2 - Parhaat asiakkaat");
        System.out.println(" 3 - Varausprosentti huoneittain");
        System.out.println(" 4 - Varausprosentti huonetyypeittäin");

        System.out.println("Syötä komento: ");
        int komento = Integer.valueOf(lukija.nextLine());

        if (komento == 1) {
            Lisavaruste.suosituimmatLisavarusteet();
        } else if (komento == 2) {

            Asiakas.parhaatAsiakkaat();
        } else if (komento
                == 3) {
            Prosenttimetodit.varausprosenttiHuoneittain(lukija);

        } else if (komento
                == 4) {
            Prosenttimetodit.varausprosenttiHuonetyypeittain(lukija);
        }
    }

    public static void alustaTietokanta() {

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            conn.prepareStatement("DROP TABLE Asiakas IF EXISTS;").executeUpdate();
            conn.prepareStatement("DROP TABLE Huone IF EXISTS;").executeUpdate();
            conn.prepareStatement("DROP TABLE Lisavaruste IF EXISTS;").executeUpdate();
            conn.prepareStatement("DROP TABLE VaratunHuoneenLisavaruste IF EXISTS;").executeUpdate();
            conn.prepareStatement("DROP TABLE Varattuhuone IF EXISTS;").executeUpdate();
            conn.prepareStatement("DROP TABLE Varaus IF EXISTS;").executeUpdate();

            conn.prepareStatement("CREATE TABLE Asiakas "
                    + "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,kokonimi VARCHAR(255) NOT NULL,puhelinnumero VARCHAR(255),email VARCHAR(255))"
                    + ";").executeUpdate();

            conn.prepareStatement("CREATE TABLE Varaus "
                    + "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, asiakas_id INT NOT NULL, toteutunut BOOLEAN NOT NULL DEFAULT true, alkupvm DATE, "
                    + "loppupvm DATE, FOREIGN KEY (asiakas_id) REFERENCES Asiakas(id));").executeUpdate();

            conn.prepareStatement("CREATE INDEX Varaus_idx_id ON Varaus(id);\n"
                    + "CREATE INDEX Varaus_idx_asiakas_id ON Varaus(asiakas_id);").executeUpdate();

            conn.prepareStatement("CREATE TABLE Huone "
                    + "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, tyyppi VARCHAR (50) NOT NULL, numero INT NOT NULL, hinta DECIMAL(12,2))"
                    + ";").executeUpdate();

            conn.prepareStatement("CREATE INDEX Huone_idx_id ON Huone(id);\n"
                    + "CREATE INDEX Huone_idx_tyyppi ON Huone(tyyppi);\n"
                    + "CREATE INDEX Huone_idx_numero ON Huone(numero);\n"
                    + "CREATE INDEX Huone_idx_hinta ON Huone(hinta);").executeUpdate();
            conn.prepareStatement("CREATE TABLE VarattuHuone (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                    + "varaus_id INT NOT NULL, huone_id INT NOT NULL, FOREIGN KEY (varaus_id) REFERENCES Varaus(id), "
                    + "FOREIGN KEY (huone_id) REFERENCES Huone(id));").executeUpdate();

            conn.prepareStatement("CREATE INDEX VarattuHuone_idx_id ON Varattuhuone(id);\n"
                    + "CREATE INDEX VarattuHuone_idx_varaus_id ON Varattuhuone (varaus_id);\n"
                    + "CREATE INDEX VarattuHuone_idx_huone_id ON Varattuhuone (huone_id);").executeUpdate();

            conn.prepareStatement("CREATE TABLE Lisavaruste (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, varuste VARCHAR(255));").executeUpdate();
            conn.prepareStatement("CREATE TABLE VaratunHuoneenLisavaruste (id INT NOT NULL AUTO_INCREMENT PRIMARY  KEY, "
                    + "varaus_id INT NOT NULL, lisavaruste_id INT NOT NULL, FOREIGN KEY (varaus_id) REFERENCES varaus(id), "
                    + "FOREIGN KEY (lisavaruste_id) REFERENCES Lisavaruste(id));").executeUpdate();

            conn.prepareStatement("CREATE INDEX VaratunHuoneenLisavaruste_idx_id ON VaratunHuoneenLisavaruste (id);\n"
                    + "CREATE INDEX VaratunHuoneenLisavaruste_idx_varaus_id ON VaratunHuoneenLisavaruste (varaus_id);\n"
                    + "CREATE INDEX VaratunHuoneenLisavaruste_idx_lisavaruste_id ON VaratunHuoneenLisavaruste (lisavaruste_id);").executeUpdate();
            conn.prepareStatement(""
                    + "INSERT INTO Huone (tyyppi, numero, hinta) VALUES ('Single', 101, 79.00);\n"
                    + "INSERT INTO Huone (tyyppi, numero, hinta) VALUES ('Single', 111, 79.00);\n"
                    + "INSERT INTO Huone (tyyppi, numero, hinta) VALUES ('Standard', 201, 99.00);\n"
                    + "INSERT INTO Huone (tyyppi, numero, hinta) VALUES ('Standard', 211, 99.00);\n"
                    + "INSERT INTO Huone (tyyppi, numero, hinta) VALUES ('Deluxe', 301, 130.00);\n"
                    + "INSERT INTO Huone (tyyppi, numero, hinta) VALUES ('Deluxe', 311, 130.00);\n"
                    + "INSERT INTO Huone (tyyppi, numero, hinta) VALUES ('Suite', 401, 150.00);\n"
                    + "INSERT INTO Huone (tyyppi, numero, hinta) VALUES ('Suite', 411, 150.00);\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('matkasänky');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('teekannu');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('lisävuode');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('kahvinkeitin');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('silitysrauta');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('satelliitti-tv');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('DVD-soitin');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('sanomalehti');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('aamiainen huoneeseen');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('leikkikehä');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('syöttötuoli');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('babysitter');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('henkilöauton pysäköinti');\n"
                    + "INSERT INTO Lisavaruste (varuste) VALUES ('kahvinkeitin');"
                    + "").executeUpdate();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
/*


RÄPELLYS:


public void kaynnista(Scanner lukija) throws SQLException {
        while (true) {
            System.out.println();
            System.out.println("Komennot: ");
            System.out.println(" x - lopeta");
            System.out.println(" 1 - lisaa huone");
            System.out.println(" 2 - listaa huoneet");
            System.out.println(" 3 - hae huoneita");
            System.out.println(" 4 - lisaa varaus");
            System.out.println(" 5 - listaa varaukset");
            System.out.println(" 6 - tilastoja");
            System.out.println("");

            String komento = lukija.nextLine();

            while (true) {
                if (!lukija.hasNextInt() || !komento.equals("x")) {

                    System.out.print(" ei ole kokonaisluku! ");
                    System.out.println("Yritä uudelleen!");
                    komento = lukija.nextLine();
                } else {
                    break;
                }
            }

            if (komento.equals("1")) {
                Huone.lisaaHuone(lukija);
            } else if (komento.equals("2")) {
                Huone.listaaHuoneet();
            } else if (komento.equals("3")) {
                Huone.haeHuoneita(lukija);
            } else if (komento.equals("4")) {
                Varaus.lisaaVaraus(lukija);
            } else if (komento.equals("5")) {
                Varaus.listaaVaraukset();
            } else if (komento.equals("6")) {
                tilastoja(lukija);
            } else if (komento.equals("777")) {
                System.out.println("Haluatko varmasti poistaa tiedot koko tietokannasta? Komentoa ei voi perua. Vahvista syöttämällä 777, muu painallus peruuttaa.");
                String vahvista = lukija.nextLine();
                if (vahvista.equals("777")) {
                    alustaTietokanta();
                }
            }
        }
    }




 */
