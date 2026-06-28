module com.example.cincuentazo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.example.cincuentazo to javafx.fxml;
    exports com.example.cincuentazo;

    opens com.example.cincuentazo.controller to javafx.fxml;
    exports com.example.cincuentazo.controller;

    exports com.example.cincuentazo.controller.handler;

    exports com.example.cincuentazo.view;

    exports com.example.cincuentazo.concurrency;

    exports com.example.cincuentazo.model;
    exports com.example.cincuentazo.model.game;
    exports com.example.cincuentazo.model.player;
    exports com.example.cincuentazo.model.exception;
}