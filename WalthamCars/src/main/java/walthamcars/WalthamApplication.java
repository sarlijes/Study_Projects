package walthamcars;

import java.io.FileReader;
import java.util.*;
import java.util.Collections;

public class WalthamApplication {

    static CarRepository carRepository = new CarRepository();

    public static void main(String[] args) {
        CarController carController = new CarController();
//        carController.readCarFile();
//        run(WalthamApplication.class, args);
        CarController.readCarFile(carRepository);
//        carRepository.printAll();

        ArrayList<Car> low = carRepository.carListLowest10Price();
        for (Car car : low) {
//            System.out.println(car);
        }

        ArrayList<Car> high = carRepository.carListTop10Price();
        for (Car car : high) {
//            System.out.println(car);
        }

        // List all cars from old to new:
        ArrayList<Car> allCars = carRepository.returnAll();
//        allCars.stream()
//                .sorted((c1, c2) -> {
//                    return c1.getYearInt() - c2.getYearInt();
//                }).forEach(c -> System.out.println(c.toString()));

//        allCars.stream()
//                .sorted((c1, c2) -> {
//                    return c1.getColor().compareTo(c2.getColor());
//                }).forEach(c -> System.out.println(c.toString()));
//
//        Comparator<Car> comparator = Comparator
//                .comparing(Car::getYearInt)
//                .thenComparing(Car::getPriceInt);
//
//        Collections.sort(allCars, comparator);
//
//        for (Car car : allCars) {
//            System.out.println(car);
//        }
    }
}
