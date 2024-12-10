package org.example.carrentalsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        String url = "jdbc:postgresql://dpg-ctarc78gph6c73erli4g-a.frankfurt-postgres.render.com:5432/car_rental_system";
        String username = "postgres1";
        String password = "sgPzhNmgGnTsUwlzLlj87aX0VoCCePF0";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database is successfully connected...");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // Метод для добавления пользователя
    public void addUser(User user) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?) RETURNING userid";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int generatedId = resultSet.getInt("userid");
                user.setUserId(generatedId); // Сохранение сгенерированного ID в объекте
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Метод для удаления пользователя
    public void removeUser(int userId) {
        String query = "DELETE FROM users WHERE userId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для обновления пользователя
    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET userName = ?, password = ?, role = ? WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString()); // Преобразуем Role в String
            statement.setInt(4, user.getUserId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    // Метод для получения всех пользователей
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String roleString = resultSet.getString("role");
                Role role = Role.valueOf(roleString.toUpperCase());

                User user;
                if ("ADMIN".equalsIgnoreCase(roleString)) {
                    user = new Admin(userName, password, role); // Используем конструктор без userId
                } else {
                    user = new Client(userName, password, role); // Используем конструктор без userId
                }

                // Устанавливаем userId после создания объекта
                user.setUserId(resultSet.getInt("userid"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Метод для проверки существования пользователя
    public boolean userExists(String userName) {
        String sql = "SELECT COUNT(*) FROM Users WHERE userName = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }

    // Закрытие соединения
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
