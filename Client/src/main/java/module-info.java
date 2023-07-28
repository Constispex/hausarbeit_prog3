module de.prog.client {
    requires jakarta.ws.rs;
    requires jakarta.activation;
    requires jakarta.annotation;

    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;

    requires java.xml.bind;
    requires java.logging;

    requires org.json;

    requires hausarbeit.prog3.Common.main;

    requires com.fasterxml.jackson.databind;

    opens de.prog.client.controller to javafx.fxml;
    opens de.prog.client.application to javafx.fxml;

    exports de.prog.client.application;
    exports de.prog.client.controller;
    exports de.prog.client.handler;
    exports de.prog.client.model;
}