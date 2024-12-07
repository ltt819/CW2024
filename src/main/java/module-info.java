module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.example.demo.model;
    exports com.example.demo.view;
    exports com.example.demo.view.control;
    exports com.example.demo.controller;
    exports com.example.demo;
    opens com.example.demo to javafx.fxml;
    opens com.example.demo.view to javafx.fxml;
    opens com.example.demo.view.control to javafx.fxml;
}