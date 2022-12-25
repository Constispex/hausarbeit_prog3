package de.prog3.client.overview.controller;

import de.prog3.client.DbmsClient;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

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
    public CheckBox check_subareas;
    public CheckBox check_title;
    public CheckBox check_author;
    public CheckBox check_publisher;


    public void submit(ActionEvent actionEvent) {
        //PreparedStatement preStatement = new Connection().getPreparedStatement();
        StringBuilder select = new StringBuilder();
        StringBuilder where = new StringBuilder();

        if (!text_title.getText().isBlank()) where.append(AppendToQuery("Title", text_title.getText()));
        if (!text_author.getText().isBlank()) where.append(AppendToQuery("Author", text_author.getText()));
        if (!text_publisher.getText().isBlank()) where.append(AppendToQuery("Publisher", text_publisher.getText()));
        if (!slider_rating.isValueChanging())
            where.append(AppendToQuery("Rating", String.valueOf(slider_rating.getValue())));
        if (!text_subareas.getText().isBlank()) where.append(AppendToQuery("Subareas", text_subareas.getText()));
        where.replace(0, 5, "");

        text_result.setText(where.toString());
        System.out.println(where);

        final String BASE_URI = "http://localhost:8080/rest";

        DbmsClient dbmsClient = new DbmsClient(BASE_URI);

        Response response = dbmsClient.post("/sqlquery", select, where);

        System.out.println(response.getStatusInfo());

    }

    private String AppendToQuery(String col, String isLike) {
        return " AND " + col + " = \"" + isLike + "\"";
    }
}
