package de.prog3.client.handler;

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

    public DbmsClient(String baseURI) {
        this.baseURI = baseURI;
        this.client = ClientBuilder.newClient();
    }

    public void get(String uri) {
        WebTarget target = getTarget("GET", uri);
        Response response = target.request(MediaType.TEXT_PLAIN).get();
        if (status(response) == 200) {
            String auffuehrung = response.readEntity(String.class);
            System.out.println(auffuehrung);
        }
    }

    public Response post(String uri, User user) {
        WebTarget target = getTarget("POST", uri);
        String login = user.getName() + ":" + user.getPassword(); // syntax = username:password
        Entity<String> entity = Entity.entity(login, MediaType.TEXT_PLAIN);
        Response response = target.request().post(entity);

        if (status(response) == 200) { // response successful
            //String location = response.getLocation().toString();
            String s = response.readEntity(String.class);
            System.out.println(s);
            //System.out.println("Location: " + location);
        }
        return response;
    }

    public Response post(String uri, StringBuilder select, StringBuilder where) {
        if (select.toString().equals("SELECT ")) select.append(" * ");
        WebTarget target = getTarget("POST", uri);
        Entity<String> entity = Entity.entity(select + "FROM Informatik " + where, MediaType.TEXT_PLAIN);
        Response response = target.request().post(entity);

        if (status(response) == 201) {
            String location = response.getLocation().toString();
            System.out.println("Location: " + location);
        }
        return response;
    }

    private WebTarget getTarget(String crud, String uri) {
        System.out.printf("%n--- %s %s%s%n", crud, baseURI, uri);
        return client.target(baseURI + uri);
    }

    private int status(Response response) {
        int code = response.getStatus();
        String reason = response.getStatusInfo().getReasonPhrase();
        System.out.printf("Status: %d %s%n", code, reason);
        return code;
    }
}
