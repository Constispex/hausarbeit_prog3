package de.prog3.client.controller;

import de.prog3.client.handler.DbmsClient;
import de.prog3.client.model.Book;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editBookController {

    private static Book currBook;
    final String BASE_URI = "http://localhost:8080/rest";
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

        currBook = (Book) this.label_error.getUserData();
        System.out.println(currBook);

        /*input_title.setText(currBook.getTitle());
        input_author.setText(currBook.getAuthor());
        input_publisher.setText(currBook.getPublisher());
        input_rating.setText(currBook.getRating());
        input_subareas.setText(currBook.getSubareas());*/
    }

    public void cancel() {
        Stage curr = (Stage) input_author.getScene().getWindow();
        curr.close();
    }

    public void editBook() {
        String sqlquery = "UPDATE Buecher SET " +
                "title = '" + input_title + "', " +
                "author = '" + input_author + "', " +
                "publisher = '" + input_publisher + "', " +
                "rating = '" + input_rating + "', " +
                "subareas = '" + input_subareas + "' " +
                "WHERE title = '" + currBook.getTitle() + "'";
        Response response = dbmsClient.post("/sqlquery", sqlquery);
        label_error.setText(response.getStatusInfo().getReasonPhrase());
        if (response.getStatus() == 200) {
            label_error.setText("Hinzufügen erfolgreich");
        }
    }
}
