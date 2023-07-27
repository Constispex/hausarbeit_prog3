package de.prog3.client.handler;

import de.prog3.common.Query;
import de.prog3.common.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * Die Klasse übersetzt die Eingaben des Clients und macht sie für den Server
 * verständlich.
 */
public class DbmsClient {
    private final Client client;
    private final String baseURI;

    /**
     * Setzt die URI und den Client
     *
     * @param baseURI ist die URL zum Server
     */
    public DbmsClient(String baseURI) {
        this.baseURI = baseURI;
        this.client = ClientBuilder.newClient();
    }

    /**
     * Schickt Username und Passwort an den Server. Die Eingaben werden per ":" getrennt
     *
     * @param uri die Zieladresse
     * @return Response, ob User existiert
     */
    public Response validateUser(User user, String uri) {
        WebTarget target = getTarget("POST", uri);
        System.out.printf("send: %s%n", user);

        Entity<User> list = Entity.entity(user, MediaType.APPLICATION_JSON);
        Response response = target.request().post(list);

        status(response);
        return response;
    }

    /**
     * Schickt an den Server eine normale SQL Query ohne Rückgabewerte (Keine SELECT Anfragen)
     *
     * @param uri   die Zieladresse
     * @param query die zu schickende SQL Query
     * @return Response mit Status
     */
    public Response postQuery(Query query, String uri) {
        WebTarget target = getTarget("POST", uri);
        System.out.printf("send: %s%n", query.toString());

        Entity<Query> list = Entity.entity(query, MediaType.APPLICATION_JSON);
        Response response = target.request().post(list);

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
     */
    private void status(Response response) {
        int code = response.getStatus();
        String reason = response.getStatusInfo().getReasonPhrase();
        System.out.printf("Status: %d %s\t%s%n", code, reason, response.getHeaders());
    }
}