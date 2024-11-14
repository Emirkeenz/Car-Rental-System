module org.example.carrentalsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.carrentalsystem to javafx.fxml;
    exports org.example.carrentalsystem;
}