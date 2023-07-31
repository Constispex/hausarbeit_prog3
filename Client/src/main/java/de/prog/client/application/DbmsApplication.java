package de.prog.client.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;


/**
 * Startet das Programm.
 * Lädt die fxml Seite.
 */
public class DbmsApplication extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        try {
            URL fxmlLocation = getClass().getResource("/signin.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception t) {
            Logger logger = LogManager.getRootLogger();
            logger.error(t.getMessage());
            logger.error(t.getStackTrace());
        }

    }
}