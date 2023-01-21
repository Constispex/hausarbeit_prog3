package de.prog3.rest;

import de.prog3.common.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;
import java.util.List;

@Path("register")
public class UserRest {
    private final List<User> users = new ArrayList<>();

    public UserRest() {
        User admin = new User("admin", "admin", true);
        User normal = new User("minf", "prog3", false);

        users.add(admin);
        users.add(normal);
    }


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getLoginData(String res, @Context UriInfo uriInfo) {
        String name;
        String password;
        String[] s = res.split(":");
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        name = s[0];
        password = s[1];

        for (User u : users) {
            if (name.equals(u.getName()) && password.equals(u.getPassword())) {
                return Response.ok(u.isAdmin()).build();
            }
        }
        return Response.notAcceptable(null).build();
    }
}
