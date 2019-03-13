
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class allData {

    List<Line> allData = new ArrayList<>();
    List<String> allLocations = new ArrayList<>();

    public allData() {
    }

    public List<Line> getAllData() {
        return allData;
    }

    public List<String> getAllLocations() {
        return allLocations;
    }

    public void addLine(Line l) {
        allData.add(l);
        allLocations.add(l.getLocation());
    }

    public void printSize() {
        System.out.println("Total row  count" + allData.size());
    }

    public List<String> getLocations() {

        List<String> filteredLocations = new ArrayList<>();

        allLocations.stream()
                .filter(l -> !l.startsWith("4"))
                .distinct()
                .forEach(l -> filteredLocations.add(l));
        
        return filteredLocations;
    }

    public List<Integer> getYearsForLocation(String locationInput) {
        List<Integer> yearsForLocation = new ArrayList<>();
        allData.stream()
                .filter(line -> line.getLocation().equals(locationInput))
                .map(line -> line.getYear())
                .distinct()
                .forEach(l -> yearsForLocation.add(l));
        return yearsForLocation;
    }

    public void printDataForLocationAndYear(String locationInput, int yearInput) {
        allData.stream()
                .filter(line -> line.getLocation().equals(locationInput))
                .filter(line -> line.getYear() == yearInput)
                .forEach(l -> System.out.println(l.toString()));
    }
}
