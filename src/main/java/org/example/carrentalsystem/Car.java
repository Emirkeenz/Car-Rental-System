package org.example.carrentalsystem;

import java.util.Objects;

public class Car {
    protected int carId;
    protected String carName;
    protected String carBrand;
    protected String carModel;
    protected double price;

    public Car(String carName, String carBrand, String carModel, double price) {
        this.carName = carName;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.price = price;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Если объекты одинаковые (одна и та же ссылка)
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return carId == car.carId;  // Сравниваем только по carId
    }


    @Override
    public int hashCode() {
        return Objects.hash(carId);
    }
}
