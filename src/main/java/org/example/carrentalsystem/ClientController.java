package org.example.carrentalsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
    private TextField carIDField, carBrandField, carModelField, carPriceField;
    @FXML
    private TextField totalCost, amountOfDays; // For total days and cost


    @FXML
    private DatePicker dateRentedField, dateReturnedField;

    @FXML
    private Button rentSubmitButton, returnCarButton, viewAvailableButton;

    @FXML
    private TableView<Reserve> yourCarsTable;
    @FXML
    private TableColumn<Reserve, String> reservedCarModelColumn;
    @FXML
    private TableColumn<Reserve, String> reservedCarBrandColumn;
    @FXML
    private TableColumn<Reserve, Integer> reservedCarIDColumn;
    @FXML
    private TableColumn<Reserve, LocalDate> startDateColumn, endDateColumn;

    private final ObservableList<Car> availableCars = FXCollections.observableArrayList();
    private final ObservableList<Reserve> userReserves = FXCollections.observableArrayList();

    private final CarsDAO carsDAO = new CarsDAO();
    private final ReserveDAO reserveDAO = new ReserveDAO();

    private static final Logger logger = Logger.getLogger(ClientController.class.getName());


    private final int currentUserId; // User ID from UserSession

    public ClientController() {
        // Get the logged-in user ID from UserSession
        this.currentUserId = UserSession.getInstance().getUserId();
    }



    @FXML
    public void initialize() {
        configureTables();
        loadUserReservations();
        setupButtonBindings();
        setupDateListeners();
    }
    @FXML
    private void onViewAvailableClick() {
        loadAvailableCars();
        configureTables();
    }





    private void setupDateListeners() {
        dateRentedField.valueProperty().addListener((observable, oldValue, newValue) -> calculateAndUpdateTotal());
        dateReturnedField.valueProperty().addListener((observable, oldValue, newValue) -> calculateAndUpdateTotal());
    }

    @FXML
    private void configureTables() {
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));
        carBrandColumn.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        carPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        carsTable.setItems(availableCars);

        reservedCarIDColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
       reservedCarBrandColumn.setCellValueFactory(param -> {
            Reserve reserve = param.getValue();
            String brand = carsDAO.getCarById(reserve.getCarId()).getCarBrand(); // Метод для получения бренда
            return new SimpleStringProperty(brand);
        });

        reservedCarModelColumn.setCellValueFactory(param -> {
            Reserve reserve = param.getValue();
            String model = carsDAO.getCarById(reserve.getCarId()).getCarModel(); // Метод для получения модели
            return new SimpleStringProperty(model);
        });
        yourCarsTable.setItems(userReserves);

        carsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
                if (selectedCar != null) populateCarForm(selectedCar);
            }
        });
    }

    private void loadAvailableCars() {
        availableCars.setAll(reserveDAO.ViewAvailableCars(dateRentedField.getValue(),dateReturnedField.getValue()));
    }

    private void loadUserReservations() {
        userReserves.setAll(reserveDAO.getClientReservations(currentUserId));
    }
    private void calculateAndUpdateTotal() {
        LocalDate startDate = dateRentedField.getValue();
        LocalDate endDate = dateReturnedField.getValue();

        if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
            // Calculate total days
            long totalDays = startDate.until(endDate).getDays() + 1;

            double pricePerDay;
            try {
                pricePerDay = Double.parseDouble(carPriceField.getText());
            } catch (NumberFormatException e) {
                pricePerDay = 0.0; // Default to 0 if price is invalid
            }
            double totalCostDouble = totalDays * pricePerDay;

            // Update fields
            amountOfDays.setText(String.valueOf(totalDays));
            totalCost.setText(String.format("%.2f", totalCostDouble));
        } else {
            // Reset fields if dates are invalid
            amountOfDays.clear();
            totalCost.clear();
        }
    }

    private void setupButtonBindings() {
        rentSubmitButton.disableProperty().bind(
                carIDField.textProperty().isEmpty()
                        .or(dateRentedField.valueProperty().isNull())
                        .or(dateReturnedField.valueProperty().isNull())
        );
        returnCarButton.disableProperty().bind(
                yourCarsTable.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    @FXML
    private void submitRent(ActionEvent event) {
        if (!validateRentForm()) return;

        int carId = Integer.parseInt(carIDField.getText());
        LocalDate startDate = dateRentedField.getValue();
        LocalDate endDate = dateReturnedField.getValue();

        Car car = availableCars.stream()
                .filter(c -> c.getCarId() == carId)
                .findFirst()
                .orElse(null);

        if (car == null) {
            showAlert("Car Not Found", "No car found with the given ID.", Alert.AlertType.WARNING);
            return;
        }

        try {
            reserveDAO.reserveCar(carId, currentUserId, startDate, endDate);
            Reserve newReservation = new Reserve(startDate, endDate, currentUserId, carId, 0);
            userReserves.add(newReservation);
            availableCars.remove(car);

            clearRentForm();
            showAlert("Success", "Car rented successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            logger.severe("Error while reserving car: " + e.getMessage());
            showAlert("Error", "Failed to rent car. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void returnCar(ActionEvent event) {
        Reserve selectedReserve = yourCarsTable.getSelectionModel().getSelectedItem();
        if (selectedReserve == null) return;

        try {
            reserveDAO.removeReservation(selectedReserve.getReserveId());
            userReserves.remove(selectedReserve);
            availableCars.add(carsDAO.getCarById(selectedReserve.getCarId()));

            showAlert("Success", "Car returned successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            logger.severe("Error while returning car: " + e.getMessage());
            showAlert("Error", "Failed to return car. Please try again.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateRentForm() {
        try {
            Integer.parseInt(carIDField.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Car ID must be numeric.", Alert.AlertType.WARNING);
            return false;
        }

        LocalDate startDate = dateRentedField.getValue();
        LocalDate endDate = dateReturnedField.getValue();

        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            showAlert("Validation Error", "Invalid rental period.", Alert.AlertType.WARNING);
            return false;
        }

        if (startDate.isBefore(LocalDate.now())) {
            showAlert("Validation Error", "Start date cannot be in the past.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void populateCarForm(Car car) {
        carIDField.setText(String.valueOf(car.getCarId()));
        carBrandField.setText(car.getCarBrand());
        carModelField.setText(car.getCarModel());
        carPriceField.setText(String.valueOf(car.getPrice()));
        //dateRentedField.setValue(LocalDate.now());
        //dateReturnedField.setValue(LocalDate.now().plusDays(1));
        calculateAndUpdateTotal();
    }

    private void clearRentForm() {
        carIDField.clear();
        carBrandField.clear();
        carModelField.clear();
        carPriceField.clear();
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
