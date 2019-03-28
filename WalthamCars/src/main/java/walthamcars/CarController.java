package walthamcars;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CarController {

    public static void readCarFile(CarRepository carRepository) {
        try {
            Files.lines(Paths.get("MOCK_DATA (1).csv"))
                    .map(l -> l.replace(".", ""))
                    .map(row -> row.split(","))
                    .map(a -> new Car(a[1], a[2], a[4], a[5], a[3], a[6]))
                    .forEach(car -> carRepository.addCar(car));

            System.out.println("All data saved");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    public static void readWorkerFile(CarRepository carRepository) {
        try {
            Files.lines(Paths.get("MOCK_DATA (1).csv"))
                    .map(l -> l.replace(".", ""))
                    .map(row -> row.split(","))
                    .map(a -> new Car(a[1], a[2], a[4], a[5], a[3], a[6]))
                    .forEach(car -> carRepository.addCar(car));

            System.out.println("All data saved");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
