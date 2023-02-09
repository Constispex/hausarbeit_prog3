package de.prog3.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.prog3.common.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
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
        User admin = new User("admin", "admin", true);
        User normal = new User("minf", "prog3", false);

        users.add(admin);
        users.add(normal);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLoginData(Object o, @Context UriInfo uriInfo) {
        System.out.println("getLoginData");
        System.out.println(o.toString());
        return Response.ok(o).build();
    }

    @GET
    @Path("{login}")
    @Produces("application/json")
    public Response getUserLogIn(@PathParam("login") JSONObject login) {

        return Response.ok(login, MediaType.APPLICATION_JSON).build();
    }
}
