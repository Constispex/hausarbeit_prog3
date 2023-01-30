module de.prog3.client {
    requires jakarta.ws.rs;
    requires jakarta.activation;
    requires jakarta.annotation;
    requires jakarta.xml.bind;
    requires jakarta.inject;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    requires genson;

    requires java.logging;
    requires java.xml.bind;
    requires org.json;
    requires com.google.gson;
    requires hausarbeit.prog3.Common.main;

    requires jersey.hk2;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires jersey.media.json.jackson;
    requires org.glassfish.jaxb.runtime;

    opens de.prog3.client.controller to javafx.fxml;
    opens de.prog3.client.application to javafx.fxml;

    exports de.prog3.client.application;
    exports de.prog3.client.controller;
    exports de.prog3.client.handler;
    exports de.prog3.client.model;
}