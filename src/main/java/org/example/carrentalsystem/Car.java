package org.example.carrentalsystem;

public class Car {
    protected int carId;
    protected String carName;
    public void setCarId(int carId) {
        this.carId = carId;
    }
    public void setCarName(String carName) {
        this.carName = carName;
    }
    public int getCarId() {
        return carId;
    }
    public String getCarName() {
        return carName;
    }
}
