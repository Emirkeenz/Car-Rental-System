package org.example.carrentalsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private final UserDAO userDAO;

    public LoginController() {
        userDAO = new UserDAO(); // Создание экземпляра UserDAO для взаимодействия с базой данных
    }

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Both fields are required.");
            return;
        }

        User user = authenticateUser(username, password);
        if (user != null) {
            // Логика успешного входа
            navigateToDashboard(user); // Перенаправление в зависимости от роли
        } else {
            showAlert("Error", "Invalid username or password.");
        }
    }

    private User authenticateUser(String username, String password) {
        // Проверяем имя пользователя и пароль
        for (User user : userDAO.getAllUsers()) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // Пользователь не найден
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToDashboard(User user) {
        String fxmlFile;
        String title;

        if (user.getRole() == Role.ADMIN) {
            fxmlFile = "AdminPanel.fxml"; // Файл интерфейса для администратора
            title = "Admin Dashboard";
        } else if (user.getRole() == Role.CLIENT) {
            fxmlFile = "ClientPanel.fxml"; // Файл интерфейса для клиента
            title = "Client Dashboard";
        } else {
            showAlert("Error", "Unknown role: " + user.getRole());
            return;
        }

        loadScene(fxmlFile, title);
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            // Загружаем FXML-файл
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Получаем текущую сцену и меняем её
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Failed to load " + title + " screen.");
        }
    }
}
