package de.prog3.client.controller;

import de.prog3.client.handler.DbmsClient;
import de.prog3.client.model.Buch;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

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
    private static final List<String> selectedCols = new ArrayList<>();
    final String BASE_URI = "http://localhost:8080/rest";
    private final DbmsClient dbmsClient = new DbmsClient(BASE_URI);
    private StringBuilder select;
    private StringBuilder where;
    private StringBuilder orderBy;
    @FXML
    public TableView<Buch> table_result;

    private static Buch setBookData(String[] column, List<String> selectedCols) {
        Buch b = new Buch();
        for (int i = 0; i < selectedCols.size(); i++) {
            String col = selectedCols.get(i);
            switch (col) {
                case "title" -> b.setTitle(column[i]);
                case "author" -> b.setAuthor(column[i]);
                case "publisher" -> b.setPublisher(column[i]);
                case "rating" -> b.setRating(column[i]);
                case "subareas" -> b.setSubareas(column[i]);
                default -> System.err.println("no columns detected");
            }
        }
        System.out.println(b);
        return b;
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
        Scene logInScene = null;
        try {
            logInScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        Stage logInWindow = new Stage();
        logInWindow.setScene(logInScene);
        logInWindow.setTitle("LogIn");
        logInWindow.show();
    }

    public void setAdminPageButton(Event event) {
        if (SignInController.currentUser.isAdmin()) menu_adminPage.setVisible(true);
    }

    public void submit() {
        where = new StringBuilder("WHERE ");
        select = new StringBuilder("SELECT ");
        String a = " AND ";
        selectedCols.clear();

        // Select Block
        Queue<CheckBox> checkBoxes = new LinkedList<>();
        checkBoxes.add(check_title);
        checkBoxes.add(check_author);
        checkBoxes.add(check_publisher);
        checkBoxes.add(check_rating);
        checkBoxes.add(check_subareas);

        // detects selected boxes
        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                String curr = cb.getId().replace("check_", "");
                select.append(curr);
                select.append(", ");
                selectedCols.add(curr);
            }
        }

        // no checkboxes selected -> program shows every column
        if (selectedCols.isEmpty()) {
            for (CheckBox cb : checkBoxes
            ) {
                selectedCols.add(cb.getId().replace("check_", ""));
            }
        }

        // deletes ", " after last column
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

        Response response = dbmsClient.post("/sqlquery", select, where, orderBy);

        String table = response.readEntity(String.class);
        setResultTable(table);

        label_error.setText(String.valueOf(response.getStatusInfo()));

    }

    /**
     * Checkt, wonach sortiert werden soll. Die Methode besitzt ein Set mit allen Sortiermöglichkeiten.
     * Falls davor kein Filter gesetzt worden ist, wird die Query dennoch ausgeführt.
     * @param actionEvent beinhaltet den Wert, was sortiert werden soll
     */
    public void setSortyBy(ActionEvent actionEvent) {
        Queue<String> sorts = new LinkedList<>();
        sorts.add("Title");
        sorts.add("Author");
        sorts.add("Publisher");
        sorts.add("Rating");
        sorts.add("Subareas");

        if (where == null) where = new StringBuilder();
        if (select == null) select = new StringBuilder("SELECT ");
        String actionTarget = actionEvent.getTarget().toString();
        orderBy = new StringBuilder(" ORDER BY ");
        for (String s: sorts
        ) {
            if (actionTarget.contains(s.toLowerCase())){
                orderBy.append(s);
                Response response = dbmsClient.post("/sqlquery", select, where, orderBy);
                label_error.setText(String.valueOf(response.getStatusInfo()));
                String table = response.readEntity(String.class);
                setResultTable(table);
                System.out.println(table);
                label_error.setText(response.getStatus() == 100 ?
                        response.getStatusInfo().getReasonPhrase() : String.valueOf(response.getStatusInfo()));
                orderBy = new StringBuilder();
                break;
            }
        }
    }

    public void setResultTable(String result) {
        String[] rows = result.split("// ");
        String[] cols = rows[0].split("; ");
        System.out.println("Rows");
        augabeArray(rows);
        System.out.println("cols");
        augabeArray(cols);
        int anzahlZeilen = rows.length - 1;
        int anzahlSpalten = cols.length;

        table_result.getColumns().clear();
        table_result.getItems().clear();

        for (String col : selectedCols
        ) {
            TableColumn<Buch, String> curr = new TableColumn<>(col);
            curr.setCellValueFactory(new PropertyValueFactory<>(col));
            table_result.getColumns().add(curr);
        }

        // create table array
        String[][] table = new String[anzahlZeilen][anzahlSpalten];
        for (int i = 0; i < anzahlZeilen; i++) {
            String[] currCols = rows[i].split("; ");
            System.arraycopy(currCols, 0, table[i], 0, anzahlSpalten);
        }

        for (String[] column : table
        ) {
            Buch b = setBookData(column, selectedCols);
            table_result.getItems().add(b);
        }
    }

    public void augabeArray(String[] arr) {
        for (String s : arr
        ) {
            System.out.println(s);
        }
    }
}
