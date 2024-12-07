package org.example.carrentalsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.carrentalsystem.*;

public class AdminController {
    @FXML
    private TextField carNameField;
    @FXML
    private TextField carBrandField;
    @FXML
    private TextField carModelField;
    @FXML
    private TextField carPriceField;
    @FXML
    private TableView<Car> carsTable;
    @FXML
    private TableColumn<Car, Integer> carIdColumn;
    @FXML
    private TableColumn<Car, String> carNameColumn;
    @FXML
    private TableColumn<Car, String> carBrandColumn;
    @FXML
    private TableColumn<Car, String> carModelColumn;
    @FXML
    private TableColumn<Car, Double> carPriceColumn;

    @FXML
    private TextField userNameField;
    @FXML
    private TextField userPasswordField;
    @FXML
    private TextField userRoleField;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> userNameColumn;
    @FXML
    private TableColumn<User, String> userRoleColumn;
    @FXML
    private TableColumn<User, String> userPasswordColumn;
    @FXML
    private TableColumn<Client, Integer> userRentsColumn;

    private final CarsDAO carsDAO = new CarsDAO();
    private final UserDAO usersDAO = new UserDAO();

    private final ObservableList<Car> carList = FXCollections.observableArrayList();
    private final ObservableList<User> userList = FXCollections.observableArrayList();

    private void enterCarEditMode(Car car) {
        carNameField.setText(car.getCarName());
        carBrandField.setText(car.getCarBrand());
        carModelField.setText(car.getCarModel());
        carPriceField.setText(String.valueOf(car.getPrice()));
    }

    private void enterUserEditMode(User user) {
        userNameField.setText(user.getUserName());
        userPasswordField.setText(user.getPassword());
        userRoleField.setText(user.getRole().toString());
    }

    @FXML
    public void initialize() {
        // Инициализация таблицы машин
        carIdColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));
        carNameColumn.setCellValueFactory(new PropertyValueFactory<>("carName"));
        carBrandColumn.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        carPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        carsTable.setItems(carList);

        // Инициализация таблицы пользователей
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        usersTable.setItems(userList);

        // Загрузка данных из базы
        loadCars();
        loadUsers();

        carsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
                if (selectedCar != null) {
                    enterCarEditMode(selectedCar);
                }
            }
        });

        usersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                User selectedUser = usersTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    enterUserEditMode(selectedUser);
                }
            }
        });
    }

    private void loadCars() {
        carList.setAll(carsDAO.getAllCars());
    }

    private void loadUsers() {
        userList.setAll(usersDAO.getAllUsers());
    }

    @FXML
    private void addCar() {
        try {
            String name = carNameField.getText();
            String brand = carBrandField.getText();
            String model = carModelField.getText();
            double price = Double.parseDouble(carPriceField.getText());

            Car car = new Car(name, brand, model, price); // carId будет автоматически генерироваться
            carsDAO.addCar(car);
            loadCars();
            clearCarFields();
        } catch (Exception e) {
            showError("Не удалось добавить машину. Проверьте введенные данные.");
        }
    }

    @FXML
    private void deleteCar() {
        Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            carsDAO.removeCar(selectedCar.getCarId());
            loadCars();
        } else {
            showError("Выберите машину для удаления.");
        }
    }

    @FXML
    private void editCar() {
        Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            try {
                String brand = carBrandField.getText();
                String model = carModelField.getText();
                double price = Double.parseDouble(carPriceField.getText());

                selectedCar.setCarBrand(brand);
                selectedCar.setCarModel(model);
                selectedCar.setPrice(price);

                carsDAO.updateCar(selectedCar);
                loadCars();
                clearCarFields();
            } catch (Exception e) {
                showError("Не удалось обновить данные машины. Проверьте введенные данные.");
            }
        } else {
            showError("Выберите машину для редактирования.");
        }
    }

    @FXML
    private void addUser() {
        try {
            String name = userNameField.getText();
            String password = userPasswordField.getText();
            String role = userRoleField.getText();

            User user = new Client(name, password, Role.valueOf(role.toUpperCase())); // При создании пользователь получает свою роль
            usersDAO.addUser(user);
            loadUsers();
            clearUserFields();
        } catch (Exception e) {
            showError("Не удалось добавить пользователя. Проверьте введенные данные.");
        }
    }

    @FXML
    private void deleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            usersDAO.removeUser(selectedUser.getUserId());
            loadUsers();
        } else {
            showError("Выберите пользователя для удаления.");
        }
    }

    @FXML
    private void editUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                String name = userNameField.getText();
                String password = userPasswordField.getText();
                String role = userRoleField.getText();

                selectedUser.setUserName(name);
                selectedUser.setPassword(password);
                selectedUser.setRole(Role.valueOf(role.toUpperCase()));

                usersDAO.updateUser(selectedUser);
                loadUsers();
                clearUserFields();
            } catch (Exception e) {
                showError("Не удалось обновить данные пользователя. Проверьте введенные данные.");
            }
        } else {
            showError("Выберите пользователя для редактирования.");
        }
    }

    private void clearCarFields() {
        carNameField.clear();
        carBrandField.clear();
        carModelField.clear();
        carPriceField.clear();
    }

    private void clearUserFields() {
        userNameField.clear();
        userPasswordField.clear();
        userRoleField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
