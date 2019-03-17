
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class stream_UIS {

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        allData UISData = new allData();
        userInterface ui = new userInterface(UISData);

        try {
            Files.lines(Paths.get(args[0]))
                    .skip(1)
                    .map(l -> l.replace("\"", ""))
                    .map(row -> row.split(","))
                    .map(a -> new Line(a[0], a[1], a[2], a[3], a[4], a[6], a[7]))
                    .forEach(line -> UISData.addLine(line));
        } catch (Exception e) {
            System.out.println("Virhe: " + e.getMessage());
        }
        UISData.printSize();
        ui.startUI(UISData, s);
    }

}
