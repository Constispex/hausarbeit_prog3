package de.prog3.client.handler;

import de.prog3.common.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Die Klasse übersetzt die Eingaben des Clients und macht sie für den Server
 * verständlich.
 */
public class DbmsClient {
    private final Client client;
    private final Logger logger = LogManager.getLogger(DbmsClient.class.getName());
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
     * @param uri  die Zieladresse
     * @param user Der User, der sich anmelden möchte
     * @return Response, ob User existiert
     */
    public Response post(String uri, User user) {
        WebTarget target = getTarget("POST", uri);
        String login = user.getName() + ":" + user.getPassword(); // syntax = username:password
        Entity<String> entity = Entity.entity(login, MediaType.TEXT_PLAIN);

        return target.request().post(entity);
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
        Entity<String> entity = Entity.entity(query, MediaType.TEXT_PLAIN);
        Response response = target.request().post(entity);
        status(response);

        return response;
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
            Entity<String> entity = Entity.entity(select + " FROM buecher " + where + sortBy, MediaType.TEXT_PLAIN);
            response = target.request().post(entity);

            return response;
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            return Response.status(100, "Bitte setzte erst den Filter, bevor du sortierst!").build();
        }
    }

    private WebTarget getTarget(String crud, String uri) {
        String log = String.format("%s %s%s", crud, baseURI, uri);
        logger.info(log);
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
        String log = String.format("HTTP Status: %d %s", code, reason);
        logger.info(log);
    }
}
