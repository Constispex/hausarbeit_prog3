package de.prog3.client.controller;

import de.prog3.client.handler.DbmsClient;
import de.prog3.common.User;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class OverViewController {
    @FXML
    public TextField text_title;
    @FXML
    public TextField text_author;
    @FXML
    public TextField text_publisher;
    @FXML
    public TextField text_subareas;
    @FXML
    public Slider slider_rating;
    @FXML
    public Label text_result;
    @FXML
    public static MenuItem menu_adminPage;

    static {
        User currentUser = SignInController.currentUser;
        if (currentUser.isAdmin()) menu_adminPage.setVisible(true);
    }

    final String BASE_URI = "http://localhost:8080/rest";
    @FXML
    public CheckBox check_subareas;
    @FXML
    public CheckBox check_title;
    @FXML
    public CheckBox check_author;
    @FXML
    public CheckBox check_publisher;
    @FXML
    public CheckBox check_rating;
    @FXML
    public MenuItem menu_sortby_title;
    @FXML
    public MenuItem menu_sortby_author;
    @FXML
    public MenuItem menu_sortby_publisher;
    @FXML
    public MenuItem menu_sortby_rating;
    @FXML
    public MenuItem menu_sortby_subareas;
    @FXML
    public Label label_error;
    @FXML
    public MenuItem menu_logoff;
    @FXML
    public Button button_submit;
    DbmsClient dbmsClient = new DbmsClient(BASE_URI);
    private StringBuilder select;
    private StringBuilder where;
    private String sortBy;

    public void submit(ActionEvent actionEvent) {
        where = new StringBuilder("WHERE ");
        select = new StringBuilder("SELECT ");
        String a = " AND ";

        // Select Block
        Set<CheckBox> checkBoxes = new HashSet<>();
        checkBoxes.add(check_title);
        checkBoxes.add(check_author);
        checkBoxes.add(check_publisher);
        checkBoxes.add(check_rating);
        checkBoxes.add(check_subareas);

        int count = 0;
        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                select.append(cb.getId().replace("check_", ""));
                if (count > 0) select.append(", ");
            }
            count++;
        }

        // Where Block
        Set<TextField> textFields = new HashSet<>();
        textFields.add(text_title);
        textFields.add(text_author);
        textFields.add(text_publisher);
        textFields.add(text_subareas);

        count = 0;
        for (TextField t : textFields) {
            if (!t.getText().isBlank()) {
                if (count > 0) where.append(a);
                where
                        .append(t.getId().replace("text_", ""))
                        .append("=\"")
                        .append(t.getText())
                        .append("\"");
                count++;
            }
        }
        if (slider_rating.getValue() > 0) {
            if (count > 0) where.append(a);
            where.append("Rating=\"").append(slider_rating.getValue()).append("\"");
        }

        if (where.toString().equals("WHERE ")) where.replace(0, where.length(), "");

        Response response = dbmsClient.post("/sqlquery", select, where);

        text_result.setText(select + " FROM Informatik " + where);
        System.out.println(response.getStatusInfo());

    }

    public void sortby_title(ActionEvent actionEvent) {
        sortBy = " SORT BY Title";
        Response response = dbmsClient.post("/sqlquery", select, new StringBuilder(where + sortBy));
        label_error.setText(String.valueOf(response.getStatusInfo()));
        text_result.setText(select + " FROM Informatik " + where + sortBy);
    }

    public void sortby_author(ActionEvent actionEvent) {
        sortBy = " SORT BY Author";
        Response response = dbmsClient.post("/sqlquery", select, new StringBuilder(where + sortBy));
        label_error.setText(String.valueOf(response.getStatusInfo()));
        text_result.setText(select + " FROM Informatik " + where + sortBy);
    }

    public void sortby_publisher(ActionEvent actionEvent) {
        sortBy = " SORT BY Publisher";
        Response response = dbmsClient.post("/sqlquery", select, new StringBuilder(where + sortBy));
        label_error.setText(String.valueOf(response.getStatusInfo()));
        text_result.setText(select + " FROM Informatik " + where + sortBy);
    }

    public void sortby_rating(ActionEvent actionEvent) {
        sortBy = " SORT BY Rating";
        Response response = dbmsClient.post("/sqlquery", select, new StringBuilder(where + sortBy));
        label_error.setText(String.valueOf(response.getStatusInfo()));
        text_result.setText(select + " FROM Informatik " + where + sortBy);
    }

    public void sortby_subareas(ActionEvent actionEvent) {
        sortBy = " SORT BY Subareas";
        Response response = dbmsClient.post("/sqlquery", select, new StringBuilder(where + sortBy));
        label_error.setText(String.valueOf(response.getStatusInfo()));
        text_result.setText(select + " FROM Informatik " + where + sortBy);
    }

    public void AdminPage(ActionEvent actionEvent) {
    }

    public void logOff(ActionEvent actionEvent) {
        Stage stage = (Stage) button_submit.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/overview.fxml"));
        Scene logInScene;
        try {
            logInScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage logInWindow = new Stage();
        logInWindow.setScene(logInScene);
        logInWindow.setTitle("LogIn");
        logInWindow.show();
    }
}
