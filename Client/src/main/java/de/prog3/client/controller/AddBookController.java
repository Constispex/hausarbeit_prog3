package de.prog3.client.controller;

import de.prog3.client.handler.DbmsClient;
import de.prog3.client.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.ws.rs.core.Response;

public class AddBookController {
    static final String BASE_URI = "http://localhost:8080/rest";
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

    public void cancel() {
        Stage curr = (Stage) input_author.getScene().getWindow();
        curr.close();
    }

    public void addBook() {
        Book b = new Book();
        b.setTitle(input_title.getText());
        b.setAuthor(input_author.getText());
        b.setPublisher(input_publisher.getText());
        b.setRating(input_rating.getText());
        b.setSubareas(input_subareas.getText());

        if (!input_title.getText().isEmpty()) {
            int rating;
            try {
                rating = Integer.parseInt(b.getRating());
            } catch (NumberFormatException n) {
                label_error.setText("Rating muss eine Zahl sein");
                return;
            }
            if (rating > 0 && rating <= 5) {
                Response response = dbmsClient.post("/sqlquery", "INSERT INTO buecher (Title, Author, Publisher, Rating, Subareas) VALUES (" + b.toSqlQuery(b) + ");");

                label_error.setText(response.getStatusInfo().getReasonPhrase());
                if (response.getStatus() == 200) {
                    label_error.setText("Hinzufügen erfolgreich");
                }
            } else {
                label_error.setText("Rating muss zwischen 1 und 5 sein");
            }
        } else {
            label_error.setText("please enter at least the title");
        }
    }
}
