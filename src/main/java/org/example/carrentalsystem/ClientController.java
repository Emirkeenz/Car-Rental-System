package org.example.carrentalsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ClientController {

    @FXML
    private TableView<Car> carsTable;

    @FXML
    private TableColumn<Car, Integer> carIDColumn;

    @FXML
    private TableColumn<Car, String> carBrandColumn;

    @FXML
    private TableColumn<Car, String> carModelColumn;

    @FXML
    private TableColumn<Car, Double> carPriceColumn;

    @FXML
    private TextField carIDField;

    @FXML
    private TextField carBrandField;

    @FXML
    private TextField carModelField;

    @FXML
    private TextField carPriceField;

    @FXML
    private TextField dataRentedField;

    @FXML
    private TextField DataReturnedField;

    @FXML
    private DatePicker dateRentedField;

    @FXML
    private DatePicker dateReturnedField;

    @FXML
    private Button rentSubmitButton;

    @FXML
    private TableView<Reserve> yourCarsTable;

    @FXML
    private TableColumn<Reserve, Integer> reservedCarIDColumn;

    @FXML
    private TableColumn<Reserve, LocalDate> startDateColumn;

    @FXML
    private TableColumn<Reserve, LocalDate> endDateColumn;

    @FXML
    private Button returnCarButton;

    private final ObservableList<Car> availableCars = FXCollections.observableArrayList();
    private final ObservableList<Reserve> userReserves = FXCollections.observableArrayList();

    private final CarsDAO carsDAO = new CarsDAO();
    private final ReserveDAO reserveDAO = new ReserveDAO();

    private int currentUserId = 1;

    private void enterCarEditMode(Car car) {
        carIDField.setText(car.getCarName());
        carBrandField.setText(car.getCarBrand());
        carModelField.setText(car.getCarModel());
        carPriceField.setText(String.valueOf(car.getPrice()));
        dateRentedField.setValue(LocalDate.now());
        dateReturnedField.setValue(LocalDate.now());
    }



    @FXML
    public void initialize() {
        configureTables();
        loadCarsFromDatabase();
        loadUserReservations();
    }

    private void configureTables() {
        // Configure available cars table
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));
        carBrandColumn.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        carPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        carsTable.setItems(availableCars);

        carsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
                if (selectedCar != null) {
                    enterCarEditMode(selectedCar);
                }
            }
        });

        // Configure user's reserved cars table
        reservedCarIDColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        yourCarsTable.setItems(userReserves);
    }

    private void loadCarsFromDatabase() {
        List<Car> cars = carsDAO.getAllCars();
        availableCars.setAll(cars);
    }

    private void loadUserReservations() {
        List<Reserve> reservations = reserveDAO.getReservationsByUserId(currentUserId);
        userReserves.setAll(reservations);
    }

    @FXML
    private void submitRent(ActionEvent event) {
        try {
            int carId = Integer.parseInt(carIDField.getText());
            LocalDate startDate = dateRentedField.getValue();
            LocalDate endDate = dateReturnedField.getValue();

            if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
                showAlert("Ошибка даты", "Пожалуйста, введите правильные данные.", Alert.AlertType.WARNING);
                return;
            }

            Car car = availableCars.stream()
                    .filter(c -> c.getCarId() == carId)
                    .findFirst()
                    .orElse(null);

            if (car == null) {
                showAlert("Машина не найдена", "Машина с указанной ID не доступна", Alert.AlertType.WARNING);
                return;
            }

            Reserve newReservation = new Reserve(
                    java.sql.Date.valueOf(startDate),
                    java.sql.Date.valueOf(endDate),
                    currentUserId,
                    carId,
                    0
            );

            reserveDAO.addReservation(newReservation);
            userReserves.add(newReservation);
            availableCars.remove(car);
            clearRentForm();
            showAlert("Успех", "Машина была зарезервирована успешно.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Ошибка ввода", "Пожалуйста, введите правильные данные.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void returnCar(ActionEvent event) {
        Reserve selectedReserve = yourCarsTable.getSelectionModel().getSelectedItem();
        if (selectedReserve == null) {
            showAlert("Нет машин", "Пожалуйста, выберите машину для возврата.", Alert.AlertType.WARNING);
            return;
        }

        reserveDAO.removeReservation(selectedReserve.getReserveId());
        userReserves.remove(selectedReserve);

        Car returnedCar = carsDAO.getCarById(selectedReserve.getCarId());
        if (returnedCar != null) {
            availableCars.add(returnedCar);
        }
        showAlert("Успех", "Машина была успешно возвращена.", Alert.AlertType.INFORMATION);
    }

    private void clearRentForm() {
        carIDField.clear();
        dateRentedField.setValue(null);
        dateReturnedField.setValue(null);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
