package de.prog3.client.controller;

import de.prog3.client.handler.DbmsClient;
import de.prog3.client.model.Book;
import de.prog3.client.model.BookHolder;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditBookController {

    static final String BASE_URI = "http://localhost:8080/rest";
    private Book currBook;
    private final DbmsClient dbmsClient = new DbmsClient(BASE_URI);
    @FXML
    public TextField input_title;
    @FXML
    public TextField input_author;
    @FXML
    public TextField input_publisher;
    @FXML
    public TextField input_rating;
    @FXML
    public TextField input_subareas;
    @FXML
    public Label label_error;

    public void initialize() {
        BookHolder bookHolder = BookHolder.getInstance();
        currBook = bookHolder.getBook();

        input_title.setText(currBook.getTitle());
        input_author.setText(currBook.getAuthor());
        input_publisher.setText(currBook.getPublisher());
        input_rating.setText(currBook.getRating());
        input_subareas.setText(currBook.getSubareas());
    }

    public void cancel() {
        Stage curr = (Stage) input_author.getScene().getWindow();
        curr.close();
    }

    public void editBook() {
        String sqlQuery = "UPDATE Buecher SET " +
                "title = '" + input_title.getText() + "', " +
                "author = '" + input_author.getText() + "', " +
                "publisher = '" + input_publisher.getText() + "', " +
                "rating = '" + input_rating.getText() + "', " +
                "subareas = '" + input_subareas.getText() + "' " +
                "WHERE title = '" + currBook.getTitle() + "'";
        Response response = dbmsClient.post("/sqlquery", sqlQuery);
        label_error.setText(response.getStatusInfo().getReasonPhrase());
        if (response.getStatus() == 200) {
            label_error.setText("Bearbeiten war erfolgreich");
        }
    }
}
