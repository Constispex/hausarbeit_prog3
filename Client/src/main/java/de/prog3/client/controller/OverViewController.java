package de.prog3.client.controller;

import de.prog3.client.handler.DbmsClient;
import de.prog3.client.model.Book;
import de.prog3.client.model.BookHolder;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;


/**
 * Die Klasse ist der Controller vom DBMS. Hier werden Methoden ausgeführt fürs Löschen und anzeigen der Datenbank.
 */
public class OverViewController {
    private static final String BASE_URI = "http://localhost:8080/rest";
    @FXML
    public CheckBox check_title;
    @FXML
    public CheckBox check_author;
    @FXML
    public CheckBox check_publisher;
    @FXML
    public CheckBox check_rating;
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
    public CheckBox check_subareas;
    @FXML
    public Label label_error;
    @FXML
    public Button button_submit;
    @FXML
    public TableView<Book> table_result;
    @FXML
    public Button admin_edit;
    @FXML
    public Button admin_delete;
    @FXML
    public Button admin_add;

    private static final Logger logger = LogManager.getLogger(OverViewController.class.getName());
    private static final List<String> selectedCols = new ArrayList<>();
    private final DbmsClient dbmsClient = new DbmsClient(BASE_URI);
    private StringBuilder select;
    private StringBuilder where;
    private StringBuilder orderBy;
    private static final String SERVER_ADDRESS = "/sqlquery";

    /**
     * Erstellt ein Buch mit den Inhalten der Spalten
     *
     * @param column aktuelle Buch
     * @return das erstellte Buch
     */
    private static Book setBookData(String[] column) {
        Book b = new Book();
        for (int i = 0; i < selectedCols.size(); i++) {
            String col = selectedCols.get(i);
            switch (col) {
                case "title" -> b.setTitle(column[i]);
                case "author" -> b.setAuthor(column[i]);
                case "publisher" -> b.setPublisher(column[i]);
                case "rating" -> b.setRating(column[i]);
                case "subareas" -> b.setSubareas(column[i]);
                default -> logger.error("no columns detected");
            }
        }
        return b;
    }

    /**
     * Überprüft beim Aufrufen der Seite, ob der Nutzer Admin ist.
     */
    @FXML
    public void initialize() {
        if (SignInController.getCurrentUser().isAdmin()) admin_add.setDisable(false);
    }

    /**
     * Schließt die Seite und öffnet die Log-In Seite wieder.
     */
    public void logOff() {
        Stage stage = (Stage) button_submit.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/signin.fxml"));
        Scene logInScene = null;
        try {
            logInScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        Stage logInWindow = new Stage();
        logInWindow.setScene(logInScene);
        logInWindow.setTitle("LogIn");
        logInWindow.show();
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
                Response response = dbmsClient.post(SERVER_ADDRESS, select, where, orderBy);
                label_error.setText(String.valueOf(response.getStatusInfo()));
                String table = response.readEntity(String.class);
                setResultTable(table);
                label_error.setText(response.getStatus() == 100 ?
                        response.getStatusInfo().getReasonPhrase() : String.valueOf(response.getStatusInfo()));
                orderBy = new StringBuilder();
                break;
            }
        }
    }

    /**
     * Erstellt eine Abfrage aus den ausgewählten Kriterien. Sendet dann die Anfrage an den Server
     */
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

        // erkennt ausgewählte CheckBoxen
        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                String curr = cb.getId().replace("check_", "");
                select.append(curr);
                select.append(", ");
                selectedCols.add(curr);
            }
        }

        // wenn keine Spalten ausgewählt sind, werden alle angezeigt
        if (selectedCols.isEmpty()) {
            for (CheckBox cb : checkBoxes
            ) {
                selectedCols.add(cb.getId().replace("check_", ""));
            }
        }

        // löscht ", " nach der letzten Spalte
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
                        .append(" LIKE\"%")
                        .append(t.getText())
                        .append("%\"");
                countWhere++;
            }
        }
        if (countWhere > 0) where.append(a);
        where.append("Rating >=\"").append(slider_rating.getValue()).append("\"");

        // clears where part if its empty
        if (where.toString().equals("WHERE ")) where.replace(0, where.length(), "");

        Response response = dbmsClient.post(SERVER_ADDRESS, select, where, orderBy);

        String table = response.readEntity(String.class);
        setResultTable(table);

        label_error.setText(String.valueOf(response.getStatusInfo()));

    }

    /**
     * Erstellt ein String-Array (Zeilen) mit String-Arrays (Spalten).
     * Danach erstellt die Methode die ausgewählten Spalten und füllt die Tabelle mit Zeilen
     *
     * @param result Query, die vom Server gesendet wurde
     */
    public void setResultTable(String result) {
        String[] rows = result.split("// ");
        String[] cols = rows[0].split("; ");
        int anzahlZeilen = rows.length;
        int anzahlSpalten = cols.length;

        table_result.getColumns().clear();
        table_result.getItems().clear();

        for (String col : selectedCols
        ) {
            TableColumn<Book, String> curr = new TableColumn<>(col);
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
            try {
                Book b = setBookData(column);
                table_result.getItems().add(b);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.addSuppressed(new ArrayIndexOutOfBoundsException());
            }
        }
    }

    /**
     * Das aktuelle Buch wird von der Singleton-Klasse BookHolder gespeichert und
     * öffnet ein neues Fenster zum Bearbeiten des Buches
     */
    public void editBook() {
        BookHolder bookHolder = BookHolder.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/editbook.fxml"));
        Scene editBookScene = null;

        bookHolder.setBook(getCurrBook());
        try {
            editBookScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        Stage editBookStage = new Stage();
        editBookStage.setScene(editBookScene);
        editBookStage.setTitle("Edit Book");
        editBookStage.showAndWait();

        submit();
        tableClicked();

    }

    /**
     * Löscht das ausgewählte Buch
     */
    public void deleteBook() {
        Book b = getCurrBook();
        if (b != null) {
            String sql = "DELETE FROM buecher WHERE Title = \"" + b.getTitle() + "\"";
            Response response = dbmsClient.post(SERVER_ADDRESS, sql);
            label_error.setText(response.getStatusInfo().toString());
            submit();
        }
        tableClicked();
    }

    /**
     * Überprüft, ob der Benutzer Admin ist und aktiviert bzw. deaktiviert die Admin-Schaltflächen.
     * Die Buttons sind nur aktiviert, wenn eine Zeile ausgewählt ist
     */
    public void tableClicked() {
        boolean adminAccess = SignInController.getCurrentUser().isAdmin();
        Book b = getCurrBook();
        if (b != null) {
            if (adminAccess) {
                admin_edit.setDisable(false);
                admin_delete.setDisable(false);
            }
        } else {
            admin_edit.setDisable(true);
            admin_delete.setDisable(true);
        }

    }

    /**
     * Setzt das ausgewählte Buch
     * @return das ausgewählte Buch, wenn keins ausgewählt ist → return null
     */
    public Book getCurrBook() {
        return table_result.getSelectionModel().getSelectedItem();
    }

    /**
     * öffnet die addbook.fxml Datei. Danach wird die Tabelle neu geladen
     */
    public void addBook() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addbook.fxml"));
        Scene logInScene = null;
        try {
            logInScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        Stage logInWindow = new Stage();
        logInWindow.setScene(logInScene);
        logInWindow.setTitle("Add Book");
        logInWindow.showAndWait();
        submit();
    }
}
