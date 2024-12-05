package org.example.carrentalsystem;

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
}
