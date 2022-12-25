package de.prog3.rest;

import de.prog3.common.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.ArrayList;
import java.util.List;

@Path("/sqlquery")
public class DatabaseRest {
    private final List<User> users = new ArrayList<>();

    public DatabaseRest() {

    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getLoginData(String res, @Context UriInfo uriInfo) {
        System.out.println("Query: " + res);
        return Response.ok().build();

    }
}
