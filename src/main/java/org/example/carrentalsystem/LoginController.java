package org.example.carrentalsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private UserDAO userDAO;

    public LoginController() {
        // Создание экземпляра UserDAO для взаимодействия с базой данных
        userDAO = new UserDAO();
    }

    @FXML
    public void initialize() {
        // Привязываем обработчик к кнопке логина
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
            // Логика для успешного входа
            showAlert("Success", "Login successful! Welcome, " + user.getUserName());
            // Здесь можно перейти на другой экран в зависимости от роли (например, для администратора или клиента)
            navigateToDashboard(user);  // Перенаправление в зависимости от роли
        } else {
            showAlert("Error", "Invalid username or password.");
        }
    }

    private User authenticateUser(String username, String password) {
        // Получаем всех пользователей и ищем соответствие по имени пользователя и паролю
        for (User user : userDAO.getAllUsers()) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;  // Если пользователь не найден
    }

    private void showAlert(String title, String message) {
        // Отображаем сообщение об ошибке или успехе
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToDashboard(User user) {
        // Логика перехода в зависимости от роли пользователя (к примеру, администратор или клиент)
        if (user.getRole() == Role.ADMIN) {
            // Перенаправляем на экран администратора
            System.out.println("Welcome, Admin!");
            // Здесь можно использовать метод для загрузки соответствующего экрана для админа
        } else if (user.getRole() == Role.CLIENT) {
            // Перенаправляем на экран клиента
            System.out.println("Welcome, Client!");
            // Здесь можно использовать метод для загрузки соответствующего экрана для клиента
        }
    }
}
