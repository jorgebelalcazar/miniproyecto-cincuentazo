module com.example.cincuentazo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.example.cincuentazo to javafx.fxml;
    exports com.example.cincuentazo;

    opens com.example.cincuentazo.controller to javafx.fxml;
    exports com.example.cincuentazo.controller;

    exports com.example.cincuentazo.view;
}