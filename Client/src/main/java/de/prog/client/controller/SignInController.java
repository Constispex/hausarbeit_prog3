package de.prog.client.controller;

import de.prog.client.handler.DbmsClient;
import de.prog.common.User;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Der Controller verwaltet die Eingabe von Username und Passwort.
 */
public class SignInController {
    @FXML
    public TextField text_username;
    @FXML
    public Button button_submit;
    @FXML
    public Label label_error;
    @FXML
    TextField text_password;
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        SignInController.currentUser = currentUser;
    }

    /**
     * Überprüft die User Eingaben und schickt diese an den Server.
     * Wenn die Eingaben korrekt sind, öffnet sich das Hauptfenster
     */
    public void submit() {
        String username = this.text_username.getText();
        String password = this.text_password.getText();

        if (username.isBlank() && password.isBlank()) return;
        if (username.isBlank() || password.isBlank()) {
            label_error.setText("please enter a name and a password");
            text_password.clear();
        } else {
            setCurrentUser(new User(username, password, false));
            final String BASE_URI = "http://localhost:8080/rest";

            DbmsClient dbmsClient = new DbmsClient(BASE_URI);

            Response response = dbmsClient.validateUser(getCurrentUser(), "/register");

            switch (response.getStatus()) {
                case 406 -> { // not Acceptable
                    label_error.setText("wrong password or user does not exist");
                    text_password.clear();

                }
                case 200 -> // User exist
                {
                    label_error.setText("login successful");
                    Stage stage = (Stage) button_submit.getScene().getWindow();
                    stage.close();

                    setCurrentUser(response.readEntity(User.class));


                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/overview.fxml"));
                    Scene secondScene;
                    try {
                        secondScene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        label_error.setText("FXML File not found");
                        return;
                    }
                    Stage mainWindow = new Stage();
                    mainWindow.setScene(secondScene); // set the scene
                    mainWindow.setTitle("Logged in as " + username);
                    mainWindow.show();
                }
                case 415 -> label_error.setText("Unsupported Media Type");
                default -> label_error.setText("Unexpected Response");
            }
        }
    }
}