module de.prog.client {
    requires jakarta.ws.rs;

    requires jakarta.activation;
    requires jakarta.annotation;
    requires jakarta.inject;


    requires java.xml.bind;

    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;


    requires org.glassfish.jaxb.core;
    requires org.glassfish.jaxb.runtime;

    requires de.prog.common;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens de.prog.client.controller to javafx.fxml;
    opens de.prog.client.application to javafx.fxml;

    exports de.prog.client.application;
    exports de.prog.client.controller;
    exports de.prog.client.handler;
    exports de.prog.client.model;
}