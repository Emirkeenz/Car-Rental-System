package org.example.carrentalsystem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReserveDAO {
    private Connection connection;
    private final CarsDAO carsDAO;

    public ReserveDAO() {
        String url = "jdbc:postgresql://dpg-ctarc78gph6c73erli4g-a.frankfurt-postgres.render.com:5432/car_rental_system";
        String username = "postgres1";
        String password = "sgPzhNmgGnTsUwlzLlj87aX0VoCCePF0";

        try {
            connection = DriverManager.getConnection(url, username, password);
            carsDAO = new CarsDAO();
            System.out.println("Reserve Database is successfully connected...");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to Reserve Database: " + e.getMessage(), e);
        }
    }

    public void reserveCar(int carId, int userId, LocalDate startDate, LocalDate endDate) {
        String insertQuery = "INSERT INTO reserves (carid, userid, datereserved, datereturned) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, carId);
            stmt.setInt(2, userId);
            stmt.setDate(3, Date.valueOf(startDate));
            stmt.setDate(4, Date.valueOf(endDate));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while reserving car: " + e.getMessage());
        }
    }

    public List<Car> getReservedCars(LocalDate startDate, LocalDate endDate) {
        List<Car> reservedCars = new ArrayList<>();
        String query = "SELECT DISTINCT carid FROM reserves WHERE ? <= datereturned AND ? >= datereserved";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Car car = carsDAO.getCarById(rs.getInt("carid"));
                    if (car != null) {
                        reservedCars.add(car);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reserved cars: " + e.getMessage());
        }
        return reservedCars;
    }

    public List<Car> ViewAvailableCars(LocalDate startDate, LocalDate endDate){
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM reserves";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                if(!overlap(rs.getDate("datereserved").toLocalDate(), rs.getDate("datereturned").toLocalDate(), startDate, endDate))//проверяем если датырезервации пересекаются с желаемыми датами
                    cars.add(carsDAO.getCarById(rs.getInt("carid")));
            }
        } catch (SQLException e) {
            //System.out.println("Ошибка: " + e.getMessage());
            StackTraceElement element = e.getStackTrace()[0];
            System.out.println("Ошибка в классе " + element.getClassName() +
                    ", методе " + element.getMethodName() +
                    " на строке " + element.getLineNumber() +
                    ": " + e.getMessage());
        }
        return cars;

    }

    public List<Reserve> getAllReservations(){
        List<Reserve> Reserves = new ArrayList<>();
        String sql = "SELECT * FROM reserves";
        try(PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reserve reserve = new Reserve();
                reserve.setStartDate(rs.getDate("datereserved").toLocalDate());
                reserve.setEndDate(rs.getDate("datereturned").toLocalDate());
                reserve.setReserveId(rs.getInt("reserveid"));
                reserve.setCarId(rs.getInt("carid"));
                reserve.setUserId(rs.getInt("userid"));
             Reserves.add(reserve);
            }
        } catch (Exception e) {
            //System.out.println("Ошибка: " + e.getMessage());
            StackTraceElement element = e.getStackTrace()[0];
            System.out.println("Ошибка в классе " + element.getClassName() +
                    ", методе " + element.getMethodName() +
                    " на строке " + element.getLineNumber() +
                    ": " + e.getMessage());
        }
        return Reserves;
    }


    public List<Reserve> getClientReservations(int userid){
        List<Reserve> clientReserves = new ArrayList<>();
        String sql = "SELECT * FROM reserves WHERE userid = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            stmt.setInt(1, userid);
            while (rs.next()) {
                Reserve reserve = new Reserve();
                reserve.setStartDate(rs.getDate("datereserved").toLocalDate());
                reserve.setEndDate(rs.getDate("datereturned").toLocalDate());
                reserve.setReserveId(rs.getInt("reserveid"));
                reserve.setCarId(rs.getInt("carid"));
                reserve.setUserId(rs.getInt("userid"));
                clientReserves.add(reserve);
            }
        } catch (Exception e) {
            //System.out.println("Ошибка: " + e.getMessage());
            StackTraceElement element = e.getStackTrace()[0];
            System.out.println("Ошибка в классе " + element.getClassName() +
                    ", методе " + element.getMethodName() +
                    " на строке " + element.getLineNumber() +
                    ": " + e.getMessage());
        }
        return clientReserves;
    }

    public void removeReservation(int reserveId) {
        String deleteQuery = "DELETE FROM reserves WHERE reserveid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, reserveId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing reservation: " + e.getMessage());
        }
    }

    private static boolean overlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return !startDate1.isAfter(endDate2) && !startDate2.isAfter(endDate1);
    }
}
