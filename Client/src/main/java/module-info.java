module de.prog3.client {
    requires jakarta.ws.rs;
    requires jakarta.annotation;
    requires jakarta.activation;
    requires hausarbeit.prog3.Common.main;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens de.prog3.client.controller to javafx.fxml;
    opens de.prog3.client.application to javafx.fxml;

    exports de.prog3.client.application;
    exports de.prog3.client.controller;
    exports de.prog3.client.handler;
    exports de.prog3.client.model;

}