package de.prog3.rest;


import de.prog3.common.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse verwaltet alle Anfragen die auf die Adresse "/register" kommen
 */
@Path("/register")
public class UserRest {
    private static final Logger logger = LogManager.getRootLogger();
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
     * Bekommt einen User vom Client und überprüft, ob dieser auf dem Restserver liegt.
     * Wenn ja, wird dieser wieder zurückgeschickt
     *
     * @param user der User, der vom Client gesendet wurde
     * @return Response mit dem user, falls dieser existiert.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoginData(User user) {
        for (User u : users) {
            if (u.equals(user)) {
                logger.info("User {} logged in", user.getName());
                return Response.ok(u).build();
            }
        }
        return Response.notAcceptable(null).build();
    }
}