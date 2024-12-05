package org.example.carrentalsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarsDAO {
    private Connection connection;

    public CarsDAO() {
        String url = "jdbc:postgresql:car_rental_system";
        String username = "postgres";
        String password = "Tls06141301";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database is successfully connected...");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // Добавление машины
    public void addCar(Car car) {
        String sql = "INSERT INTO Cars (carId, carName, carBrand, carModel, price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, car.getCarId());
            stmt.setString(2, car.getCarName());
            stmt.setString(3, car.getCarBrand());
            stmt.setString(4, car.getCarModel());
            stmt.setDouble(5, car.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Удаление машины
    public void removeCar(int carId) {
        String sql = "DELETE FROM Cars WHERE carId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, carId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Обновление информации о машине
    public void updateCar(Car car) {
        String sql = "UPDATE Cars SET carName = ?, carBrand = ?, carModel = ?, price = ? WHERE carId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarBrand());
            stmt.setString(3, car.getCarModel());
            stmt.setDouble(4, car.getPrice());
            stmt.setInt(5, car.getCarId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Получение списка всех машин
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM Cars";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("carId"),
                        rs.getString("carName"),
                        rs.getString("carBrand"),
                        rs.getString("carModel"),
                        rs.getDouble("price")
                );
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    // Получение информации о машине по ID
    public Car getCarById(int carId) {
        String sql = "SELECT * FROM Cars WHERE carId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, carId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Car(
                            rs.getInt("carId"),
                            rs.getString("carName"),
                            rs.getString("carBrand"),
                            rs.getString("carModel"),
                            rs.getDouble("price")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
