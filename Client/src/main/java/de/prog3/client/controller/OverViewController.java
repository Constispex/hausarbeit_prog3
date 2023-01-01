package de.prog3.client.controller;

import de.prog3.client.handler.DbmsClient;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
    public MenuItem menu_adminPage;
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
    public Label label_error;
    @FXML
    public MenuItem menu_logoff;
    @FXML
    public Button button_submit;
    final String BASE_URI = "http://localhost:8080/rest";
    DbmsClient dbmsClient = new DbmsClient(BASE_URI);
    private StringBuilder select;
    private StringBuilder where;
    private StringBuilder sortBy;

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

        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                select.append(cb.getId().replace("check_", ""));
                select.append(", ");
            }
        }
       if (!select.toString().equals("SELECT ")) {
           select.delete(select.length() - 2, select.length());
       }

        // Where Block
        Set<TextField> textFields = new HashSet<>();
        textFields.add(text_title);
        textFields.add(text_author);
        textFields.add(text_publisher);
        textFields.add(text_subareas);

        int countWhere = 0;
        for (TextField t : textFields) {
            if (!t.getText().isBlank()) {
                if (countWhere > 0) where.append(a);
                where
                        .append(t.getId().replace("text_", ""))
                        .append("=\"")
                        .append(t.getText())
                        .append("\"");
                countWhere++;
            }
        }
        if (slider_rating.getValue() > 0) {
            if (countWhere > 0) where.append(a);
            where.append("Rating=\"").append(slider_rating.getValue()).append("\"");
        }

        if (where.toString().equals("WHERE ")) where.replace(0, where.length(), "");

        Response response = dbmsClient.post("/sqlquery", select, where, sortBy);

        text_result.setText(select + " FROM Informatik " + where);
        label_error.setText(String.valueOf(response.getStatusInfo()));

    }



    public void AdminPage(ActionEvent actionEvent) {
    }

    /**
     * Schließt die Seite und öffnet wieder das Log-in Fenster
     */
    public void logOff() {
        Stage stage = (Stage) button_submit.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/signin.fxml"));
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

    public void setAdminPageButton(Event event) {
        if (SignInController.currentUser.isAdmin()) menu_adminPage.setVisible(true);
    }

    /**
     * Checkt, wonach sortiert werden soll. Die Methode besitzt ein Set mit allen Sortiermöglichkeiten.
     * Falls davor kein Filter gesetzt worden ist, wird die Query dennoch ausgeführt.
     * @param actionEvent beinhaltet den Wert, was sortiert werden soll
     */
    public void setSortyBy(ActionEvent actionEvent) {
        Set<String> sorts = new HashSet<>();
        sorts.add("Title");
        sorts.add("Author");
        sorts.add("Publisher");
        sorts.add("Rating");
        sorts.add("Subareas");

        if (where == null) where = new StringBuilder();
        if (select == null) select = new StringBuilder("SELECT ");
        System.out.println(actionEvent.getTarget());
        String actionTarget = actionEvent.getTarget().toString();
        sortBy = new StringBuilder(" SORT BY ");
        for (String s: sorts
        ) {
            if (actionTarget.contains(s.toLowerCase())){
                sortBy.append(s);
                Response response = dbmsClient.post("/sqlquery", select, where, sortBy);
                label_error.setText(String.valueOf(response.getStatusInfo()));
                text_result.setText(select + " FROM Informatik " + where + sortBy);
                label_error.setText(response.getStatus() == 100 ?
                        response.getStatusInfo().getReasonPhrase() : String.valueOf(response.getStatusInfo()));
                sortBy = new StringBuilder();
                break;
            }
        }
    }
}
