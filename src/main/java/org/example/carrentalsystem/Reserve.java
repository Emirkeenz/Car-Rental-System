package org.example.carrentalsystem;

import java.util.Date;

public class Reserve {
    protected Date startDate;
    protected Date endDate;
    protected int userId;
    protected int carId;
    protected int reserveId;

    public Reserve(Date startDate, Date endDate, int userId, int carId, int reserveId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.carId = carId;
        this.reserveId = reserveId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
