
import java.util.List;
import java.util.Scanner;

public class userInterface {

    public userInterface(allData UISData) {
    }

    public void startUI(allData UISData, Scanner s) {

        OUTER:
        while (true) {
            System.out.println("Locations available: \n");
            List<String> filteredLocations = UISData.getLocations();

            int counter = 0;
            for (int i = 0; i < 10; i++) {
                for (int l = 0; l < 10; l++) {
                    System.out.print(filteredLocations.get(counter));
                    System.out.print(" ");
                    counter++;
                }
                System.out.println();
                System.out.println();
            }

            String locationInput = "";
            while (!UISData.getAllLocations().contains(locationInput)) {
                System.out.println("Insert location: (insert x to quit)");
                locationInput = s.nextLine().toUpperCase();
                if (locationInput.equals("X")) {
                    break OUTER;
                }
            }

            System.out.println("Years available:");

            for (int y : UISData.getYearsForLocation(locationInput)) {
                System.out.print(y + " ");
            }
            int yearInput = -1;

            while (!UISData.getYearsForLocation(locationInput).contains(yearInput)) {
                yearInput = Integer.valueOf(s.nextLine());
            }

            System.out.println("Fetching data for location " + locationInput + ", year " + yearInput);
            UISData.printDataForLocationAndYear(locationInput, yearInput);
        }

    }
}
