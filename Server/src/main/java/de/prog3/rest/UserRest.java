package de.prog3.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.prog3.common.User;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse verwaltet alle Anfragen die auf die Adresse "/register" kommen
 */

@Path("/register")
public class UserRest {
    static ObjectMapper mapper = new ObjectMapper();
    private final List<User> users = new ArrayList<>();

    /**
     * Constructor erstellt die voreingestellten Benutzer
     * User 1: name: minf psswd: prog3 -> Kein Admin
     * User 2: name: admin psswd: admin -> Ist Admin
     */
    public UserRest() {
        User admin = new User("admin", "admin", "true");
        User normal = new User("minf", "prog3", "false");

        users.add(admin);
        users.add(normal);
    }


    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getLoginData(Entity<JSONObject> list) {
        try {
            String currUser = mapper.writeValueAsString(list.getEntity());
            System.out.println(currUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return Response.notAcceptable(null).build();
    }
}
