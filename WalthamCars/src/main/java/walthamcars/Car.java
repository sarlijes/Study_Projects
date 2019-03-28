package walthamcars;

import java.util.ArrayList;
import java.util.List;

public class Car implements Comparable<Car> {

    private String make, model, vin, color;
    private String year, price;
    private Integer yearInt, priceInt;

    public Car(String make, String model, String vin, String color, String year, String price) {
        this.make = make;
        this.model = model;
        this.vin = vin;
        this.color = color;
        this.year = year;
        this.price = price;
        this.yearInt = Integer.valueOf(year);
        this.priceInt = Integer.valueOf(price);
    }

    @Override
    public int compareTo(Car car) {
        return this.priceInt - car.getPriceInt();
    }

    @Override
    public String toString() {
        return "Car{" + "make=" + make + ", model=" + model + ", vin=" + vin + ", color=" + color +  ", yearInt=" + yearInt  + ", priceInt=   " + priceInt / 100 + '}';
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getVin() {
        return vin;
    }

    public String getColor() {
        return color;
    }

    public Integer getYearInt() {
        return yearInt;
    }

    public Integer getPriceInt() {
        return priceInt;
    }

}
