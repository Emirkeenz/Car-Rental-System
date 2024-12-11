package org.example.carrentalsystem;

import java.time.LocalDate;

public class Reserve {
    private LocalDate startDate;
    private LocalDate endDate;
    private int userId;
    private int carId;
    private int reserveId;

    // Constructor with all fields
    public Reserve(LocalDate startDate, LocalDate endDate, int userId, int carId, int reserveId) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.carId = carId;
        this.reserveId = reserveId;
    }

    // Default constructor
    public Reserve() {
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now();
    }

    // Getters and Setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (this.endDate != null && startDate.isAfter(this.endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (this.startDate != null && endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("End date must be after or equal to start date.");
        }
        this.endDate = endDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive integer.");
        }
        this.userId = userId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        if (carId <= 0) {
            throw new IllegalArgumentException("Car ID must be a positive integer.");
        }
        this.carId = carId;
    }

    public int getReserveId() {
        return reserveId;
    }

    public void setReserveId(int reserveId) {
        if (reserveId < 0) {
            throw new IllegalArgumentException("Reserve ID must be non-negative.");
        }
        this.reserveId = reserveId;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Reserve{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", userId=" + userId +
                ", carId=" + carId +
                ", reserveId=" + reserveId +
                '}';
    }
}
