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

import java.io.IOException;
import java.util.*;

// TODO comment
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

    private static final List<String> selectedCols = new ArrayList<>();
    @FXML
    public Button admin_add;
    private final DbmsClient dbmsClient = new DbmsClient(BASE_URI);
    private StringBuilder select;
    private StringBuilder where;
    private StringBuilder orderBy;
    private final String SERVER_ADDRESS = "/sqlquery";

    /**
     * Creates a book with the data in the selected columns
     *
     * @param column current Book
     * @return created book
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
                default -> System.err.println("no columns detected");
            }
        }
        System.out.println(b);
        return b;
    }

    /**
     * Checks if User is admin -> Enables/Disables Add Button
     */
    @FXML
    public void initialize() {
        if (SignInController.currentUser.isAdmin()) admin_add.setDisable(false);
    }

    /**
     * Closes the page and opens the login window again
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
                System.out.println(table);
                label_error.setText(response.getStatus() == 100 ?
                        response.getStatusInfo().getReasonPhrase() : String.valueOf(response.getStatusInfo()));
                orderBy = new StringBuilder();
                break;
            }
        }
    }

    /**
     * Creates a query from the selected criteria.
     * Then sends the query to the server
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
        if (countWhere > 0) where.append(a);
        where.append("Rating >=\"").append(slider_rating.getValue()).append("\"");


        if (where.toString().equals("WHERE ")) where.replace(0, where.length(), "");

        Response response = dbmsClient.post(SERVER_ADDRESS, select, where, orderBy);

        String table = response.readEntity(String.class);
        setResultTable(table);

        label_error.setText(String.valueOf(response.getStatusInfo()));

    }

    /**
     * Creates a string array (rows) with string arrays (columns).
     * After that, the method creates the selected columns and fills the table with rows
     *
     * @param result Query sent by the server
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
            Book b = setBookData(column);
            table_result.getItems().add(b);
        }
    }

    /**
     * Is executed when the delete button is pressed.
     * The current book is stored by the singleton class BookHolder.
     * Opens a new window for editing the book
     */
    public void editBook() {
        BookHolder bookHolder = BookHolder.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/editbook.fxml"));
        Scene editBookScene = null;

        bookHolder.setBook(getCurrBook());
        try {
            editBookScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        Stage editBookStage = new Stage();
        editBookStage.setScene(editBookScene);
        editBookStage.setTitle("Edit Book");
        editBookStage.showAndWait();

        submit();
        tableClicked();

    }

    /**
     * Action Event for pressing the delete button.
     * Button deletes current selected Book
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
     * Checks, if User is admin and enables/disables admin buttons.
     * Buttons are enabled only when a row is selected
     */
    public void tableClicked() {
        boolean adminAccess = SignInController.currentUser.isAdmin();
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
     * Get the selected Book.
     *
     * @return selected Book. If none selected -> null
     */
    public Book getCurrBook() {
        return table_result.getSelectionModel().getSelectedItem();
    }

    /**
     * Action Event for pressing the add button.
     * Loads the addbook.fxml file.
     * Then submit() to update the query
     */
    public void addBook() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addbook.fxml"));
        Scene logInScene = null;
        try {
            logInScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        Stage logInWindow = new Stage();
        logInWindow.setScene(logInScene);
        logInWindow.setTitle("Add Book");
        logInWindow.showAndWait();
        submit();
    }
}
