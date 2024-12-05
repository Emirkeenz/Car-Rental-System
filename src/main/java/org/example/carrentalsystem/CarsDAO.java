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
        String sql = "INSERT INTO Cars (carName, carBrand, carModel, price) VALUES (?, ?, ?, ?) RETURNING carId";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, car.getCarName());
            stmt.setString(2, car.getCarBrand());
            stmt.setString(3, car.getCarModel());
            stmt.setDouble(4, car.getPrice());

            System.out.println("Executing query: " + stmt.toString()); // Логирование SQL-запроса

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        car.setCarId(generatedKeys.getInt(1));
                        System.out.println("Generated carId: " + car.getCarId()); // Логирование сгенерированного ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error executing addCar: " + e.getMessage());
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
                        rs.getString("carName"),
                        rs.getString("carBrand"),
                        rs.getString("carModel"),
                        rs.getDouble("price")
                );
                car.setCarId(rs.getInt("carId"));  // carId будет извлечен и установлен отдельно
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
                    Car car = new Car(
                            rs.getString("carName"),
                            rs.getString("carBrand"),
                            rs.getString("carModel"),
                            rs.getDouble("price")
                    );
                    car.setCarId(rs.getInt("carId"));  // Устанавливаем carId после создания объекта
                    return car;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
