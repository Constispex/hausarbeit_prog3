module de.prog3.client {
    requires jakarta.ws.rs;
    requires jakarta.annotation;
    requires jakarta.activation;
    requires hausarbeit.prog3.Common.main;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens de.prog3.client.overview.application to javafx.fxml;
    opens de.prog3.client.overview.controller to javafx.fxml;

    opens de.prog3.client.signin.application to javafx.fxml;
    opens de.prog3.client.signin.controller to javafx.fxml;

    exports de.prog3.client.overview.application;
    exports de.prog3.client.overview.controller;

    exports de.prog3.client.signin.application;
    exports de.prog3.client.signin.controller;

    exports de.prog3.client;

}