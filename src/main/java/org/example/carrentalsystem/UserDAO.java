package org.example.carrentalsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
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

    // Метод для добавления пользователя
    public void addUser(User user) {
        String query = "INSERT INTO users (userId, userName, password, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole().toString());  // Предполагается, что Role является enum

            statement.executeUpdate();
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
    public boolean updateUser(int userId, String newUserName, String newPassword, String newRole) {
        String sql = "UPDATE Users SET userName = ?, password = ?, role = ? WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newUserName);
            statement.setString(2, newPassword);
            statement.setString(3, newRole);
            statement.setInt(4, userId);
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
                int userId = resultSet.getInt("userId");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String roleString = resultSet.getString("role");
                Role role = Role.valueOf(roleString);  // Преобразуем строку в Enum

                // Используем Client или Admin в зависимости от роли
                if ("ADMIN".equalsIgnoreCase(roleString)) {
                    users.add(new Admin(userId, userName, password, role));
                } else {
                    users.add(new Client(userId, userName, password, role));
                }
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
