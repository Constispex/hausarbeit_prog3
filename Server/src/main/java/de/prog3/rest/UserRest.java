package de.prog3.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.prog3.common.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLoginData(JSONObject jsonObject) {
        try {
            String currUser = mapper.writeValueAsString(jsonObject);
            System.out.println(currUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return Response.notAcceptable(null).build();
    }
}
