package walthamcars;

import java.util.ArrayList;
import java.util.Collections;

public class CarRepository {

    public static ArrayList<Car> cars;

    public CarRepository() {
        cars = new ArrayList();
    }

    public void addCar(Car c) {
        cars.add(c);
    }

    public void printAll() {
        for (Car car : cars) {
            System.out.println(car.toString());
        }
    }

    public ArrayList<Car> returnAll() {
        return cars;
    }

    public ArrayList<Car> carListTop10Price() {
        Collections.sort(cars);
        ArrayList<Car> carsOutput = new ArrayList();
        for (int i = 0; i < 10; i++) {
            carsOutput.add(cars.get(i));
        }
        return carsOutput;
    }

    public ArrayList<Car> carListLowest10Price() {
        Collections.sort(cars);
        ArrayList<Car> carsOutput = new ArrayList();
        for (int i = cars.size() - 1; i > cars.size() - 10; i--) {
            carsOutput.add(cars.get(i));
        }
        return carsOutput;
    }
}
