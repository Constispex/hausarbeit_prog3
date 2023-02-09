package de.prog3.client.controller;

import de.prog3.client.handler.DbmsClient;
import de.prog3.common.User;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static de.prog3.client.handler.DbmsClient.createJson;

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
            System.out.printf("Username: %s \t Password: %s%n", username, password);
            User u = new User(username, password, false);
            final String BASE_URI = "http://localhost:8080/rest";

            DbmsClient dbmsClient = new DbmsClient(BASE_URI);
            System.out.println("create json as string" + createJson(u));
            Response response = dbmsClient.get(createJson(u), "/register");
            System.out.println(response.getStatusInfo());
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


                    boolean isAdmin = response.readEntity(boolean.class);

                    setCurrentUser(new User(username, password, isAdmin));

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/overview.fxml"));
                    Scene secondScene;
                    try {
                        secondScene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        System.err.println("FXML File not found");
                        throw new RuntimeException(e);
                    }
                    Stage mainWindow = new Stage();
                    mainWindow.setScene(secondScene); // set the scene
                    mainWindow.setTitle("Logged in as " + username);
                    mainWindow.show();
                }
                default -> label_error.setText("Unexpected Response");
            }
        }
    }
}
