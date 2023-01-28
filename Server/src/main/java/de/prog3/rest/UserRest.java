package de.prog3.rest;

import de.prog3.common.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse verwaltet alle Anfragen die auf die Adresse "/register" kommen
 */
@Path("/register")
public class UserRest {
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

    /**
     * Methode bekommt Name und Password eines Users. Die beiden Eingaben werden per ":" abgetrennt.
     *
     * @param res die User Eingabe, geschickt vom Client
     * @return gibt den Status zurück. Und, ob der User Admin ist.
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getLoginData(String res) {
        String name;
        String password;
        String[] s = res.split(":");
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
