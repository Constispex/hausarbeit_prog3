package de.prog3.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.prog3.common.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;



/**
 * Die Klasse übersetzt die Eingaben des Clients und macht sie für den Server
 * verständlich.
 */
public class DbmsClient {
    private final Client client;
    private final String baseURI;
    public static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Setzt die URI und den Client
     *
     * @param baseURI ist die URL zum Server
     */
    public DbmsClient(String baseURI) {
        this.baseURI = baseURI;
        this.client = ClientBuilder.newClient();
    }

    public static JSONObject createJson(User user) {
        try {
            String json = mapper.writeValueAsString(user);
            return new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Schickt Username und Passwort an den Server. Die Eingaben werden per ":" getrennt
     *
     * @param uri  die Zieladresse
     * @param user Der User, der sich anmelden möchte
     * @return Response, ob User existiert
     */
    public Response post(JSONObject jsonObject, String uri) {
        WebTarget target = getTarget("POST", uri);
        Entity<JSONObject> list = Entity.entity((jsonObject), MediaType.APPLICATION_JSON);
        System.out.printf("send: %s%n", jsonObject);

        Response response = target.request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).header("Content-Type", "application/json").property("Content-Type", "application/json").post(list);
        status(response);

        return response;
    }

    public Response get(JSONObject jsonObject, String uri) {
        WebTarget target = getTarget("GET", uri);
        Entity<JSONObject> entity = Entity.entity(jsonObject, MediaType.APPLICATION_JSON);

        return target.request().get();
    }

    /**
     * Schickt an den Server eine normale SQL Query ohne Rückgabewerte (Keine SELECT Anfragen)
     *
     * @param uri   die Zieladresse
     * @param query die zu schickende SQL Query
     * @return Response mit Status
     */
    public Response post(String uri, String query) {
        WebTarget target = getTarget("POST", uri);
        Entity<String> entity = Entity.entity(query, MediaType.APPLICATION_JSON);
        System.out.println(entity);
        Response response = target.request().post(entity);
        status(response);

        return response;
    }

    private WebTarget getTarget(String crud, String uri) {
        System.out.printf("%n--- %s %s%s%n", crud, baseURI, uri);
        return client.target(baseURI + uri);
    }

    /**
     * Dient zum Überprüfen des Statuses. Dieser wird in der Konsole ausgegeben
     *
     * @param response die Serverantwort
     * @return gibt den Statuscode zurück
     */
    private int status(Response response) {
        int code = response.getStatus();
        String reason = response.getStatusInfo().getReasonPhrase();
        System.out.printf("Status: %d %s%n", code, reason);
        return code;
    }

    /**
     * Schickt eine SELECT Anfrage an den Server. Der Server schickt dann das Ergebnis zurück.
     *
     * @param uri    die Zieladresse
     * @param select der SELECT Teil der Anfrage
     * @param where  der WHERE Teil der Anfrage
     * @param sortBy der SORT BY Teil der Anfrage
     * @return Response mit SELECT Ergebnis
     */
    public Response post(String uri, StringBuilder select, StringBuilder where, StringBuilder sortBy) {
        Response response;
        try {
            if (select.toString().equals("SELECT ")) select.append(" *");
            if (sortBy == null) sortBy = new StringBuilder();
            WebTarget target = getTarget("POST", uri);
            Entity<String> entity = Entity.entity(select + " FROM Buecher " + where + sortBy, MediaType.APPLICATION_JSON);
            response = target.request().post(entity);

            return response;
        } catch (NullPointerException e) {
            Logger.getLogger(e.getMessage());
            return Response.status(100, "Bitte setzte erst den Filter, bevor du sortierst!").build();
        }
    }
}
