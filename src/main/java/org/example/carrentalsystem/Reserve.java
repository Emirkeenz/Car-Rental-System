package org.example.carrentalsystem;

import java.util.Date;

public class Reserve {
    Date startDate;
    Date endDate;
    protected int userId;
    protected int carId;
    protected int reserveId;

    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getCarId() {
        return carId;
    }
    public void setCarId(int carId) {
        this.carId = carId;
    }
    public int getReserveId() {
        return reserveId;
    }
    public void setReserveId(int reserveId) {
        this.reserveId = reserveId;
    }

}
