package org.example.carrentalsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.*;


public class ReserveDAO {
    private Connection connection;
    CarsDAO carsDAO;
    public ReserveDAO() {
        String url = "jdbc:postgresql://dpg-ctarc78gph6c73erli4g-a.frankfurt-postgres.render.com:5432/car_rental_system";
        String username = "postgres1";
        String password = "sgPzhNmgGnTsUwlzLlj87aX0VoCCePF0";
//        String url = "jdbc:postgresql:car_rental_system";
//        String username = "postgres";
//        String password = "Tls06141301";


        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Reserve Database is successfully connected...");
        } catch (SQLException e) {
            System.out.println("Reserve Database connection failed: " + e.getMessage());
        }
    }
    public void reserveCar(int carid,int userid, LocalDate startDate, LocalDate endDate) {
        String insertQuery = "INSERT INTO reserves (carid, userid, datereserved, datereturned) VALUES (?, ?, ?, ?)";
             try(PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, carid);
            stmt.setInt(2, userid);
            stmt.setDate(3, Date.valueOf(startDate));  // Преобразование LocalDate в java.sql.Date
            stmt.setDate(4, Date.valueOf(endDate));
            stmt.executeUpdate();
        } catch (Exception e) {
                 System.out.println("Ошибка: " + e.getMessage());
        }

    }
    public List<Car> ReservedCars(LocalDate startDate, LocalDate endDate){
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM reserves";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                if(overlap(rs.getDate("datereserved").toLocalDate(), rs.getDate("datereturned").toLocalDate(), startDate, endDate))//проверяем если датырезервации пересекаются с желаемыми датами
                cars.add(carsDAO.getCarById(rs.getInt("carid")));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        return cars;
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
            System.out.println("Ошибка: " + e.getMessage());
        }
        return cars;

    }
    public List<Reserve> ViewClientReserves(int userid){
        List<Reserve> clientReserves = new ArrayList<>();
        String sql = "SELECT * FROM reserves WHERE userid = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery(sql)) {
            stmt.setInt(1, userid);
            while (rs.next()) {
                Reserve reserve = new Reserve();
                        reserve.setStartDate(rs.getDate("datereserved"));
                        reserve.setEndDate(rs.getDate("datereturned"));
                        reserve.setReserveId(rs.getInt("reserveid"));
                        reserve.setCarId(rs.getInt("carid"));
                        reserve.setUserId(rs.getInt("userid"));
                clientReserves.add(reserve);
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        return clientReserves;
    }
    public void GetCarById(){

    }
    private static boolean overlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return !startDate1.isAfter(endDate2) && !startDate2.isAfter(endDate1);
    }
}
