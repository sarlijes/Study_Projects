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
            Files.lines(Paths.get("car_data.csv"))
                    .map(l -> l.replace(".", ""))
                    .map(row -> row.split(","))
                    .map(a -> new Car(a[1], a[2], a[4], a[5], a[3], a[6]))
                    .forEach(car -> carRepository.addCar(car));

            System.out.println("All cars saved");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void readWorkerFile(PersonRepository personRepository) {
        try {
            Files.lines(Paths.get("worker_data.csv"))
                    .skip(1)
                    .map(row -> row.split(","))
                    .map(a -> new Worker((a[0]), a[1], a[2], a[3], a[4]))
                    .forEach(w -> personRepository.addWorker(w));

            System.out.println("All data saved");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
//1,Mavra,Tabram,mtabram0@skype.com,Engineering
